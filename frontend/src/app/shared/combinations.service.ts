import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { Grado } from '../search-combinations/filters/filter.interface';

@Injectable({
  providedIn: 'root',
})
export class CombinationService {
  readonly apiUrl = '/api/combinations';
  readonly http = inject(HttpClient);

  searchCombinations(filters: Map<number, number>): Observable<Combination[]> {
    const filtersObject: { [key: number]: Grado } = {};
    filters.forEach((value, key) => {
      filtersObject[key] = value;
    });

    return this.http.post<Combination[]>(
      this.apiUrl + '/search',
      filtersObject
    );
  }

  createCombination(combination: Combination): Observable<Combination> {
    const { id, ...combinationData } = combination;
    return this.http.post<Combination>(this.apiUrl, combinationData);
  }

  updateCombination(
    id: number,
    combination: Combination
  ): Observable<Combination> {
    return this.http.put<Combination>(`${this.apiUrl}/${id}`, combination);
  }

  deleteCombination(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
