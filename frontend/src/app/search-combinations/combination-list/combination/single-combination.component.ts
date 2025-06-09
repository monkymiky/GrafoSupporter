import { Component, Input } from '@angular/core';
import { Combination } from '../combination.interface';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-single-combination',
  standalone: true,
  templateUrl: './combination.component.html',
  styleUrls: ['./combination.component.css'],
  imports: [NgbAccordionModule],
})
export class SingleCombination {
  @Input() combination!: Combination;
}
