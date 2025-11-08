import { Component, inject } from '@angular/core';
import { FiltersComponent } from './components/filters/filters.component';
import { CombinationListComponent } from './components/combination-list/combination-list.component';
import { NgbModal, NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { CombinationsFormComponent } from '../combinations-form/combination-form.component';
import { SharedStateService } from '../../shared/services/shared-state.service';

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

  openSidebar(content: any) {
    this.offcanvasService.open(content, { position: 'start' });
  }

  openModalCombiantion() {
    this.modalService.open(CombinationsFormComponent, {
      size: 'xl',
      centered: true,
      fullscreen: 'lg',
    });
  }
}
