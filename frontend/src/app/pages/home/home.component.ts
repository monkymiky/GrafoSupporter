import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { LoginModalService } from '../../shared/services/login-modal.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  readonly router = inject(Router);
  readonly route = inject(ActivatedRoute);
  readonly authService = inject(AuthService);
  readonly loginModalService = inject(LoginModalService);

  isHovering = false;
  goDownNow = false;
  imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

  play() {
    this.isHovering = true;
  }
  stop() {
    this.isHovering = false;
  }

  image: string = 'assets/combination-image/scrittura-home3.png';

  panelAngle = 0;

  ngOnInit(): void {
    this.panelAngle = 360 / this.imageIndex.length;

    this.route.queryParams.subscribe((params) => {
      if (params['token']) {
        const authUser = {
          token: params['token'],
          email: params['email'] || '',
          name: params['name'] || '',
          pictureUrl: params['pictureUrl'] || '',
          userId: Number.parseInt(params['userId'] || '0', 10),
        };
        console.debug('Auth user from query params:', authUser);
        setTimeout(() => {
          // setTimeout is necessary to force the calling of these operations to be in the next angular call-back cicle to fix the error https://angular.dev/errors/NG0100
          this.authService.setUser(authUser);
          this.router.navigate(['/ricerca-combinazioni'], { replaceUrl: true });
        }, 0);
      }

      if (params['error']) {
        console.error('Authentication error:', params['error']);
      }

      if (params['login'] === 'true') {
        // User needs to login to access ricerca-combinazioni
        // Login button is already visible in navbar
      }
    });
  }

  openLogin(): void {
    this.loginModalService.open('login');
  }
  openSignup(): void {
    this.loginModalService.open('signup');
  }
}
