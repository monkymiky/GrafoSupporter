import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SingleSignFormModel } from '../combination-form.component';
import { SignApiResponseItem } from '../../combination-search/components/combination-list/services/signs.service';
import { SharedStateService } from '../../../shared/services/shared-state.service';

@Component({
  selector: 'app-sign-form-field',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sign-form-field.component.html',
  styleUrl: './sign-form-field.component.scss',
})
export class SignFormFieldComponent {
  @Input({ required: true }) signFormGroup!: FormGroup<SingleSignFormModel>;
  @Input({ required: true }) signIndex!: number;
  @Input() availableSigns: SignApiResponseItem[] = [];
  sharedState = inject(SharedStateService);

  filteredSignsByType(type: string) {
    return this.availableSigns
      .map((sign, i) => ({ ...sign, index: i }))
      .filter((s) => s.tipo === type);
  }

  range = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
}
