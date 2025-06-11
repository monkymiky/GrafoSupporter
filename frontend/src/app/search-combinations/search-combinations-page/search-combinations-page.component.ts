import { Component, inject } from '@angular/core';
import { FiltersComponent } from '../filters/filters.component';
import { CombinationListComponent } from '../combination-list/combinations-list.component';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-search-combinations-page',
  standalone: true,
  imports: [FiltersComponent, CombinationListComponent],
  templateUrl: './search-combinations-page.component.html',
  styleUrl: './search-combinations-page.component.css',
})
export class SearchCombinationsPageComponent {
  offcanvasService = inject(NgbOffcanvas);

  openSidebar(content: any) {
    this.offcanvasService.open(content, { position: 'start' });
  }
}
