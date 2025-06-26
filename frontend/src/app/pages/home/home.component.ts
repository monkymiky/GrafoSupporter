import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  constructor(readonly router: Router) {}
  isHovering = false;
  goDownNow = false;
  imageIndex = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

  play() {
    this.isHovering = true;
  }
  stop() {
    this.isHovering = false;
  }

  image: string = '/api/files/combination-image/scrittura-home3.png';

  panelAngle = 0;

  ngOnInit(): void {
    this.panelAngle = 360 / this.imageIndex.length;
  }
  goDown() {
    this.goDownNow = true;
    setTimeout(() => {
      this.router.navigate(['/ricerca-combinazioni']);
    }, 1250);
  }
}
