import { Routes } from '@angular/router';
import { AccountComponent } from './account.component';

export default [
  {
    path: '',
    component: AccountComponent,
    children: [
      {
        path: 'register',
        loadComponent: () => import('./register/register.component').then(c => c.RegisterComponent)
      },
      {
        path: 'login',
        loadComponent: () => import('./login/login.component').then(c => c.LoginComponent)
      }
    ]
  }
] as Routes;
