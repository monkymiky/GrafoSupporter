import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { FiltersComponent } from './components/filters/filters.component';
import { CombinationListComponent } from './components/combination-list/combination-list.component';
import { NgbModal, NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { CombinationsFormComponent } from '../combinations-form/combination-form.component';
import { SharedStateService } from '../../shared/services/shared-state.service';
import { AuthService } from '../../core/services/auth.service';
import { LoginModalService } from '../../shared/components/login-modal/services/login-modal.service';

@Component({
  selector: 'app-search-combinations-page',
  standalone: true,
  imports: [FiltersComponent, CombinationListComponent],
  templateUrl: './combination-search.component.html',
  styleUrl: './combination-search.component.scss',
})
export class SearchCombinationsPageComponent {
  sharedState = inject(SharedStateService);
  offcanvasService = inject(NgbOffcanvas);
  private readonly modalService = inject(NgbModal);
  private readonly authService = inject(AuthService);
  private readonly loginModalService = inject(LoginModalService);

  readonly isAuthenticated = toSignal(
    this.authService.currentUser$.pipe(map((user) => user !== null)),
    { initialValue: this.authService.isAuthenticated }
  );

  openSidebar(content: any) {
    this.offcanvasService.open(content, { position: 'start' });
  }

  openModalCombiantion() {
    if (!this.isAuthenticated()) {
      this.loginModalService.open('login');
      return;
    }
    this.modalService.open(CombinationsFormComponent, {
      size: 'xl',
      centered: true,
      fullscreen: 'lg',
    });
  }
}
