import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter, Routes, withDebugTracing } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

const APP_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./app/account')
  },
  {
    path: 'shortener',
    loadComponent: () => import('./app/shortener/shortener.component').then(c => c.ShortenerComponent)
  }
];


bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([])),
    provideRouter(APP_ROUTES, withDebugTracing())
  ]
})
  .catch(err => console.log(err));
