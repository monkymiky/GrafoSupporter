import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../shared/services/auth.service';
import { LoginModalService } from '../../shared/services/login-modal.service';
import { LoginModalComponent } from '../../login-modal/login-modal.component';

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

  openLogin() {
    this.loginModalService.open('login');
  }
  openSignup() {
    this.loginModalService.open('signup');
  }

  logout() {
    this.authService.logout();
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.style.display = 'none';
  }
}
