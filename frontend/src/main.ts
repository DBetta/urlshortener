import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter, Routes, withDebugTracing } from '@angular/router';

const APP_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./app/account').then(m => m.ACCOUNT_ROUTES)
  }
];


bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(APP_ROUTES, withDebugTracing())
  ]
})
  .catch(err => console.log(err));
