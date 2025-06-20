import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/home/home.component').then((m) => m.HomeComponent),
        title: 'Homepage - Grafologo Digitale',
      },
      {
        path: 'ricerca-combinazioni',
        loadComponent: () =>
          import(
            './search-combinations/search-combinations-page/search-combinations-page.component'
          ).then((m) => m.SearchCombinationsPageComponent),
        title: 'Ricerca Combinazioni',
      },
    ],
  },
];
