import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-delete-component-modal',
  imports: [],
  templateUrl: './delete-component-modal.component.html',
  styleUrl: './delete-component-modal.component.scss',
})
export class DeleteComponentModalComponent {
  modal = inject(NgbActiveModal);
}
