import { inject, Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private readonly authService = inject(AuthService);

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Skip token only for public auth endpoints (OAuth2, validate)
    // But include token for protected endpoints like /me, /profile/username
    const publicAuthEndpoints = ['/oauth2/', '/validate'];
    const isPublicAuthEndpoint = publicAuthEndpoints.some(endpoint => 
      request.url.includes(endpoint)
    );

    if (!isPublicAuthEndpoint && token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request);
  }
}

