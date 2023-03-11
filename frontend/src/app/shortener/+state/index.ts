import { EnvironmentProviders, makeEnvironmentProviders } from '@angular/core';
import { provideState } from '@ngrx/store';
import { shortenerFeature } from './shortener.state';
import { provideEffects } from '@ngrx/effects';
import { ShortenerEffects } from './shortener.effects';

const {
  selectShortUrl,

  ...otherActions
} = shortenerFeature;

export const fromShortener = {
  ...otherActions,
  selectShortUrl
};

export function provideShortenerFeature(): EnvironmentProviders {
  return makeEnvironmentProviders([
    provideState(shortenerFeature),
    provideEffects(ShortenerEffects)
  ]);
}
