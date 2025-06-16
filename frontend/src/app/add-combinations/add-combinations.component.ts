import { Component, computed, inject, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  FormControl,
  FormArray,
  ValidatorFn,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { SharedStateService } from '../shared/shared-state.service';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { classification } from '../search-combinations/combination-list/sign.interface';
import { CombinationService } from '../shared/combinations.service';
import { SignFormFieldComponent } from './sign-form-field/sign-form-field.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FileUploadService } from '../shared/file-upload.service';
import { finalize } from 'rxjs';

export interface SingleSignFormModel {
  signId: FormControl<number | null>;
  max: FormControl<number>;
  min: FormControl<number>;
  isAbsent: FormControl<boolean>;
  isOptional: FormControl<boolean>;
  classification: FormControl<
    | classification.Sostanziale
    | classification.Modificante
    | classification.Accidentale
  >;
}

interface CombinationFormModel {
  id: FormControl<number | null>;
  title: FormControl<string>;
  shortDescription: FormControl<string>;
  longDescription: FormControl<string | null>;
  imagePath: FormControl<string | File | null>;
  firstSign: FormGroup<SingleSignFormModel>;
  secondSign: FormGroup<SingleSignFormModel>;
  otherSigns: FormArray<FormGroup<SingleSignFormModel>>;
}

export function intervalValidator(
  control: AbstractControl
): ValidationErrors | null {
  const group = control as FormGroup<SingleSignFormModel>;

  const min = group.controls.min?.value;
  const max = group.controls.max?.value;

  if (min === null || max === null) {
    return null;
  }

  return min > max ? { invalidInterval: true } : null;
}

export function SignIsNotAbsentValidator(
  control: AbstractControl
): ValidationErrors | null {
  const group = control as FormGroup<SingleSignFormModel>;

  const min = group.controls.min?.value;
  const max = group.controls.max?.value;

  if (min === null || max === null) {
    return null;
  }

  return min == 0 && max == 0 ? { RequiredSignCanTBeAbsent: true } : null;
}

export function uniqueSignsValidator(
  control: AbstractControl
): ValidationErrors | null {
  const group = control as FormGroup<CombinationFormModel>;

  const firstSignId = group.controls.firstSign.get('signId')?.value;
  const secondSignId = group.controls.secondSign.get('signId')?.value;

  const otherSigns = group.get('otherSigns') as FormArray;
  const otherSignsIds = otherSigns.controls.map(
    (signControl) => signControl.get('signId')?.value
  );
  const allIds = [firstSignId, secondSignId, ...otherSignsIds];

  const validIds = allIds.filter((id) => id !== null && id !== undefined);

  if (validIds.length < 2) {
    return null;
  }
  const idSet = new Set(validIds);

  if (validIds.length !== idSet.size) {
    return { duplicateSigns: true };
  }
  return null;
}

@Component({
  selector: 'app-add-combinations',
  imports: [CommonModule, ReactiveFormsModule, SignFormFieldComponent],
  templateUrl: './add-combinations.component.html',
  styleUrl: './add-combinations.component.css',
})
export class AddCombinationsComponent {
  activeModal = inject(NgbActiveModal);
  private formBuilder = inject(FormBuilder);
  sharedState = inject(SharedStateService);
  private combinationsService = inject(CombinationService);
  private fileUploadService = inject(FileUploadService);
  selectedFileName: string | null = null;

  combinationForm!: FormGroup<CombinationFormModel>;

  readonly errorMessage = signal<string | null>(null);
  readonly successMessage = signal<string | null>(null);
  readonly isEditMode = signal(false);
  @Input() combination: Combination | null = null;

  readonly pageTitle = computed(() =>
    this.isEditMode() ? 'Modifica Combinazione' : 'Aggiungi Combinazione'
  );
  readonly submitButtonText = computed(() =>
    this.isEditMode() ? 'Modifica' : 'Salva'
  );

  ngOnInit(): void {
    this.initForm();
    if (this.combination !== null) {
      this.isEditMode.set(true);
      this.loadCombinationData(this.combination);
    } else {
      this.isEditMode.set(false);
    }
  }

  private initForm(): void {
    this.combinationForm = this.formBuilder.group<CombinationFormModel>(
      {
        id: this.formBuilder.control<number | null>(null),
        title: this.formBuilder.control<string>('', {
          nonNullable: true,
          validators: [Validators.required, Validators.minLength(10)],
        }),
        shortDescription: this.formBuilder.control<string>('', {
          nonNullable: true,
          validators: [
            Validators.required,
            Validators.minLength(30),
            Validators.maxLength(300),
          ],
        }),
        longDescription: this.formBuilder.control<string>('', {
          nonNullable: true,
          validators: [Validators.maxLength(2048)],
        }),
        imagePath: this.formBuilder.control<string | File | null>(null, [
          this.fileSizeValidator(5 * 1024 * 1024),
          this.fileTypeValidator([
            'image/png',
            'image/jpg',
            'image/jpeg',
            'image/gif',
            'image/webp',
            'image/svg',
          ]),
        ]),
        firstSign: this.createSingleSignForm(true),
        secondSign: this.createSingleSignForm(true),
        otherSigns: this.formBuilder.array<FormGroup<SingleSignFormModel>>([]),
      },
      {
        validators: [uniqueSignsValidator],
      }
    );
    this.combinationForm.controls.firstSign.controls.isOptional.disable();
    this.combinationForm.controls.secondSign.controls.isOptional.disable();
  }

  private createSingleSignForm(
    isRequiredAndNotAbsent: boolean
  ): FormGroup<SingleSignFormModel> {
    const groupValidators: ValidatorFn[] = [intervalValidator];

    if (isRequiredAndNotAbsent) {
      groupValidators.push(SignIsNotAbsentValidator);
    }

    return this.formBuilder.group<SingleSignFormModel>(
      {
        signId: this.formBuilder.control<number | null>(null, {
          validators: [Validators.required],
        }),
        min: this.formBuilder.control<number>(0, {
          nonNullable: true,
          validators: [
            Validators.min(0),
            Validators.max(10),
            Validators.required,
          ],
        }),
        max: this.formBuilder.control<number>(0, {
          nonNullable: true,
          validators: [
            Validators.min(0),
            Validators.max(10),
            Validators.required,
          ],
        }),
        isAbsent: this.formBuilder.control<boolean>(false, {
          nonNullable: true,
          validators: [Validators.required],
        }),
        isOptional: this.formBuilder.control<boolean>(false, {
          nonNullable: true,
          validators: [Validators.required],
        }),
        classification: this.formBuilder.control<classification>(
          classification.Sostanziale,
          {
            nonNullable: true,
            validators: [Validators.required],
          }
        ),
      },
      {
        validators: groupValidators,
      }
    );
  }

  private loadCombinationData(c: Combination): void {
    const combination = c;
    let isSignAbsent: boolean[] = [];

    for (const sign of combination.signs) {
      sign.min == 0 && sign.max == 0
        ? isSignAbsent.push(true)
        : isSignAbsent.push(false);
    }

    this.combinationForm.patchValue({
      id: combination.id,
      title: combination.title,
      shortDescription: combination.shortDescription,
      longDescription: combination.longDescription,
      imagePath: combination.imagePath,
      firstSign: {
        signId: combination.signs[0].signId,
        max: combination.signs[0].max,
        min: combination.signs[0].min,
        isAbsent: isSignAbsent[0],
        isOptional: combination.signs[0].isOptional,
        classification:
          combination.signs[0].classification ?? classification.Sostanziale,
      },
      secondSign: {
        signId: combination.signs[1].signId,
        max: combination.signs[1].max,
        min: combination.signs[1].min,
        isAbsent: isSignAbsent[1],
        isOptional: combination.signs[1].isOptional,
        classification:
          combination.signs[1].classification ?? classification.Sostanziale,
      },
    });
    this.combinationForm.controls.otherSigns.clear();

    for (let i = 2; i < combination.signs.length; i++) {
      const sign = combination.signs[i];
      const isAbsent = sign.min === 0 && sign.max === 0;

      const signForm = this.createSingleSignForm(false);
      signForm.patchValue({
        signId: sign.signId,
        min: sign.min,
        max: sign.max,
        isAbsent: isAbsent,
        isOptional: sign.isOptional,
        classification: sign.classification ?? classification.Sostanziale,
      });
      this.combinationForm.controls.otherSigns.push(signForm);
    }
    this.errorMessage.set(null);
  }

  addOtherSign(): void {
    this.combinationForm.controls.otherSigns.push(
      this.createSingleSignForm(false)
    );
  }
  removeOtherSign(i: number): void {
    this.combinationForm.controls.otherSigns.removeAt(i);
  }

  fileSizeValidator(maxSize: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const file = control.value as File;
      if (file && file.size > maxSize) {
        return { maxSize: { actualSize: file.size, requiredSize: maxSize } };
      }
      return null;
    };
  }

  fileTypeValidator(allowedTypes: string[]): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const file = control.value as File;
      if (file && !allowedTypes.includes(file.type)) {
        return {
          fileType: { actualType: file.type, allowedTypes: allowedTypes },
        };
      }
      return null;
    };
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file: File | null = input.files ? input.files[0] : null;

    if (file) {
      this.selectedFileName = file.name;
      this.combinationForm.get('imagePath')?.patchValue(file);
      this.combinationForm.get('imagePath')?.updateValueAndValidity();
    } else {
      this.selectedFileName = null;
      this.combinationForm.get('imagePath')?.patchValue(null);
      this.combinationForm.get('imagePath')?.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    this.errorMessage.set(null);

    if (this.combinationForm.invalid) {
      // const invalidFields = this.getInvalidControls(this.combinationForm);
      // console.warn('Campi invalidi:', invalidFields);
      this.combinationForm.markAllAsTouched();
      return;
    }

    const formValue = this.combinationForm.getRawValue();
    const imageFile = this.combinationForm.get('imagePath')?.value;

    const getSignNameById = (id: number | null): string => {
      if (id === null) return 'ID nullo';
      const sign = this.sharedState.signs().find((s) => s[0] == id);
      return sign?.[1][0] ?? `Nome non trovato per ID: ${id}`;
    };

    const combinationData: Combination = {
      id: this.isEditMode() ? this.combination?.id! : undefined,
      title: formValue.title,
      shortDescription: formValue.shortDescription,
      longDescription: formValue.longDescription,
      imagePath: this.selectedFileName,
      originalTextCondition: null,
      author: 'Utente',
      sourceBook: null,
      signs: [
        {
          signId: +formValue.firstSign.signId!,
          name: getSignNameById(formValue.firstSign.signId),
          min: formValue.firstSign.min,
          max: formValue.firstSign.max,
          isOptional: formValue.firstSign.isOptional,
          classification: formValue.firstSign.classification,
        },
        {
          signId: +formValue.secondSign.signId!,
          name: getSignNameById(formValue.secondSign.signId),
          min: formValue.secondSign.min,
          max: formValue.secondSign.max,
          isOptional: formValue.secondSign.isOptional,
          classification: formValue.secondSign.classification,
        },

        ...(formValue.otherSigns || []).map((sign) => ({
          signId: +sign.signId!,
          name: getSignNameById(sign.signId),
          min: sign.min,
          max: sign.max,
          isOptional: sign.isOptional,
          classification: sign.classification as classification,
        })),
      ],
    };

    const saveCombinationToDatabase = () => {
      if (this.isEditMode()) {
        this.combinationsService
          .updateCombination(combinationData.id!, combinationData)
          .pipe(
            finalize(() =>
              this.sharedState.triggherCombinationsSearch.set(Date.now())
            )
          )
          .subscribe({
            next: () => {
              this.activeModal.close('submit');
            },
            error: (err) => {
              console.error(
                "Errore durante l'aggiornamento della combinazione:",
                err
              );
              this.errorMessage.set(
                `Errore durante l'aggiornamento della combinazione: ${
                  err.error?.message || err.message
                }`
              );
            },
          });
      } else {
        this.combinationsService
          .createCombination(combinationData as Omit<Combination, 'id'>)
          .pipe(
            finalize(() =>
              this.sharedState.triggherCombinationsSearch.set(Date.now())
            )
          )
          .subscribe({
            next: () => {
              this.successMessage.set(
                "Combinazione inserita con successo, se vuoi puoi inserirne un'altra."
              );
              this.combinationForm.reset();
            },
            error: (err) => {
              console.error(
                'Errore durante la creazione della combinazione:',
                err
              );
              this.errorMessage.set(
                `Errore durante la creazione della combinazione: ${
                  err.error?.message || err.message
                }`
              );
            },
          });
      }
    };

    if (imageFile && typeof imageFile === 'object' && 'name' in imageFile) {
      const formImageData = new FormData();
      formImageData.append('imageFile', imageFile);

      this.fileUploadService.uploadCombiantionImage(formImageData).subscribe({
        next: (response) => {
          combinationData.imagePath = response.fileName;
          console.log('Immagine caricata con successo!');
          this.selectedFileName = response.fileName;
          saveCombinationToDatabase();
        },
        error: (err) => {
          console.error("Errore durante l'upload dell'immagine:", err);
          this.errorMessage.set(
            `Errore durante l'upload dell'immagine: ${
              err.error?.message || err.message
            }`
          );
        },
      });
    } else if (
      this.isEditMode() &&
      typeof this.combination?.imagePath === 'string'
    ) {
      combinationData.imagePath = this.combination.imagePath;
      saveCombinationToDatabase();
    } else {
      saveCombinationToDatabase();
    }
  }

  get titleCtrl() {
    return this.combinationForm.controls.title;
  }
  get shortDescriptionCtrl() {
    return this.combinationForm.controls.shortDescription;
  }
  get longDescriptionCtrl() {
    return this.combinationForm.controls.longDescription;
  }
  get imagePathCtrl() {
    return this.combinationForm.controls.imagePath;
  }

  // private getInvalidControls(
  //   form: FormGroup | FormArray,
  //   path: string = ''
  // ): string[] {
  //   let invalidControls: string[] = [];

  //   Object.entries(form.controls).forEach(([key, control]) => {
  //     const currentPath = path ? `${path}.${key}` : key;

  //     if (control instanceof FormGroup || control instanceof FormArray) {
  //       invalidControls = invalidControls.concat(
  //         this.getInvalidControls(control, currentPath)
  //       );
  //     } else if (control.invalid) {
  //       invalidControls.push(currentPath);
  //     }
  //   });

  //   return invalidControls;
  //}
}
