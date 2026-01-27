import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-terms-of-service',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './terms-of-service.component.html',
  styleUrl: './terms-of-service.component.scss',
})
export class TermsOfServiceComponent implements OnInit, OnDestroy {
  ngOnInit(): void {
    document.body.classList.add('active-scrolling');
    document.documentElement.classList.add('active-scrolling');
  }

  ngOnDestroy(): void {
    document.body.classList.remove('active-scrolling');
    document.documentElement.classList.remove('active-scrolling');
  }
}
