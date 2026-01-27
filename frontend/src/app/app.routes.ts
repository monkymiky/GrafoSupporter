import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { signsResolver } from './features/combination-search/components/filters/resolver/signs.resolver';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./layout/home/home.component').then((m) => m.HomeComponent),
        title: 'Homepage - Grafosupporter',
      },
      {
        path: 'ricerca-combinazioni',
        loadComponent: () =>
          import(
            './features/combination-search/combination-search.component'
          ).then((m) => m.SearchCombinationsPageComponent),
        title: 'Ricerca Combinazioni - Grafosupporter',
        resolve: {
          signs: signsResolver,
        },
        // canActivate: [authGuard], // Disattivato per permettere accesso pubblico. Per riattivare: decommentare questa riga
      },
      {
        path: 'contatti',
        loadComponent: () =>
          import('./features/contacts/contacts.component').then(
            (m) => m.ContactsComponent
          ),
        title: 'Contatti - GrafoSupporter',
      },
      {
        path: 'impostazioni',
        loadComponent: () =>
          import('./features/settings/settings.component').then(
            (m) => m.SettingsComponent
          ),
        title: 'Impostazioni - GrafoSupporter',
        canActivate: [authGuard],
      },
      {
        path: 'termini-di-servizio',
        loadComponent: () =>
          import('./features/legal/terms-of-service/terms-of-service.component').then(
            (m) => m.TermsOfServiceComponent
          ),
        title: 'Termini di servizio - GrafoSupporter',
      },
      {
        path: 'informativa-privacy',
        loadComponent: () =>
          import('./features/legal/privacy-policy/privacy-policy.component').then(
            (m) => m.PrivacyPolicyComponent
          ),
        title: 'Informativa sulla privacy - GrafoSupporter',
      },
    ],
  },
];
