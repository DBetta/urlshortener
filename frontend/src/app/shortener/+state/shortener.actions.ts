import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { ShortenerRequestDto } from '../shortener.models';

export const ShortenerPageActions = createActionGroup({
  source: 'Shortener Page',
  events: {
    'Shorten Url': props<{ payload: ShortenerRequestDto }>(),
    'Copy To Clipboard': emptyProps()
  }
});

export const ShortenerApiActions = createActionGroup({
  source: 'Shortener API',
  events: {
    'Shortener Url Success': props<{ payload: string }>(),
    'Shortener Url Failure': props<{ payload: any }>()
  }
});
