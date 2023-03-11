import { createFeature } from '@ngrx/store';

import * as fromReducer from './shortener.reducer';

export const shortenerFeature = createFeature({
  name: 'shortener',
  reducer: fromReducer.reducer
});
