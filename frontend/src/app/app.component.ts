import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CombiantionService } from './shared/combinations.service';
import { SharedStateService } from './shared/shared-state.service';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  public service = inject(CombiantionService);
  public stateService = inject(SharedStateService);

  onSearch(): void {
    console.log('Button clicked, initiating search...');
    this.service.searchCombinations().subscribe({
      next: (combinations) => {
        console.log('Combinations received:', combinations);
        this.stateService.setcombinations(combinations);
      },
      error: (err) => {
        console.error('Error fetching combinations:', err);
      },
      complete: () => {
        console.log('Combination search completed.');
      },
    });
  }
}
