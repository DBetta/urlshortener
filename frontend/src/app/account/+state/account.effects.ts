import { Actions, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import {
  AccountsActions,
  LoginApiActions,
  LoginPageActions,
  RegisterApiActions,
  RegisterPageActions
} from './account.actions';
import { catchError, exhaustMap, map, of, tap } from 'rxjs';
import { AccountService } from '../account.service';

const authenticate$ = createEffect((actions$ = inject(Actions)) => {
  const accountService = inject(AccountService);

  return actions$.pipe(
    ofType(LoginPageActions.login),
    exhaustMap(({ auth }) => accountService.authenticate(auth).pipe(
      map(token => LoginApiActions.loginSuccess({ token })),
      catchError(err => of(LoginApiActions.loginFailure({ error: err?.error })))
    ))
  );
}, { functional: true });

const registerAccount$ = createEffect((actions$ = inject(Actions)) => {
  const accountService = inject(AccountService);
  return actions$.pipe(
    ofType(RegisterPageActions.register),
    exhaustMap(({ register }) => accountService.register(register).pipe(
      map(registerDto => RegisterApiActions.registerSuccess({ payload: registerDto })),
      catchError(err => of(RegisterApiActions.registerFailure({ error: err?.error })))
    ))
  );
}, { functional: true });

const navigateToLoginOnRegister$ = createEffect((actions$ = inject(Actions)) => {
  const accountService = inject(AccountService);

  return actions$.pipe(
    ofType(RegisterApiActions.registerSuccess),
    exhaustMap(({ payload }) => accountService.navigateToLogin(payload))
  );
}, { functional: true, dispatch: false });

const onLoginSuccess$ = createEffect((actions$ = inject(Actions)) => {
  const accountService = inject(AccountService);

  return actions$.pipe(
    ofType(LoginApiActions.loginSuccess),
    tap(({ token }) => {
      accountService.onLoginSuccess(token);
    })
  );
}, { functional: true, dispatch: false });

const onAccountsEnter$ = createEffect((actions$ = inject(Actions)) => {

  const accountService = inject(AccountService);

  return actions$.pipe(
    ofType(AccountsActions.persistentAuthenticate),
    map(() => {
      const token = accountService.authenticateViaPersistence();

      return token != null ?
        AccountsActions.persistentAuthenticateSuccess({ token }) :
        AccountsActions.persistentAuthenticateFailure();
    })
  );
}, { functional: true });

export const AccountEffects = {
  authenticate$,
  registerAccount$,
  navigateToLoginOnRegister$,
  onLoginSuccess$,
  onAccountsEnter$
};
