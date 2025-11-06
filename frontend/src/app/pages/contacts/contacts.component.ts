import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ContactService,
  ContactFeedback,
} from '../../shared/services/contact.service';
import { MessageService } from '../../shared/error-handling/message.service';
import { MessageType } from '../../shared/error-handling/message.interface';

@Component({
  selector: 'app-contacts',
  imports: [CommonModule, FormsModule],
  templateUrl: './contacts.component.html',
  styleUrl: './contacts.component.scss',
})
export class ContactsComponent implements OnInit, OnDestroy {
  private readonly contactService = inject(ContactService);
  private readonly messageService = inject(MessageService);

  feedback: ContactFeedback = {
    name: '',
    email: '',
    category: 'question',
    subject: '',
    message: '',
  };

  isSubmitting = false;

  readonly categories = [
    {
      value: 'bug' as const,
      label: 'Ho trovato un problema',
      icon: 'bi-bug',
      description: 'Qualcosa non funziona come dovrebbe',
      example: 'Esempio: "Quando cerco una combinazione, la pagina si blocca"',
    },
    {
      value: 'suggestion' as const,
      label: 'Ho un suggerimento',
      icon: 'bi-lightbulb',
      description: "Un'idea per migliorare l'applicazione",
      example:
        'Esempio: "Sarebbe utile poter salvare le combinazioni preferite"',
    },
    {
      value: 'question' as const,
      label: 'Ho una domanda',
      icon: 'bi-question-circle',
      description: 'Vorrei sapere qualcosa di più',
      example: 'Esempio: "Come funziona la ricerca delle combinazioni?"',
    },
    {
      value: 'other' as const,
      label: 'Altro',
      icon: 'bi-chat-dots',
      description: 'Qualsiasi altra cosa tu voglia dirci',
      example: 'Esempio: "Vorrei ringraziarvi per questo strumento!"',
    },
  ];

  get selectedCategory() {
    return this.categories.find((c) => c.value === this.feedback.category);
  }

  ngOnInit(): void {
    // Abilita lo scrolling quando si entra nella pagina contatti
    document.body.classList.add('active-scrolling');
    document.documentElement.classList.add('active-scrolling');
  }

  ngOnDestroy(): void {
    // Disabilita lo scrolling quando si esce dalla pagina contatti
    document.body.classList.remove('active-scrolling');
    document.documentElement.classList.remove('active-scrolling');
  }

  onSubmit(): void {
    if (!this.isFormValid()) {
      this.messageService.showMessage(
        'Per favore, compila tutti i campi obbligatori',
        MessageType.warning
      );
      return;
    }

    this.isSubmitting = true;

    // Aggiungi informazioni utili per il debug
    this.feedback.userAgent = navigator.userAgent;
    this.feedback.pageUrl = window.location.href;

    this.contactService.sendFeedback(this.feedback).subscribe({
      next: () => {
        this.messageService.showMessage(
          'Grazie! Il tuo messaggio è stato inviato con successo. Ti risponderemo presto.',
          MessageType.success
        );
        this.resetForm();
        this.isSubmitting = false;
      },
      error: () => {
        this.messageService.showMessage(
          "Si è verificato un errore durante l'invio del messaggio. Riprova più tardi.",
          MessageType.error
        );
        this.isSubmitting = false;
      },
    });
  }

  private isFormValid(): boolean {
    return !!(
      this.feedback.name?.trim() &&
      this.feedback.email?.trim() &&
      this.feedback.subject?.trim() &&
      this.feedback.message?.trim()
    );
  }

  private resetForm(): void {
    this.feedback = {
      name: '',
      email: '',
      category: 'question',
      subject: '',
      message: '',
    };
  }
}
