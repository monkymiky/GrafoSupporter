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
import { MessageService } from '../shared/error-handling/message.service';

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
  selector: 'app-combinations-form',
  imports: [CommonModule, ReactiveFormsModule, SignFormFieldComponent],
  templateUrl: './combinations-form.component.html',
  styleUrl: './combinations-form.component.scss',
})
export class CombinationsFormComponent {
  activeModal = inject(NgbActiveModal);
  messageService = inject(MessageService);
  private readonly formBuilder = inject(FormBuilder);
  sharedState = inject(SharedStateService);
  private readonly combinationsService = inject(CombinationService);
  private readonly fileUploadService = inject(FileUploadService);
  activeImageFileName: string | null = null;
  RemoteImageFileName: string | null = null;
  imagePreviewUrl: string | null = null;
  input: HTMLInputElement | null = null;

  combinationForm!: FormGroup<CombinationFormModel>;

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
    this.input = event.target as HTMLInputElement;
    const file: File | null = this.input.files ? this.input.files[0] : null;

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
    this.combinationForm.markAllAsTouched();

    if (this.combinationForm.invalid) {
      console.warn('Form is invalid. Submission aborted.');
      return;
    }

    const formValue = this.combinationForm.getRawValue();

    const signsMap = this.sharedState.signsMap();
    const getSignDataById = (id: number | null) => {
      if (id === null) return null;
      return signsMap.get(id) ?? null;
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
          name:
            getSignDataById(formValue.firstSign.signId)?.name ??
            'Nome non trovato',
          min: formValue.firstSign.min,
          max: formValue.firstSign.max,
          isOptional: formValue.firstSign.isOptional,
          classification: formValue.firstSign.classification,
          temperamento:
            getSignDataById(formValue.firstSign.signId)?.temperamento ?? null,
        },
        {
          signId: +formValue.secondSign.signId!,
          name:
            getSignDataById(formValue.secondSign.signId)?.name ??
            'Nome non trovato',
          min: formValue.secondSign.min,
          max: formValue.secondSign.max,
          isOptional: formValue.secondSign.isOptional,
          classification: formValue.secondSign.classification,
          temperamento:
            getSignDataById(formValue.secondSign.signId)?.temperamento ?? null,
        },
        ...(formValue.otherSigns || []).map((sign) => {
          const signData = getSignDataById(sign.signId);
          return {
            signId: +sign.signId!,
            name: signData?.name ?? 'Nome non trovato',
            min: sign.min,
            max: sign.max,
            isOptional: sign.isOptional,
            classification: sign.classification as classification,
            temperamento: signData?.temperamento ?? null,
          };
        }),
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
              this.messageService.showMessage(
                "Combinazione inserita con successo, se vuoi puoi inserirne un'altra.",
                3
              );
              this.combinationForm.reset();
              this.activeImageFileName = null;
              this.RemoteImageFileName = null;
              if (this.input?.files) {
                this.input.value = '';
              }
            }
          },
          error: (err) => {
            const action = this.isEditMode() ? 'aggiornamento' : 'creazione';
            this.messageService.showMessage(
              `Errore durante l'${action} della combinazione: ${
                err.error?.message ?? err.message
              }`,
              0
            );
          },
        });
    };

    const imageValue = this.combinationForm.get('imagePath')?.value;

    if (imageValue instanceof File) {
      const formImageData = new FormData();
      formImageData.append('imageFile', imageValue);

      this.fileUploadService.uploadCombinationImage(formImageData).subscribe({
        next: (response) => {
          combinationData.imagePath = response.fileName;
          this.activeImageFileName = response.fileName;
          saveCombination(combinationData);
        },
        error: (err) => {
          this.messageService.showMessage(
            `Errore durante l'upload dell'immagine: ${
              err.error?.message ?? err.message
            }`,
            0
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
