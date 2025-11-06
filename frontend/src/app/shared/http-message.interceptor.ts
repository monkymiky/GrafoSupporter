import { inject, Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { MessageService } from './error-handling/message.service';
import { MessageType } from './error-handling/message.interface';

@Injectable()
export class HttpMessageInterceptor implements HttpInterceptor {
  readonly messageService = inject(MessageService);
  intercept(request: HttpRequest<any>, next: HttpHandler) {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Si è verificato un errore sconosciuto!';
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Client-side error: ${error.error.message}`;
        } else {
          switch (error.status) {
            case 400:
              errorMessage = `Bad Request: ${
                error.error.message ?? 'I dati forniti non sono validi.'
              }`;
              break;
            case 404:
              errorMessage = `Not Found: ${
                error.error.message ??
                'La risorsa richiesta non è stata trovata.'
              }`;
              break;
            case 409:
              errorMessage = `Conflict: ${
                error.error.message ?? 'La risorsa esiste già.'
              }`;
              break;
            case 500:
              errorMessage =
                'Internal Server Error: Si è verificato un errore sul server.';
              break;
          }
        }
        console.error(errorMessage);
        this.messageService.showMessage(errorMessage, MessageType.error);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}

