import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { Filter } from '../search-combinations/filters/filter.interface';

@Injectable({
  providedIn: 'root',
})
export class CombinationService {
  private apiUrl = 'http://localhost:8080/combinations';
  private http = inject(HttpClient);

  searchCombinations(filters: Filter): Observable<Combination[]> {
    console.log(this.apiUrl + '/withoutNumbers');
    return this.http.post<Combination[]>(
      this.apiUrl + '/withoutNumbers',
      filters
    );
  }

  createProduct(combination: Combination): Observable<Combination> {
    const { id, ...combinationData } = combination;
    return this.http.post<Combination>(this.apiUrl, combinationData);
  }

  updateProduct(id: number, combination: Combination): Observable<Combination> {
    return this.http.put<Combination>(`${this.apiUrl}/${id}`, combination);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
