import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SettingsService {
  private readonly apiUrl = `${environment.apiUrl}${environment.apiPrefix}/auth`;
  private readonly http = inject(HttpClient);

  updateCustomUsername(customUsername: string | null): Observable<{ message: string; customUsername: string | null }> {
    return this.http.put<{ message: string; customUsername: string | null }>(
      `${this.apiUrl}/profile/username`,
      { customUsername }
    );
  }
}
