import { Component } from '@angular/core';
import { FiltersComponent } from '../filters/filters.component';
import { CombinationListComponent } from '../combination-list/combination-list.component';

@Component({
  selector: 'app-search-combinations-page',
  imports: [FiltersComponent, CombinationListComponent],
  templateUrl: './search-combinations-page.component.html',
  styleUrl: './search-combinations-page.component.css',
})
export class SearchCombinationsPageComponent {}
