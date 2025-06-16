import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

interface SignApiResponseItem {
  id: number;
  name: string;
  temperamento: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class SignsService {
  private apiUrl = '/api/signs';
  private http = inject(HttpClient);

  getSigns(): Observable<[number, [string, string]][]> {
    return this.http.get<SignApiResponseItem[]>(this.apiUrl).pipe(
      map((responseArray) => {
        const signs = [] as [number, [string, string]][];
        if (responseArray && Array.isArray(responseArray)) {
          for (const item of responseArray) {
            const temperamentoValue =
              item.temperamento === null ? '' : item.temperamento;
            signs.push([item.id, [item.name, temperamentoValue]]);
          }
        }
        return signs;
      })
    );
  }
}
