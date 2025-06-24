import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { signsResolver } from './shared/signs.resolver';

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
            './pages/search-combinations-page/search-combinations-page.component'
          ).then((m) => m.SearchCombinationsPageComponent),
        title: 'Ricerca Combinazioni',
        resolve: {
          signs: signsResolver,
        },
      },
    ],
  },
];
