import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { signsResolver } from './shared/signs.resolver';
import { authGuard } from './shared/guards/auth.guard';

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
        canActivate: [authGuard],
      },
      {
        path: 'contatti',
        loadComponent: () =>
          import('./pages/contacts/contacts.component').then(
            (m) => m.ContactsComponent
          ),
        title: 'Contatti - GrafoSupporter',
      },
    ],
  },
];
