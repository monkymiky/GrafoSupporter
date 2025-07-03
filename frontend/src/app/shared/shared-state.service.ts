import { computed, Injectable, signal, WritableSignal } from '@angular/core';
import { Grado } from '../search-combinations/filters/filter.interface';
import { SignApiResponseItem } from './signs.service';

export enum MessageType {
  'empty' = 0,
  'error' = 1,
  'warning' = 2,
  'success' = 3,
  'info' = 4,
}

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
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
