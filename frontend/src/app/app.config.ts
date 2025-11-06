import {
  ApplicationConfig,
  provideZoneChangeDetection,
  importProvidersFrom,
  ErrorHandler,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';

import { registerLocaleData } from '@angular/common';
import it from '@angular/common/locales/it';
import { FormsModule } from '@angular/forms';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { HttpMessageInterceptor } from './shared/http-message.interceptor';
import { GlobalMessageHandler } from './shared/error-handling/global-message-handler';
import { JwtInterceptor } from './shared/interceptors/jwt.interceptor';

registerLocaleData(it);

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom(FormsModule),
    provideAnimationsAsync(),
    provideHttpClient(),
    provideHttpClient(withInterceptorsFromDi()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpMessageInterceptor,
      multi: true,
    },
    { provide: ErrorHandler, useClass: GlobalMessageHandler },
  ],
};
