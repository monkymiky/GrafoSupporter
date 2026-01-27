import { Injectable, inject, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, interval, Subscription } from 'rxjs';
import { switchMap, catchError, tap } from 'rxjs/operators';
import { Notification } from '../models/notification.interface';
import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class NotificationService implements OnDestroy {
  readonly apiUrl = `${environment.apiUrl}/notifications`;
  readonly http = inject(HttpClient);
  readonly authService = inject(AuthService);

  private readonly unreadCountSubject = new BehaviorSubject<number>(0);
  public readonly unreadCount$ = this.unreadCountSubject.asObservable();

  private pollingSubscription?: Subscription;
  private readonly POLLING_INTERVAL = 30000; // 30 secondi

  ngOnDestroy(): void {
    this.stopPolling();
  }

  getUnreadNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.apiUrl);
  }

  getUnreadCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/unread-count`).pipe(
      tap((count) => this.unreadCountSubject.next(count)),
      catchError(() => {
        this.unreadCountSubject.next(0);
        return [0];
      })
    );
  }

  markAsRead(notificationId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${notificationId}/read`, {}).pipe(
      tap(() => {
        this.getUnreadCount().subscribe();
      })
    );
  }

  startPolling(): void {
    if (this.pollingSubscription) {
      return;
    }

    this.pollingSubscription = interval(this.POLLING_INTERVAL)
      .pipe(
        switchMap(() => {
          if (this.authService.isAuthenticated) {
            return this.getUnreadCount();
          }
          return [];
        }),
        catchError(() => [])
      )
      .subscribe();
  }

  stopPolling(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
      this.pollingSubscription = undefined;
    }
  }
}
