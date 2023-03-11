import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { fromAccounts } from './account/+state';
import { exhaustMap, map, take } from 'rxjs';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);

  return store.select(fromAccounts.selectAuthToken).pipe(
    take(1),
    exhaustMap(token => {
      if (!token) return next(req);

      const newReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });

      return next(newReq);
    })
  );
};

