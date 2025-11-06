import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, map, catchError, of } from 'rxjs';
import { Router } from '@angular/router';

export interface AuthUser {
  token: string;
  email: string;
  name: string;
  pictureUrl: string;
  userId: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly apiUrl = '/api/auth';

  private readonly currentUserSubject = new BehaviorSubject<AuthUser | null>(
    this.getStoredUser()
  );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    const storedUser = this.getStoredUser();
    if (storedUser) {
      this.validateToken(storedUser.token).subscribe({
        next: (isValid) => {
          if (!isValid) {
            this.logout();
          }
        },
        error: () => this.logout(),
      });
    }
  }

  get currentUserValue(): AuthUser | null {
    return this.currentUserSubject.value;
  }

  get isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }

  getToken(): string | null {
    return this.currentUserSubject.value?.token || null;
  }

  loginWithGoogle(): void {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  handleOAuthCallback(): Observable<AuthUser> {
    return this.http.get<AuthUser>(`${this.apiUrl}/oauth2/success`).pipe(
      tap((user) => {
        this.setUser(user);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  getCurrentUser(): Observable<Omit<AuthUser, 'token'>> {
    return this.http.get<Omit<AuthUser, 'token'>>(`${this.apiUrl}/me`);
  }

  private validateToken(token: string): Observable<boolean> {
    return this.http
      .post<{ valid: boolean }>(
        `${this.apiUrl}/validate`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      )
      .pipe(
        map((response) => {
          if (!response.valid) {
            this.logout();
          }
          return response.valid;
        }),
        catchError(() => {
          this.logout();
          return of(false);
        })
      );
  }

  setUser(user: AuthUser): void {
    localStorage.setItem('auth_user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getStoredUser(): AuthUser | null {
    const stored = localStorage.getItem('auth_user');
    if (!stored) return null;
    try {
      return JSON.parse(stored);
    } catch {
      return null;
    }
  }
}
