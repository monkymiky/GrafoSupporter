import { Injectable, signal } from '@angular/core';

import { Grado } from '../search-combinations/filters/filter.interface';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  errorMessage = signal('');

  private initializeFiltersMap(): Map<number, Grado> {
    const map = new Map<number, Grado>();
    for (let i = 1; i <= 57; i++) {
      map.set(i, Grado.ASSENTE);
    }
    return map;
  }

  filters = signal<Map<number, Grado>>(this.initializeFiltersMap());
}
