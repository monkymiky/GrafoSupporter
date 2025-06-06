import { Component, inject, signal } from '@angular/core';
import { SingleCombination } from './combination/single-combination.component';
import { CombinationService } from '../../shared/combinations.service';
import { SignsService } from '../../shared/signs.service';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { Combination } from './combination.interface';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SharedStateService } from '../../shared/shared-state.service';

@Component({
  selector: 'app-combination-list',
  imports: [SingleCombination],
  templateUrl: './combination-list.component.html',
  styleUrl: './combination-list.component.css',
})
export class CombinationListComponent {
  combiantionsService = inject(CombinationService);
  sharedState = inject(SharedStateService);
  signsService = inject(SignsService);
  errorMessage = signal('');

  private combinationsObservable: Observable<Combination[]> = toObservable(
    this.sharedState.filters
  ).pipe(
    switchMap(() => {
      console.log('signal ricevuto' + this.sharedState.filters);
      return this.combiantionsService
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
