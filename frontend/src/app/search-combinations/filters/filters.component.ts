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
  Validators,
  FormControl,
  FormArray,
} from '@angular/forms';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SignsService } from '../../shared/signs.service';
import { Grado } from './filter.interface';
import { SharedStateService } from '../../shared/shared-state.service';

interface SingleSignForm {
  id: number;
  signName: string;
  temperamento: string;
  radioOptions: { value: number }[];
}

@Component({
  selector: 'app-filters',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.css',
})
export class FiltersComponent {
  private formBuilder = inject(FormBuilder);
  private signsService = inject(SignsService);
  private sharedState = inject(SharedStateService);

  signsForm!: FormGroup;

  readonly errorMessage = signal<string | null>(null);

  triggerSignsRequest = signal(0);
  private signsObservable: Observable<Map<number, [string, string]>> =
    toObservable(this.triggerSignsRequest).pipe(
      startWith([]),
      switchMap(() => {
        return this.signsService.getSigns().pipe(
          tap(() => this.errorMessage.set('')),
          catchError((err) => {
            this.errorMessage.set(`Errore caricamento segni : ${err.message}`);
            return of(
              new Map<number, [string, string]>() as Map<
                number,
                [string, string]
              >
            );
          })
        );
      })
    );
  signs = toSignal(this.signsObservable, {
    initialValue: new Map<number, [string, string]>(),
  });

  signsData: Signal<SingleSignForm[]> = computed(() => {
    const result: SingleSignForm[] = [];
    for (const [id, [signName, temperamento]] of this.signs()) {
      result.push({
        id,
        signName,
        temperamento,
        radioOptions: [
          // Esempio di opzioni, adattale a Grado
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

  constructor() {
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
    this.triggerSignsRequest.set(Date.now());
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
  }
}
