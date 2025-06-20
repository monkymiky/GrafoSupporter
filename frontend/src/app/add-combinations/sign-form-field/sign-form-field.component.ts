import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SingleSignFormModel } from '../add-combinations.component';
import { SignApiResponseItem } from '../../shared/signs.service';

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
  range = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
}
