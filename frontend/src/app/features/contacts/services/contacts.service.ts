import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ContactFeedback {
  name: string;
  email: string;
  category: 'bug' | 'suggestion' | 'question' | 'other';
  subject: string;
  message: string;
  userAgent?: string;
  pageUrl?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ContactService {
  readonly apiUrl = '/api/contact';
  readonly http = inject(HttpClient);

  sendFeedback(feedback: ContactFeedback): Observable<void> {
    return this.http.post<void>(this.apiUrl, feedback);
  }
}


