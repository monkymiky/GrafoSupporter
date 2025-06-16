import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileUploadService {
  private apiUrl = '/api/file-upload';

  constructor(private http: HttpClient) {}

  uploadCombiantionImage(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl + '/combination-image', formData);
  }
}
