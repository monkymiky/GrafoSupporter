import { Component, inject } from '@angular/core';
import { FiltersComponent } from '../filters/filters.component';
import { CombinationListComponent } from '../combination-list/combinations-list.component';
import { NgbModal, NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { AddCombinationsComponent } from '../../add-combinations/add-combinations.component';
import { SharedStateService } from '../../shared/shared-state.service';

@Component({
  selector: 'app-search-combinations-page',
  standalone: true,
  imports: [FiltersComponent, CombinationListComponent],
  templateUrl: './search-combinations-page.component.html',
  styleUrl: './search-combinations-page.component.css',
})
export class SearchCombinationsPageComponent {
  sharedState = inject(SharedStateService);
  offcanvasService = inject(NgbOffcanvas);
  private modalService = inject(NgbModal);

  openSidebar(content: any) {
    this.offcanvasService.open(content, { position: 'start' });
  }

  openModalCombiantion() {
    this.modalService.open(AddCombinationsComponent, {
      size: 'xl',
      centered: true,
      fullscreen: 'lg',
    });
  }
}
