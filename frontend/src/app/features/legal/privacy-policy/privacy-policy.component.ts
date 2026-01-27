import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-privacy-policy',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './privacy-policy.component.html',
  styleUrl: './privacy-policy.component.scss',
})
export class PrivacyPolicyComponent implements OnInit, OnDestroy {
  ngOnInit(): void {
    document.body.classList.add('active-scrolling');
    document.documentElement.classList.add('active-scrolling');
  }

  ngOnDestroy(): void {
    document.body.classList.remove('active-scrolling');
    document.documentElement.classList.remove('active-scrolling');
  }
}
