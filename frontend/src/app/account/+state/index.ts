import { APP_INITIALIZER, EnvironmentProviders, makeEnvironmentProviders } from '@angular/core';
import { provideEffects } from '@ngrx/effects';
import { provideState, Store } from '@ngrx/store';
import { accountsFeature } from './account.state';
import { AccountEffects } from './account.effects';
import { AccountsActions } from './account.actions';
import { filter, takeUntil, takeWhile } from 'rxjs';

export { RegisterPageActions, LoginPageActions } from './account.actions';

const {
  selectAuthToken,
  selectIsLoggedIn,
  selectError
} = accountsFeature;

export const fromAccounts = {
  selectAuthToken,
  selectIsLoggedIn,
  selectAuthError: selectError
};

export function provideAccountsFeature(): EnvironmentProviders {
  return makeEnvironmentProviders([

    {
      provide: APP_INITIALIZER,
      useFactory: (store: Store) => () => {
        store.dispatch(AccountsActions.persistentAuthenticate());

        return store.select(accountsFeature.selectInitialized)
          .pipe(takeWhile(initialized => !initialized));
      },
      multi: true,
      deps: [Store]
    },

    provideState(accountsFeature),
    provideEffects(AccountEffects)
  ]);
}
