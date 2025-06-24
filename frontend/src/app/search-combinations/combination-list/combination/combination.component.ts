import {
  Component,
  Input,
  inject,
  HostBinding,
  HostListener,
} from '@angular/core';
import { Combination } from '../combination.interface';
import {
  NgbAccordionModule,
  NgbTooltip,
  NgbModal,
} from '@ng-bootstrap/ng-bootstrap';
import { DeleteComponentModalComponent } from './delete-component-modal/delete-component-modal.component';
import { CombinationService } from '../../../shared/combinations.service';
import { SharedStateService } from '../../../shared/shared-state.service';
import { AddCombinationsComponent } from '../../../add-combinations/add-combinations.component';

@Component({
  selector: 'app-single-combination',
  standalone: true,
  templateUrl: './combination.component.html',
  styleUrls: ['./combination.component.scss'],
  imports: [NgbAccordionModule, NgbTooltip],
})
export class SingleCombination {
  @Input() combination!: Combination;

  private modalService = inject(NgbModal);
  private combinationService = inject(CombinationService);
  private sharedState = inject(SharedStateService);
  public isExpanded = false;
  public temperaments: string[] = [];
  public isTooltipDisabled = false;
  private mobileBreakpoint = 768;

  ngOnInit() {
    const signsMap = this.sharedState.signsMap();
    this.temperaments = this.combination.signs.map((sign) => {
      const signData = signsMap.get(sign.signId);
      return signData?.temperamento ?? '';
    });
    this.checkWindowSize();
  }
  private checkWindowSize() {
    this.isTooltipDisabled = window.innerWidth < this.mobileBreakpoint;
  }
  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkWindowSize();
  }
  @HostBinding('style.display') display = 'contents';

  toggleExpand() {
    this.isExpanded = !this.isExpanded;
  }

  openModifyModal() {
    const modalRef = this.modalService.open(AddCombinationsComponent, {
      size: 'xl',
      centered: true,
      fullscreen: 'lg',
    });
    modalRef.componentInstance.combination = this.combination;
    modalRef.result.then(
      (result) => {
        console.log(`Modale chiuso con risultato: ${result}`);
      },
      (reason) => {
        console.log(`Modale annullato, motivo: ${reason}`);
      }
    );
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
                this.sharedState.triggerCombinationsSearch.set(Date.now());
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
