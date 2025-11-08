import { Injectable, signal } from '@angular/core';

export type LoginModalMode = 'login' | 'signup';

@Injectable({ providedIn: 'root' })
export class LoginModalService {
  readonly isOpen = signal<boolean>(false);
  readonly mode = signal<LoginModalMode>('login');

  open(mode: LoginModalMode = 'login'): void {
    this.mode.set(mode);
    this.isOpen.set(true);
  }

  close(): void {
    this.isOpen.set(false);
  }
}


