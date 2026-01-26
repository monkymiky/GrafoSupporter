import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable, tap, map, catchError, of } from "rxjs";
import { Router } from "@angular/router";
import { environment } from "../../../environments/environment";

export interface AuthUser {
  token: string;
  email: string;
  name: string;
  pictureUrl: string;
  customUsername?: string;
  userId: number;
}

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly authApiUrl = `${environment.apiUrl}${environment.apiPrefix}/auth`;

  private readonly currentUserSubject = new BehaviorSubject<AuthUser | null>(
    this.getStoredUser()
  );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    const storedUser = this.getStoredUser();
    if (storedUser) {
      setTimeout(() => {
        this.validateToken(storedUser.token).subscribe({
          next: (isValid) => {
            if (!isValid) {
              this.logout();
            }
          },
          error: () => this.logout(),
        });
      }, 0);
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
    window.location.href = `${environment.apiUrl}/oauth2/authorization/google`;
  }

  handleOAuthCallback(): Observable<AuthUser> {
    return this.http.get<AuthUser>(`${this.authApiUrl}/oauth2/success`).pipe(
      tap((user) => {
        this.setUser(user);
      })
    );
  }

  logout(): void {
    localStorage.removeItem("auth_user");
    this.currentUserSubject.next(null);
    this.router.navigate(["/"]);
  }

  getCurrentUser(): Observable<Omit<AuthUser, "token">> {
    return this.http.get<Omit<AuthUser, "token">>(`${this.authApiUrl}/me`);
  }

  private validateToken(token: string): Observable<boolean> {
    return this.http
      .post<{ valid: boolean }>(
        `${this.authApiUrl}/validate`,
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
    localStorage.setItem("auth_user", JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getStoredUser(): AuthUser | null {
    const stored = localStorage.getItem("auth_user");
    if (!stored) return null;
    try {
      return JSON.parse(stored);
    } catch {
      return null;
    }
  }
}
