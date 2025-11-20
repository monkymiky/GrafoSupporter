import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";

@Injectable({
  providedIn: "root",
})
export class FileUploadService {
  private readonly apiUrl = `${environment.apiUrl}/files`;

  constructor(private readonly http: HttpClient) {}

  uploadCombinationImage(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl + "/combination-image", formData);
  }
}
