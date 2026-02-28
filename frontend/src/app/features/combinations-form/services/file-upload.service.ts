import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileUploadService {
  private apiUrl = '/api/files';

  constructor(private http: HttpClient) {}

  uploadCombinationImage(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl + '/combination-image', formData);
  }
}
