import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { fromAccounts } from './account/+state';
import { map } from 'rxjs';

export const noAuthGuard: CanActivateFn = (route, state) => {
  const store = inject(Store);
  const router = inject(Router);

  return store.select(fromAccounts.selectIsLoggedIn)
    .pipe(
      map(isLoggedIn => {
        if (isLoggedIn) return router.createUrlTree(['/shortener']);

        return true;
      })
    );
};

export const authGuard: CanActivateFn = (route, state) => {
  const store = inject(Store);
  const router = inject(Router);
  return store.select(fromAccounts.selectIsLoggedIn)
    .pipe(
      map(isLoggedIn => {
        if (isLoggedIn) return true;

        return router.createUrlTree(['/']);
      })
    );
};
