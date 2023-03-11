import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter, withEnabledBlockingInitialNavigation } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { isDevMode } from '@angular/core';
import { provideEffects } from '@ngrx/effects';
import { appRoutes } from './app/app.routes';
import { apiInterceptor } from './app/app.interceptors';
import { provideAccountsFeature } from './app/account/+state';


bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([apiInterceptor])),
    provideRouter(appRoutes, withEnabledBlockingInitialNavigation()),

    provideEffects(),
    provideStore(),
    provideStoreDevtools({
      name: 'Url Shortener Application',
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: true,
      trace: false,
      traceLimit: 75
    }),


    // account feature
    provideAccountsFeature(),
  ]
})
  .catch(err => console.log(err));
