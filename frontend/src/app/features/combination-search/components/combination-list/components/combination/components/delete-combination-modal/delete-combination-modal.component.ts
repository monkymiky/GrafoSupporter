import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-delete-component-modal',
  imports: [],
  templateUrl: './delete-combination-modal.component.html',
  styleUrl: './delete-combination-modal.component.scss',
})
export class DeleteComponentModalComponent {
  modal = inject(NgbActiveModal);
}
