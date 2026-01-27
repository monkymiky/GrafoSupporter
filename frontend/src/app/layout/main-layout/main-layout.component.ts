import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { LoginModalService } from '../../shared/components/login-modal/services/login-modal.service';
import { LoginModalComponent } from '../../shared/components/login-modal/login-modal.component';
import { NotificationBellComponent } from './components/notification-bell/notification-bell.component';
import { NotificationService } from '../../core/services/notification.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    LoginModalComponent,
    NotificationBellComponent,
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  readonly authService = inject(AuthService);
  readonly loginModalService = inject(LoginModalService);
  private readonly router = inject(Router);
  private readonly notificationService = inject(NotificationService);

  openLogin() {
    this.loginModalService.open('login');
  }
  openSignup() {
    this.loginModalService.open('signup');
  }

  logout() {
    this.authService.logout();
  }

  navigateToSettings() {
    this.router.navigate(['/impostazioni']);
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.style.display = 'none';
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.notificationService.startPolling();
      this.notificationService.getUnreadCount().subscribe();
    }
  }

  ngOnDestroy(): void {
    this.notificationService.stopPolling();
  }
}
