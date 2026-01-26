import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Author } from "../model/author.interface";
import { environment } from "../../../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class AuthorsService {
  readonly apiUrl = `${environment.apiUrl}/authors`;
  readonly http = inject(HttpClient);

  searchAuthors(prefix: string): Observable<Author[]> {
    if (!prefix || prefix.trim().length < 2) {
      return new Observable((subscriber) => {
        subscriber.next([]);
        subscriber.complete();
      });
    }

    const params = new HttpParams().set("prefix", prefix.trim());
    return this.http.get<Author[]>(`${this.apiUrl}/search`, { params });
  }
}
