import { createReducer, on } from '@ngrx/store';
import { ShortenerApiActions, ShortenerPageActions } from './shortener.actions';

export interface ShortenerState {
  loading: boolean;
  error: any | null;
  shortUrl: string | null;
}

const initialState: ShortenerState = {
  loading: false,
  error: null,
  shortUrl: null
};

export const reducer = createReducer(
  initialState,
  on(ShortenerPageActions.shortenUrl, (state) => ({ ...state, loading: true })),
  on(ShortenerApiActions.shortenerUrlSuccess,
    (state, { payload }) => ({ ...state, shortUrl: payload })),
  on(ShortenerApiActions.shortenerUrlFailure,
    (state, { payload }) => ({ ...state, error: payload })),
  on(ShortenerApiActions.shortenerUrlFailure,
    ShortenerApiActions.shortenerUrlSuccess, (state) => ({ ...state, loading: false }))
);
