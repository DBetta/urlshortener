import { createReducer, on } from '@ngrx/store';
import { AuthRequestDto, AuthResponseDto } from '../accounts.models';
import {
  AccountsActions,
  LoginApiActions,
  LoginPageActions,
  RegisterApiActions,
  RegisterPageActions
} from './account.actions';

export interface AccountState {
  initialized: boolean;
  loading: boolean;
  token: AuthResponseDto | null;
  error: any | null;
}

const initialState: AccountState = {
  initialized: false,
  token: null,
  error: null,
  loading: false
};

export const reducer = createReducer(
  initialState,
  on(
    LoginPageActions.login,
    RegisterPageActions.register,
    (state) => {
      return { ...state, loading: true };
    }
  ),
  on(
    LoginApiActions.loginSuccess,
    AccountsActions.persistentAuthenticateSuccess,
    (state, { token }) => {
      return { ...state, token: token };
    }),
  on(
    LoginApiActions.loginSuccess,
    RegisterApiActions.registerSuccess,
    (state) => {
      return { ...state, loading: false };
    }),
  on(
    LoginApiActions.loginFailure,
    RegisterApiActions.registerFailure,
    (state, { error }) => {
      return { ...state, loading: false, error };
    }),
  on(
    AccountsActions.persistentAuthenticateSuccess,
    AccountsActions.persistentAuthenticateFailure,
    (state) => ({ ...state, initialized: true }))
);
