import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbActiveModal, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Notification, NotificationType } from '../../../../core/models/notification.interface';
import { NotificationService } from '../../../../core/services/notification.service';
import { MessageService } from '../../../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../../../shared/components/message-toast/models/message.interface';

@Component({
  selector: 'app-notification-modal',
  standalone: true,
  imports: [CommonModule, NgbModalModule],
  templateUrl: './notification-modal.component.html',
  styleUrl: './notification-modal.component.scss',
})
export class NotificationModalComponent implements OnInit {
  private readonly notificationService = inject(NotificationService);
  private readonly router = inject(Router);
  private readonly messageService = inject(MessageService);
  readonly activeModal = inject(NgbActiveModal);

  notifications: Notification[] = [];
  isLoading = false;

  readonly NotificationType = NotificationType;

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.isLoading = true;
    this.notificationService.getUnreadNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
        this.isLoading = false;
      },
      error: (err) => {
        this.messageService.showMessage(
          `Errore nel caricamento delle notifiche: ${err.error?.message || err.message}`,
          MessageType.error
        );
        this.isLoading = false;
      },
    });
  }

  handleNotificationClick(notification: Notification): void {
    if (notification.read) return;

    this.notificationService.markAsRead(notification.id).subscribe({
      next: () => {
        notification.read = true;
        this.activeModal.close();

        if (notification.combination) {
          this.router.navigate(['/ricerca-combinazioni']).then(() => {
            setTimeout(() => {
              const element = document.querySelector(
                `[data-combination-id="${notification.combination!.id}"]`
              );
              if (element) {
                element.scrollIntoView({ behavior: 'smooth', block: 'center' });
                if (notification.comment) {
                  setTimeout(() => {
                    const commentElement = document.querySelector(
                      `[data-comment-id="${notification.comment!.id}"]`
                    );
                    if (commentElement) {
                      commentElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                  }, 300);
                }
              }
            }, 100);
          });
        }
      },
      error: (err) => {
        this.messageService.showMessage(
          `Errore: ${err.error?.message || err.message}`,
          MessageType.error
        );
      },
    });
  }

  getNotificationText(notification: Notification): string {
    switch (notification.type) {
      case NotificationType.COMMENT_ON_COMBINATION:
        return `${notification.sender.name} ha commentato la tua combinazione "${notification.combination?.title || ''}"`;
      case NotificationType.REPLY_TO_COMMENT:
        return `${notification.sender.name} ha risposto al tuo commento`;
      default:
        return 'Nuova notifica';
    }
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.style.display = 'none';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'adesso';
    if (diffMins < 60) return `${diffMins} min fa`;
    if (diffHours < 24) return `${diffHours} ore fa`;
    if (diffDays < 7) return `${diffDays} giorni fa`;
    return date.toLocaleDateString('it-IT');
  }
}
