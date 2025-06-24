import { computed, inject, Injectable, signal } from '@angular/core';

import { Grado } from '../search-combinations/filters/filter.interface';
import {
  catchError,
  distinctUntilChanged,
  Observable,
  of,
  switchMap,
  tap,
} from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { SignsService } from './signs.service';
import { SignApiResponseItem } from './signs.service';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  errorMessage = signal('');
  noResult = signal('');
  private signsService = inject(SignsService);

  triggerCombinationsSearch = signal(0);

  filters = signal(new Map<number, Grado>());
  signs = signal<SignApiResponseItem[]>([]);

  triggerSignsRequest = signal(0);

  signsMap = computed(() => {
    return new Map(this.signs().map((sign) => [sign.id, sign]));
  });
  signsTypes = computed(() => {
    const result: string[] = [];
    for (const singleSing of this.signs()) {
      if (!result.includes(singleSing.tipo)) {
        result.push(singleSing.tipo);
      }
    }
    return result;
  });
}
