import { computed, inject, Injectable, signal } from '@angular/core';

import { Grado } from '../search-combinations/filters/filter.interface';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SignsService } from './signs.service';
import { SignApiResponseItem } from './signs.service';

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
  filtersInitializer = computed<Map<number, Grado>>(() => {
    const map = new Map<number, Grado>();

    for (const sign of this.signs()) {
      map.set(sign.id, Grado.ASSENTE);
    }
    this.filters.set(map);
    return map;
  });
  filters = signal(new Map<number, Grado>());

  triggerSignsRequest = signal(0);
  private signsObservable: Observable<SignApiResponseItem[]> = toObservable(
    this.triggerSignsRequest
  ).pipe(
    startWith([]),
    switchMap(() => {
      return this.signsService.getSigns().pipe(
        tap(() => this.errorMessage.set('')),
        catchError((err) => {
          this.errorMessage.set(`Errore caricamento segni : ${err.message}`);
          return of([] as SignApiResponseItem[]);
        })
      );
    })
  );
  signs = toSignal(this.signsObservable, {
    initialValue: [] as SignApiResponseItem[],
  });
  signsMap = computed(() => {
    return new Map(this.signs().map((sign) => [sign.id, sign]));
  });
}
