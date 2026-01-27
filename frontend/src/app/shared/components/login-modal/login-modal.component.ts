import { CommonModule } from '@angular/common';
import { Component, HostListener, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginModalService } from './services/login-modal.service';

@Component({
  selector: 'app-login-modal',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.scss'],
})
export class LoginModalComponent {
  readonly authService = inject(AuthService);
  readonly loginModalService = inject(LoginModalService);

  close(): void {
    this.loginModalService.close();
  }

  switchMode(mode: 'login' | 'signup'): void {
    this.loginModalService.open(mode);
  }

  loginWithGoogle(): void {
    this.authService.loginWithGoogle();
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.loginModalService.isOpen()) this.close();
  }
}
