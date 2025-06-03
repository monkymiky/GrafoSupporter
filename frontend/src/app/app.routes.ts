import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/ricerca-combinazioni', pathMatch: 'full' },
  {
    path: 'ricerca-combinazioni',
    loadComponent: () =>
      import(
        './search-combinations/search-combinations-page/search-combinations-page.component'
      ).then((m) => m.SearchCombinationsPageComponent),
  },
  {
    path: 'aggiunta-combinazioni',
    loadComponent: () =>
      import('./add-combinations/add-combinations.component').then(
        (m) => m.AddCombinationsComponent
      ),
  },
];
