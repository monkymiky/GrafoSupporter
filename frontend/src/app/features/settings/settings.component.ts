import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { SettingsService } from './services/settings.service';
import { MessageService } from '../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../shared/components/message-toast/models/message.interface';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent implements OnInit, OnDestroy {
  readonly authService = inject(AuthService);
  private readonly settingsService = inject(SettingsService);
  private readonly messageService = inject(MessageService);
  private readonly router = inject(Router);

  customUsername: string = '';
  isSubmitting = false;

  ngOnInit(): void {
    // Abilita lo scrolling quando si entra nella pagina impostazioni
    document.body.classList.add('active-scrolling');
    document.documentElement.classList.add('active-scrolling');

    // Load updated user data from server
    this.authService.getCurrentUser().subscribe({
      next: (userData) => {
        const currentUser = this.authService.currentUserValue;
        if (currentUser) {
          const updatedUser = {
            ...currentUser,
            ...userData,
            token: currentUser.token,
          };
          this.authService.setUser(updatedUser);
          this.loadUserData(updatedUser);
        }
      },
      error: () => {
        // If /me fails, use cached data
        const currentUser = this.authService.currentUserValue;
        if (currentUser) {
          this.loadUserData(currentUser);
        }
      },
    });
  }

  private loadUserData(user: any): void {
    this.customUsername = user.customUsername || '';
  }

  ngOnDestroy(): void {
    // Disabilita lo scrolling quando si esce dalla pagina impostazioni
    document.body.classList.remove('active-scrolling');
    document.documentElement.classList.remove('active-scrolling');
  }

  onSubmit(): void {
    if (this.isSubmitting) return;

    if (this.customUsername === (this.authService.currentUserValue?.customUsername || '')) {
      return;
    }

    this.isSubmitting = true;

    this.settingsService.updateCustomUsername(this.customUsername || null).subscribe({
      next: (response) => {
        const currentUser = this.authService.currentUserValue;
        if (currentUser) {
          currentUser.customUsername = response.customUsername || undefined;
          this.authService.setUser(currentUser);
        }
        this.messageService.showMessage(
          'Impostazioni aggiornate con successo',
          MessageType.success
        );
        this.isSubmitting = false;
      },
      error: () => {
        this.messageService.showMessage(
          "Si è verificato un errore durante l'aggiornamento delle impostazioni",
          MessageType.error
        );
        this.isSubmitting = false;
      },
    });
  }
}
