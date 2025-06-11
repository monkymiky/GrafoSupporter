import { Component, Input, inject, HostBinding } from '@angular/core';
import { Combination } from '../combination.interface';
import {
  NgbAccordionModule,
  NgbTooltip,
  NgbActiveModal,
  NgbModal,
} from '@ng-bootstrap/ng-bootstrap';
import { DeleteComponentModalComponent } from './delete-component-modal/delete-component-modal.component';
import { CombinationService } from '../../../shared/combinations.service';
import { SharedStateService } from '../../../shared/shared-state.service';

@Component({
  selector: 'app-single-combination',
  standalone: true,
  templateUrl: './combination.component.html',
  styleUrls: ['./combination.component.css'],
  imports: [NgbAccordionModule, NgbTooltip],
})
export class SingleCombination {
  @Input() combination!: Combination;

  private modalService = inject(NgbModal);
  private combinationService = inject(CombinationService);
  private sharedState = inject(SharedStateService);
  public isExpanded = false;

  @HostBinding('style.display') display = 'contents';

  toggleExpand() {
    this.isExpanded = !this.isExpanded;
  }

  openConfirmDeletionModal() {
    const modalRef = this.modalService.open(DeleteComponentModalComponent);
    modalRef.result.then(
      (result) => {
        if (this.combination.id) {
          this.combinationService
            .deleteCombination(this.combination.id)
            .subscribe({
              next: () => {
                this.sharedState.triggherCombinationsSearch.set(Date.now());
                console.log(
                  `Combinazione ${this.combination.id} eliminata con successo!`
                );
              },
              error: (err) => {
                console.error('Errore durante l eliminazione:', err);
              },
            });
        }
      },
      (reason) => {
        console.log('Modale annullato, motivo:', reason);
      }
    );
  }
}
