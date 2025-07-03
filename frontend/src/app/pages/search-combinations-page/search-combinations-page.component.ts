import { Component, inject } from '@angular/core';
import { FiltersComponent } from '../../search-combinations/filters/filters.component';
import { CombinationListComponent } from '../../search-combinations/combination-list/combinations-list.component';
import { NgbModal, NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { CombinationsFormComponent } from '../../combinations-form/combinations-form.component';
import { SharedStateService } from '../../shared/shared-state.service';

@Component({
  selector: 'app-search-combinations-page',
  standalone: true,
  imports: [FiltersComponent, CombinationListComponent],
  templateUrl: './search-combinations-page.component.html',
  styleUrl: './search-combinations-page.component.scss',
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
