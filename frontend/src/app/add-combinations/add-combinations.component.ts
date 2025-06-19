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
import {
  classification,
  Sign,
} from '../search-combinations/combination-list/sign.interface';
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

  const validIds = allIds
    .filter((id) => id !== null && id !== undefined)
    .map((id) => Number(id));

  if (validIds.length < 2) {
    return null;
  }
  const idSet = new Set(validIds);

  if (validIds.length !== idSet.size) {
    return { duplicateSigns: true };
  }
  return null;
}

export function fileSizeValidator(maxSize: number): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const file = control.value;

    if (!(file instanceof File)) {
      return null;
    }

    if (file.size > maxSize) {
      return { maxSize: { actualSize: file.size, requiredSize: maxSize } };
    }
    return null;
  };
}

export function fileTypeValidator(allowedTypes: string[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const file = control.value;

    if (!(file instanceof File)) {
      return null;
    }

    if (!allowedTypes.includes(file.type)) {
      return {
        fileType: { actualType: file.type, allowedTypes: allowedTypes },
      };
    }
    return null;
  };
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
  activeImageFileName: string | null = null;
  RemoteImageFileName: string | null = null;
  imagePreviewUrl: string | null = null;

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
          fileSizeValidator(5 * 1024 * 1024),
          fileTypeValidator([
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

  private mapSignDataToForm(sign: Sign) {
    return {
      signId: sign.signId,
      max: sign.max,
      min: sign.min,
      isAbsent: sign.min === 0 && sign.max === 0,
      isOptional: sign.isOptional,
      classification: sign.classification ?? classification.Sostanziale,
    };
  }

  private loadCombinationData(c: Combination): void {
    this.combinationForm.patchValue({
      id: c.id,
      title: c.title,
      shortDescription: c.shortDescription,
      longDescription: c.longDescription,
      imagePath: c.imagePath,
      firstSign: this.mapSignDataToForm(c.signs[0]),
      secondSign: this.mapSignDataToForm(c.signs[1]),
    });

    if (typeof c.imagePath === 'string') {
      this.RemoteImageFileName = c.imagePath;
    }

    this.combinationForm.controls.otherSigns.clear();
    c.signs.slice(2).forEach((sign) => {
      const signForm = this.createSingleSignForm(false);
      signForm.patchValue(this.mapSignDataToForm(sign));
      this.combinationForm.controls.otherSigns.push(signForm);
    });

    this.errorMessage.set(null);
    this.combinationForm.markAllAsTouched();
  }

  addOtherSign(): void {
    this.combinationForm.controls.otherSigns.push(
      this.createSingleSignForm(false)
    );
  }
  removeOtherSign(i: number): void {
    this.combinationForm.controls.otherSigns.removeAt(i);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file: File | null = input.files ? input.files[0] : null;

    if (file) {
      this.activeImageFileName = file.name;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreviewUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
      this.combinationForm.get('imagePath')?.patchValue(file);
      this.combinationForm.get('imagePath')?.updateValueAndValidity();
    } else {
      this.activeImageFileName = null;
      this.imagePreviewUrl = null;
      this.combinationForm.get('imagePath')?.patchValue(null);
      this.combinationForm.get('imagePath')?.updateValueAndValidity();
    }
  }

  onSubmit(): void {
    this.errorMessage.set(null);
    this.combinationForm.markAllAsTouched();

    if (this.combinationForm.invalid) {
      console.warn('Form is invalid. Submission aborted.');
      return;
    }

    const formValue = this.combinationForm.getRawValue();

    const getSignNameById = (id: number | null): string => {
      if (id === null) return 'ID nullo';
      const sign = this.sharedState.signs().find((s) => s.id == id);
      return sign?.name ?? `Nome non trovato per ID: ${id}`;
    };

    const combinationData: Combination = {
      id: this.isEditMode() ? this.combination!.id : undefined,
      title: formValue.title,
      shortDescription: formValue.shortDescription,
      longDescription: formValue.longDescription,
      imagePath: null,
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

    const saveCombination = (finalCombinationData: Combination) => {
      const saveOperation = this.isEditMode()
        ? this.combinationsService.updateCombination(
            finalCombinationData.id!,
            finalCombinationData
          )
        : this.combinationsService.createCombination(
            finalCombinationData as Omit<Combination, 'id'>
          );

      saveOperation
        .pipe(
          finalize(() =>
            this.sharedState.triggerCombinationsSearch.set(Date.now())
          )
        )
        .subscribe({
          next: () => {
            if (this.isEditMode()) {
              this.activeModal.close('submit');
            } else {
              this.successMessage.set(
                "Combinazione inserita con successo, se vuoi puoi inserirne un'altra."
              );
              this.combinationForm.reset();
            }
          },
          error: (err) => {
            const action = this.isEditMode() ? 'aggiornamento' : 'creazione';
            this.errorMessage.set(
              `Errore durante l'${action} della combinazione: ${
                err.error?.message || err.message
              }`
            );
          },
        });
    };

    const imageValue = this.combinationForm.get('imagePath')?.value;

    if (imageValue instanceof File) {
      const formImageData = new FormData();
      formImageData.append('imageFile', imageValue);

      this.fileUploadService.uploadCombiantionImage(formImageData).subscribe({
        next: (response) => {
          combinationData.imagePath = response.fileName;
          this.activeImageFileName = response.fileName;
          saveCombination(combinationData);
        },
        error: (err) => {
          this.errorMessage.set(
            `Errore durante l'upload dell'immagine: ${
              err.error?.message || err.message
            }`
          );
        },
      });
    } else {
      if (
        this.isEditMode() &&
        typeof this.combination?.imagePath === 'string'
      ) {
        combinationData.imagePath = this.combination.imagePath;
      } else {
        combinationData.imagePath = null;
      }
      saveCombination(combinationData);
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
}
