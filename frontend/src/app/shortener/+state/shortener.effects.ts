import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { inject } from '@angular/core';
import { ShortenerService } from '../shortener.service';
import { ShortenerApiActions, ShortenerPageActions } from './shortener.actions';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { Store } from '@ngrx/store';
import { fromShortener } from './index';

const shortenLongUrl = createEffect((actions$ = inject(Actions)) => {

  const shortenerService = inject(ShortenerService);

  return actions$.pipe(
    ofType(ShortenerPageActions.shortenUrl),
    switchMap(({ payload }) => shortenerService.shortenLongUrl(payload).pipe(
      map(responseDto => shortenerService.generateShortUrl(responseDto)),
      map(shortUrl => ShortenerApiActions.shortenerUrlSuccess({ payload: shortUrl })),
      catchError(err => of(ShortenerApiActions.shortenerUrlFailure({ payload: err?.error })))
    ))
  );
}, { functional: true });

const copyToClipboard = createEffect((actions$ = inject(Actions)) => {
  const shortenerService = inject(ShortenerService);
  const store = inject(Store);

  return actions$.pipe(
    ofType(ShortenerPageActions.copyToClipboard),
    concatLatestFrom(() => store.select(fromShortener.selectShortUrl)),
    tap(([action, shortUrl]) => {
      shortenerService.copyToClipboard(shortUrl);
    })
  );
}, { functional: true, dispatch: false });

export const ShortenerEffects = {
  shortenLongUrl,
  copyToClipboard
};
