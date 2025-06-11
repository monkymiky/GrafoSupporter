import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { Grado } from '../search-combinations/filters/filter.interface';

@Injectable({
  providedIn: 'root',
})
export class CombinationService {
  private apiUrl = '/api/combinations';
  private http = inject(HttpClient);

  searchCombinations(filters: Map<number, number>): Observable<Combination[]> {
    console.log(this.apiUrl + '/withoutNumbers');

    const filtersObject: { [key: number]: Grado } = {};
    filters.forEach((value, key) => {
      filtersObject[key] = value;
    });

    return this.http.post<Combination[]>(
      this.apiUrl + '/withoutNumbers',
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
