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
  private apiUrl = 'http://localhost:8080/signs';
  private http = inject(HttpClient);

  getSigns(): Observable<Map<number, [string, string]>> {
    return this.http.get<SignApiResponseItem[]>(this.apiUrl).pipe(
      map((responseArray) => {
        const newMap = new Map<number, [string, string]>();
        if (responseArray && Array.isArray(responseArray)) {
          // Controllo di sicurezza
          for (const item of responseArray) {
            // Gestisci il caso in cui temperamento è null.
            // Decidi cosa vuoi mettere nella mappa: una stringa vuota, "N/A", o mantenere null
            // e adattare il componente. Per ora, uso una stringa vuota se null.
            const temperamentoValue =
              item.temperamento === null ? '' : item.temperamento;
            newMap.set(item.id, [item.name, temperamentoValue]);
          }
        }
        return newMap;
      })
    );
  }
}
