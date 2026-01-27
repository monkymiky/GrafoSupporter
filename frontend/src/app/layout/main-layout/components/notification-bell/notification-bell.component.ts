import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NotificationService } from '../../../../core/services/notification.service';
import { AuthService } from '../../../../core/services/auth.service';
import { NotificationModalComponent } from '../notification-modal/notification-modal.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification-bell',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-bell.component.html',
  styleUrl: './notification-bell.component.scss',
})
export class NotificationBellComponent implements OnInit, OnDestroy {
  private readonly notificationService = inject(NotificationService);
  private readonly authService = inject(AuthService);
  private readonly modalService = inject(NgbModal);

  unreadCount = 0;
  private countSubscription?: Subscription;

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.loadUnreadCount();
      this.countSubscription = this.notificationService.unreadCount$.subscribe(
        (count) => {
          this.unreadCount = count;
        }
      );
    }
  }

  ngOnDestroy(): void {
    if (this.countSubscription) {
      this.countSubscription.unsubscribe();
    }
  }

  loadUnreadCount(): void {
    this.notificationService.getUnreadCount().subscribe();
  }

  openNotificationsModal(): void {
    const modalRef = this.modalService.open(NotificationModalComponent, {
      size: 'lg',
      centered: true,
    });
    modalRef.result.then(
      () => {
        this.loadUnreadCount();
      },
      () => {
        this.loadUnreadCount();
      }
    );
  }
}
