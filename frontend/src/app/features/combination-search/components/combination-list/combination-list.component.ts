import { Component, inject } from '@angular/core';
import { SingleCombination } from './components/combination/combination.component';
import { CombinationService } from '../../services/combinations.service';
import { SignsService } from './services/signs.service';
import { catchError, Observable, of, switchMap, tap } from 'rxjs';
import { Combination } from '../../model/combination.interface';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SharedStateService } from '../../../../shared/services/shared-state.service';
import {
  NgbAccordionModule,
  NgbTooltipConfig,
  NgbTooltip,
} from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from '../../../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../../../shared/components/message-toast/models/message.interface';
import { EXAMPLE_COMBINATIONS } from '../../constants/example-combinations';

@Component({
  selector: 'app-combination-list',
  imports: [SingleCombination, NgbAccordionModule, NgbTooltip],
  providers: [NgbTooltipConfig],
  templateUrl: './combination-list.component.html',
  styleUrl: './combination-list.component.scss',
})
export class CombinationListComponent {
  combinationsService = inject(CombinationService);
  sharedState = inject(SharedStateService);
  messageService = inject(MessageService);
  signsService = inject(SignsService);

  readonly combinationsObservable: Observable<Combination[]> = toObservable(
    this.sharedState.combinationsSearchTrigger
  ).pipe(
    switchMap(() => {
      const searchTrigger = this.sharedState.combinationsSearchTrigger();
      
      if (searchTrigger === 0) {
        return of(EXAMPLE_COMBINATIONS);
      }

      const selectedAuthors = this.sharedState.selectedAuthors();
      const authorCustomUsernames = selectedAuthors.map((a) => a.name);

      return this.combinationsService
        .searchCombinations(
          this.sharedState.filters(),
          authorCustomUsernames.length > 0 ? authorCustomUsernames : undefined
        )
        .pipe(
          tap((combinations) => {
            if (combinations.length == 0) {
              this.messageService.showMessage(
                'Ci dispiace, nel sistema non sono state trovate combinazioni con questi parametri.',
                MessageType.warning
              );
            }
          }),
          catchError((err) => {
            this.messageService.showMessage(
              `Errore caricamento combinazioni : ${err.message}`,
              MessageType.error
            );
            return of([] as Combination[]);
          })
        );
    })
  );
  readonly combinationsDisplayed = toSignal(this.combinationsObservable, {
    initialValue: EXAMPLE_COMBINATIONS,
  });

  constructor(config: NgbTooltipConfig) {
    config.container = 'body';
    config.tooltipClass = 'tooltipW';
  }
}
