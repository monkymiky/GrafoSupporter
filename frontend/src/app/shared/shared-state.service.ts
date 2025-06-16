import { inject, Injectable, signal } from '@angular/core';

import { Grado } from '../search-combinations/filters/filter.interface';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SignsService } from './signs.service';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  errorMessage = signal('');
  private signsService = inject(SignsService);

  private initializeFiltersMap(): Map<number, Grado> {
    const map = new Map<number, Grado>();
    for (let i = 1; i <= 57; i++) {
      map.set(i, Grado.ASSENTE);
    }
    return map;
  }
  triggerCombinationsSearch = signal(0);
  filters = signal<Map<number, Grado>>(this.initializeFiltersMap());

  triggerSignsRequest = signal(0);
  private signsObservable: Observable<[number, [string, string]][]> =
    toObservable(this.triggerSignsRequest).pipe(
      startWith([]),
      switchMap(() => {
        return this.signsService.getSigns().pipe(
          tap(() => this.errorMessage.set('')),
          catchError((err) => {
            this.errorMessage.set(`Errore caricamento segni : ${err.message}`);
            return of([] as [number, [string, string]][]);
          })
        );
      })
    );
  signs = toSignal(this.signsObservable, {
    initialValue: [] as [number, [string, string]][],
  });
}
