import { ErrorHandler, inject, Injectable, NgZone } from '@angular/core';
import { MessageService } from '../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../shared/components/message-toast/models/message.interface';

@Injectable({
  providedIn: 'root',
})
export class GlobalMessageHandler implements ErrorHandler {
  private readonly messageService = inject(MessageService);
  private readonly zone = inject(NgZone);

  handleError(error: any): void {
    console.error('GlobalMessageHandler ha intercettato un errore:', error);

    const message = error.message ?? 'Si è verificato un errore inaspettato.';

    this.zone.run(() => {
      this.messageService.showMessage(message, MessageType.error);
    });
  }
}
