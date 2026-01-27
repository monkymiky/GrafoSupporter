import {
  Component,
  Input,
  inject,
  HostBinding,
  HostListener,
} from '@angular/core';
import { Combination } from '../../../../model/combination.interface';
import {
  NgbAccordionModule,
  NgbTooltip,
  NgbModal,
} from '@ng-bootstrap/ng-bootstrap';
import { DeleteComponentModalComponent } from './components/delete-combination-modal/delete-combination-modal.component';
import { CombinationService } from '../../../../services/combinations.service';
import { SharedStateService } from '../../../../../../shared/services/shared-state.service';
import { CombinationsFormComponent } from '../../../../../combinations-form/combination-form.component';
import { AuthService } from '../../../../../../core/services/auth.service';
import { MessageService } from '../../../../../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../../../../../shared/components/message-toast/models/message.interface';
import { CommentSectionComponent } from './components/comment-section/comment-section.component';

@Component({
  selector: 'app-single-combination',
  standalone: true,
  templateUrl: './combination.component.html',
  styleUrls: ['./combination.component.scss'],
  imports: [NgbAccordionModule, NgbTooltip, CommentSectionComponent],
})
export class SingleCombination {
  @Input() combination!: Combination;

  private readonly modalService = inject(NgbModal);
  private readonly combinationService = inject(CombinationService);
  private readonly sharedState = inject(SharedStateService);
  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);
  public isExpanded = false;
  public isTooltipDisabled = false;
  private readonly mobileBreakpoint = 768;

  ngOnInit() {
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
    const modalRef = this.modalService.open(CombinationsFormComponent, {
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
                this.sharedState.combinationsSearchTrigger.set(Date.now());
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

  onAuthorImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.style.display = 'none';
  }

  public canVote(): boolean {
    if (!this.authService.isAuthenticated) {
      return false;
    }
    const currentUser = this.authService.currentUserValue;
    if (!currentUser || !this.combination.author) {
      return false;
    }
    return currentUser.userId !== this.combination.author.id;
  }

  public handleVote(voteType: 'UP' | 'DOWN'): void {
    if (!this.canVote() || !this.combination.id) {
      return;
    }

    const currentVote = this.combination.voteStats?.userVote;
    let newVoteType: 'UP' | 'DOWN' | null = voteType;

    if (currentVote === voteType) {
      newVoteType = null;
    }

    this.combinationService.voteCombination(this.combination.id, newVoteType).subscribe({
      next: (voteStats) => {
        if (!this.combination.voteStats) {
          this.combination.voteStats = voteStats;
        } else {
          this.combination.voteStats.upvotes = voteStats.upvotes;
          this.combination.voteStats.downvotes = voteStats.downvotes;
          this.combination.voteStats.score = voteStats.score;
          this.combination.voteStats.userVote = voteStats.userVote;
        }
        this.sharedState.combinationsSearchTrigger.set(Date.now());
      },
      error: (err) => {
        this.messageService.showMessage(
          `Errore durante il voto: ${err.error?.message || err.message}`,
          MessageType.error
        );
      },
    });
  }
}
