import { Component, inject, signal } from '@angular/core';
import { SingleCombination } from './combination/combination.component';
import { CombinationService } from '../../shared/combinations.service';
import { SignsService } from '../../shared/signs.service';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { Combination } from './combination.interface';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SharedStateService } from '../../shared/shared-state.service';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-combination-list',
  imports: [SingleCombination, NgbAccordionModule],
  templateUrl: './combinations-list.component.html',
  styleUrl: './combinations-list.component.css',
})
export class CombinationListComponent {
  combinationsService = inject(CombinationService);
  sharedState = inject(SharedStateService);
  signsService = inject(SignsService);
  errorMessage = signal('');

  private combinationsObservable: Observable<Combination[]> = toObservable(
    this.sharedState.triggerCombinationsSearch
  ).pipe(
    switchMap(() => {
      console.log('signal ricevuto' + this.sharedState.filters);
      return this.combinationsService
        .searchCombinations(this.sharedState.filters())
        .pipe(
          tap(() => this.errorMessage.set('')),
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
}
