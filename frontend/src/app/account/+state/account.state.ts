import { createFeature, createSelector } from '@ngrx/store';
import * as fromReducer from './account.reducers';

export const accountsFeature = createFeature({
  name: 'accounts',
  reducer: fromReducer.reducer,
  extraSelectors: ({ selectToken }) => ({
    selectIsLoggedIn: createSelector(
      selectToken,
      (token) => token?.token != null
    ),
    selectAuthToken: createSelector(
      selectToken,
      (token) => token?.token
    )
  })
});
