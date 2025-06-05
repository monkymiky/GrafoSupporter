import { Component, inject } from '@angular/core';
import { SharedStateService } from '../../shared/shared-state.service';
import { SingleCombination } from './combination/single-combination.component';

@Component({
  selector: 'app-combination-list',
  imports: [SingleCombination],
  templateUrl: './combination-list.component.html',
  styleUrl: './combination-list.component.css',
})
export class CombinationListComponent {
  sharedState = inject(SharedStateService);
  public triggerSearch() {
    this.sharedState.triggerCombinationSearch();
  }
}
