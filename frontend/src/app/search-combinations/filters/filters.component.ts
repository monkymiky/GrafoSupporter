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
  temperamento: string | null;
  type: string;
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

  singsTypes: Signal<string[]> = computed(() => {
    const result: string[] = [];
    for (const singleSing of this.sharedState.signs()) {
      if (!result.includes(singleSing.tipo)) {
        result.push(singleSing.tipo);
      }
    }
    return result;
  });

  signsData: Signal<SingleSignForm[]> = computed(() => {
    const result: SingleSignForm[] = [];
    for (const singleSing of this.sharedState.signs()) {
      const id = singleSing.id;
      const signName = singleSing.name;
      const temperamento = singleSing.temperamento;
      const type = singleSing.tipo;
      result.push({
        id,
        signName,
        temperamento,
        type,
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
  filteredSignsByType(type: string) {
    return this.signsData()
      .map((data, i) => ({ ...data, index: i }))
      .filter((d) => d.type === type);
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
