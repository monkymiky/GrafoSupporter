import { Injectable, signal } from '@angular/core';
import { Message, MessageType } from './message.interface';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private readonly messagesState = signal<Message[]>([]);

  public readonly messages = this.messagesState.asReadonly();

  private messageIdCounter = 0;

  showMessage(text: string, type: MessageType): void {
    const newMessage: Message = {
      id: this.messageIdCounter++,
      text,
      type,
    };

    this.messagesState.update((currentMessages) => [
      newMessage,
      ...currentMessages,
    ]);
    let delay: number = 0;
    switch (newMessage.type) {
      case 0:
        delay = 15000;
        break;
      case 1:
        delay = 10000;
        break;
      case 2:
        delay = 10000;
        break;
      case 3:
        delay = 5000;
        break;
      default:
        delay = 10000;
    }
    setTimeout(() => this.removeMessage(newMessage.id), delay);
  }

  removeMessage(messageId: number): void {
    this.messagesState.update((currentMessages) =>
      currentMessages.filter((msg) => msg.id !== messageId)
    );
  }
}
