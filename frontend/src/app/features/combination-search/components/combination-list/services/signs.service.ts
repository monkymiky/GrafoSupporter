import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../../../environments/environment";

export interface SignApiResponseItem {
  id: number;
  name: string;
  temperamento: string | null;
  tipo: string;
}

@Injectable({
  providedIn: "root",
})
export class SignsService {
  private readonly apiUrl = `${environment.apiUrl}/signs`;
  private readonly http = inject(HttpClient);

  getSigns(): Observable<SignApiResponseItem[]> {
    return this.http.get<SignApiResponseItem[]>(this.apiUrl);
  }
}
