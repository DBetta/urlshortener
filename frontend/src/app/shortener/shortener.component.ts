import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { ShortenerPageActions } from './+state/shortener.actions';
import { ShortenerRequestDto } from './shortener.models';
import { fromShortener } from './+state';
import { map, startWith, tap } from 'rxjs';

@Component({
  selector: 'url-sht-shortener',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './shortener.component.html',
  styleUrls: ['./shortener.component.css']
})
export default class ShortenerComponent {
  private readonly fb = inject(FormBuilder);
  private readonly store = inject(Store);

  private readonly _shortUrl$ = this.store.select(fromShortener.selectShortUrl);

  readonly longUrlControl = this.fb.control('');
  readonly shortUrl$ = this._shortUrl$.pipe(
    map(shortUrl => shortUrl ?? 'Provide a long url')
  );
  readonly isShortUrlGenerated$ = this._shortUrl$.pipe(map(shortUrl => !!shortUrl));
  readonly loading$ = this.store.select(fromShortener.selectLoading);

  onShortenUrl(): void {
    if (this.longUrlControl.invalid) return;

    const longUrl = this.longUrlControl.getRawValue();
    if (!longUrl) return;

    this.longUrlControl.reset();

    const payload: ShortenerRequestDto = {
      longUrl
    };
    this.store.dispatch(ShortenerPageActions.shortenUrl({ payload }));
  }

  onCopyShortUrl(): void {
    this.store.dispatch(ShortenerPageActions.copyToClipboard());
  }

}
