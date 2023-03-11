import { Routes } from '@angular/router';
import { authGuard, noAuthGuard } from './app.guards';
import { provideShortenerFeature } from './shortener/+state';

export const appRoutes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./account'),
    canMatch: [noAuthGuard]
  },
  {
    path: 'shortener',
    providers: [provideShortenerFeature()],
    loadComponent: () => import('./shortener/shortener.component'),
    canMatch: [authGuard]
  }
];
