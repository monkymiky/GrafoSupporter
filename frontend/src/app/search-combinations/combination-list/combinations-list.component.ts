import { Component, inject } from '@angular/core';
import { SingleCombination } from './combination/combination.component';
import { CombinationService } from '../../shared/combinations.service';
import { SignsService } from '../../shared/signs.service';
import { catchError, Observable, of, switchMap, tap } from 'rxjs';
import { Combination } from './combination.interface';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SharedStateService } from '../../shared/shared-state.service';
import {
  NgbAccordionModule,
  NgbTooltipConfig,
  NgbTooltip,
} from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from '../../shared/error-handling/message.service';
import { MessageType } from '../../shared/error-handling/message.interface';

@Component({
  selector: 'app-combination-list',
  imports: [SingleCombination, NgbAccordionModule, NgbTooltip],
  providers: [NgbTooltipConfig],
  templateUrl: './combinations-list.component.html',
  styleUrl: './combinations-list.component.scss',
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
      return this.combinationsService
        .searchCombinations(this.sharedState.filters())
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
    initialValue: [] as Combination[],
  });

  constructor(config: NgbTooltipConfig) {
    config.container = 'body';
    config.tooltipClass = 'tooltipW';
  }
}
