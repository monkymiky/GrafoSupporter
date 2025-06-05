import { inject, Injectable, signal } from '@angular/core';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { CombinationService } from './combinations.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  combiantionsService = inject(CombinationService);
  errorMessage = signal('');
  triggerCombinationSearch = signal(0);

  filters = signal({
    '1': 0,
    // attesa
    '2': 0,
    '3': 0,
    '4': 0,
    '5': 0,
    '6': 0,
    '7': 0,
    '8': 0,
    '9': 0,
    '10': 0,
    // resistenza
    '11': 0,
    '12': 0,
    '13': 0,
    '14': 0,
    '15': 0,
    '16': 0,
    '17': 0,
    // assalto
    '18': 0,
    '19': 0,
    '20': 0,
    '21': 0,
    '22': 0,
    '23': 0,
    '24': 0,
    '25': 0,
    '26': 0,
    '27': 0,
    '28': 0,
    '29': 0,
    '30': 0,
    // Attesa
    '31': 0,
    '32': 0,
    '33': 0,
    '34': 0,
    '35': 0,
    '36': 0,
    '37': 0,
    '38': 0,
    '39': 0,
    '40': 0,
    '41': 0,
    '42': 0,
    '43': 0,
    '44': 0,
    '45': 0,
    '46': 0,
    '47': 0,
    '48': 0,
    '49': 0,
    '50': 0,
    '51': 0,
    '52': 0,
    '53': 0,
    '54': 0,
    '55': 0,
    '56': 0,
    '57': 0,
  });

  private combinationsObservable: Observable<Combination[]> = toObservable(
    this.triggerCombinationSearch
  ).pipe(
    startWith([]),
    switchMap(() => {
      return this.combiantionsService.searchCombinations(this.filters()).pipe(
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
