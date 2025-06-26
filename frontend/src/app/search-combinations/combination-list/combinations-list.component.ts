import { Component, inject, signal } from '@angular/core';
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
  signsService = inject(SignsService);
  errorMessage = signal('');

  readonly combinationsObservable: Observable<Combination[]> = toObservable(
    this.sharedState.triggerCombinationsSearch
  ).pipe(
    switchMap(() => {
      return this.combinationsService
        .searchCombinations(this.sharedState.filters())
        .pipe(
          tap((combinations) => {
            this.errorMessage.set('');
            this.sharedState.noResult.set('');
            if (combinations.length == 0) {
              this.sharedState.noResult.set(
                'Ci dispiace, nel sistema non sono state trovate combinazioni con questi parametri.'
              );
            }
          }),
          catchError((err) => {
            this.errorMessage.set(
              `Errore caricamento combinazioni : ${err.message}`
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
