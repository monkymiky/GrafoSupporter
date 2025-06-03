import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Combination } from '../search-combinations/combination/combination.interface';
import { Sign_filter } from '../search-combinations/filters/sign-filter.interface';
import { SharedStateService } from './shared-state.service';

@Injectable({
  providedIn: 'root',
})
export class CombiantionService {
  private apiUrl = 'http://localhost:8080/combinations';
  private http = inject(HttpClient);
  private sharedState = inject(SharedStateService);

  searchCombinations(): Observable<Combination[]> {
    console.log(this.sharedState.getfilters()());
    return this.http.post<Combination[]>(
      this.apiUrl,
      this.sharedState.getfilters()()
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
