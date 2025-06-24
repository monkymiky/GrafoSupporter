import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { routes } from '../../app.routes';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  constructor(private router: Router) {}
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
import { Router } from '@angular/router';
