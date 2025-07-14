import { Component, inject } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { trigger, transition, style, animate } from '@angular/animations';
import { MessageService } from '../message.service';
import { Message, MessageType } from '../message.interface';

@Component({
  selector: 'app-message-toast',
  standalone: true,
  imports: [CommonModule, NgClass],
  templateUrl: './message-toast.component.html',
  styleUrls: ['./message-toast.component.scss'],
  animations: [
    trigger('toastAnimation', [
      transition(':enter', [
        style({ transform: 'translateX(100%)', opacity: 0 }),
        animate(
          '300ms ease-out',
          style({ transform: 'translateX(0)', opacity: 1 })
        ),
      ]),
      transition(':leave', [
        animate(
          '300ms ease-in',
          style({ transform: 'translateX(100%)', opacity: 0 })
        ),
      ]),
    ]),
  ],
})
export class MessageToastComponent {
  private readonly messageService = inject(MessageService);
  public readonly messages = this.messageService.messages;

  close(messageId: number): void {
    this.messageService.removeMessage(messageId);
  }

  getCssClass(type: MessageType): string {
    switch (type) {
      case MessageType.error:
        return 'toast-error';
      case MessageType.warning:
        return 'toast-warning';
      case MessageType.info:
        return 'toast-info';
      case MessageType.success:
        return 'toast-success';
      default:
        return 'toast-info';
    }
  }

  trackById(index: number, item: Message): number {
    return item.id;
  }

  // ngOnInit() {
  //   this.messageService.showMessage(
  //     "Ci dispiace qualcosa è andato storto! L'inserimento dell'immagine non è andato a buon fine, riprova tra qualche minuto.",
  //     0
  //   );
  //   this.messageService.showMessage(
  //     'Attenzione! selezionare almeno due segni per effettuare una ricerca.',
  //     1
  //   );
  //   this.messageService.showMessage(
  //     "La richiesta è stata ricevuta, l'elaborazione richiederà qualche minuto.",
  //     2
  //   );
  //   this.messageService.showMessage('Combinazione modificata con successo!', 3);
  // }
}
