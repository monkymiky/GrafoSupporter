import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

export interface SignApiResponseItem {
  id: number;
  name: string;
  temperamento: string | null;
  tipo: string;
}

@Injectable({
  providedIn: 'root',
})
export class SignsService {
  private apiUrl = '/api/signs';
  private http = inject(HttpClient);

  getSigns(): Observable<SignApiResponseItem[]> {
    return this.http.get<SignApiResponseItem[]>(this.apiUrl);
  }
}
