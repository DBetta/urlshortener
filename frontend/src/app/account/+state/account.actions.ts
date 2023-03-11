import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { AuthRequestDto, AuthResponseDto, RegisterRequestDto, RegisterResponseDto } from '../accounts.models';

export const AccountsActions = createActionGroup({
  source: 'Accounts',
  events: {
    'Persistent Authenticate': emptyProps(),
    'Persistent Authenticate Success': props<{ token: AuthResponseDto }>(),
    'Persistent Authenticate Failure': emptyProps()
  }
});

export const LoginPageActions = createActionGroup({
  source: 'Login Page',
  events: {
    'Login': props<{ auth: AuthRequestDto }>()
  }
});

export const LoginApiActions = createActionGroup({
  source: 'Login Page API',
  events: {
    'Login Success': props<{ token: AuthResponseDto }>(),
    'Login Failure': props<{ error: any }>()
  }
});

export const RegisterPageActions = createActionGroup({
  source: 'Register Page',
  events: {
    'Register': props<{ register: RegisterRequestDto }>()
  }
});

export const RegisterApiActions = createActionGroup({
  source: 'Register Api',
  events: {
    'Register Success': props<{ payload: RegisterResponseDto }>(),
    'Register Failure': props<{ error: any }>()
  }
});
