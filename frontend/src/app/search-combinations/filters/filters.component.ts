import {
  Component,
  computed,
  effect,
  inject,
  Signal,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  FormControl,
  FormArray,
} from '@angular/forms';
import { Grado } from './filter.interface';
import { SharedStateService } from '../../shared/shared-state.service';
import { NgbTooltip, NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';

interface SingleSignForm {
  id: number;
  signName: string;
  temperamento: string;
  radioOptions: { value: number }[];
}

@Component({
  selector: 'app-filters',
  imports: [ReactiveFormsModule, CommonModule, NgbTooltip],
  providers: [NgbTooltipConfig],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.css',
})
export class FiltersComponent {
  private formBuilder = inject(FormBuilder);
  private sharedState = inject(SharedStateService);

  signsForm!: FormGroup;

  readonly errorMessage = signal<string | null>(null);

  signsData: Signal<SingleSignForm[]> = computed(() => {
    const result: SingleSignForm[] = [];
    for (const [id, [signName, temperamento]] of this.sharedState.signs()) {
      result.push({
        id,
        signName,
        temperamento,
        radioOptions: [
          { value: Grado.ASSENTE },
          { value: Grado.BASSO },
          { value: Grado.SOTTO_MEDIA },
          { value: Grado.MEDIO },
          { value: Grado.SOPRA_MEDIA },
          { value: Grado.ALTO },
        ],
      });
    }
    return result;
  });

  constructor(config: NgbTooltipConfig) {
    config.container = 'body';
    config.placement = 'bottom';
    config.tooltipClass = 'tooltipW';
    effect(() => {
      const currentSignsData = this.signsData();
      this.signsForm = this.formBuilder.group({
        rowSelections: this.formBuilder.array(
          currentSignsData.map(() =>
            this.formBuilder.control<Grado | null>(null)
          )
        ),
      });
    });
  }

  ngOnInit(): void {
    this.sharedState.triggerSignsRequest.set(Date.now());
  }
  get rowSelectionsFormArray(): FormArray {
    return this.signsForm.get('rowSelections') as FormArray;
  }

  onRadioClick(rowIndex: number, radioValue: number): void {
    const formArray = this.rowSelectionsFormArray;
    const currentControl = formArray.at(rowIndex) as FormControl;
    const currentValue = currentControl.value;

    if (currentValue === radioValue) {
      currentControl.setValue(null);
    } else {
      currentControl.setValue(radioValue);
    }
  }

  onSubmit(): void {
    this.errorMessage.set('');
    if (this.signsForm.invalid) {
      this.signsForm.markAllAsTouched();
      return;
    }

    const selections: (Grado | null)[] =
      this.signsForm.getRawValue().rowSelections;
    const newFilterMap = new Map<number, Grado>();
    const currentSignsData = this.signsData();

    for (let i = 0; i < currentSignsData.length; i++) {
      const signId = currentSignsData[i].id;
      const selectedValue = selections[i];

      if (selectedValue !== null) {
        newFilterMap.set(signId, selectedValue);
      } else {
        newFilterMap.set(signId, Grado.ASSENTE);
      }
    }

    this.sharedState.filters.set(newFilterMap);
    this.sharedState.triggerCombinationsSearch.set(Date.now());
  }
}
