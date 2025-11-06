import { computed, Injectable, signal } from '@angular/core';
import { Grado } from '../search-combinations/filters/filter.interface';
import { SignApiResponseItem } from './signs.service';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  combinationsSearchTrigger = signal(0);

  filters = signal(new Map<number, Grado>());
  signs = signal<SignApiResponseItem[]>([]);

  signsRequestTrigger = signal(0);

  signsMap = computed(() => {
    return new Map(this.signs().map((sign) => [sign.id, sign]));
  });
  signTypes = computed(() => {
    const result: string[] = [];
    for (const singleSign of this.signs()) {
      if (!result.includes(singleSign.tipo)) {
        result.push(singleSign.tipo);
      }
    }
    return result;
  });
}
