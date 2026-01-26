import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { LoginModalService } from '../../shared/components/login-modal/services/login-modal.service';
import { LoginModalComponent } from '../../shared/components/login-modal/login-modal.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    LoginModalComponent,
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent {
  readonly authService = inject(AuthService);
  readonly loginModalService = inject(LoginModalService);
  private readonly router = inject(Router);

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
}
