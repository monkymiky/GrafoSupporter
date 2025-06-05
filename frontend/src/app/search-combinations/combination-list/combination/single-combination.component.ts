import { Component, Input } from '@angular/core';
import { Combination } from '../combination.interface';

@Component({
  selector: 'app-single-combination',
  standalone: true,
  templateUrl: './combination.component.html',
  styleUrls: ['./combination.component.css'],
})
export class SingleCombination {
  @Input() combination!: Combination;
}
