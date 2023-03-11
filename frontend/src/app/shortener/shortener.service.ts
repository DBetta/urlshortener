import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ShortenerRequestDto, ShortenerResponseDto } from './shortener.models';
import { Observable } from 'rxjs';
import { DOCUMENT } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class ShortenerService {
  private http = inject(HttpClient);
  private document = inject(DOCUMENT);

  shortenLongUrl(payload: ShortenerRequestDto): Observable<ShortenerResponseDto> {
    return this.http.post<ShortenerResponseDto>('/api/shortener', payload);
  }

  generateShortUrl(responseDto: ShortenerResponseDto): string {
    const baseUrl = this.document.location.origin;
    const shortUrl = `/l/${responseDto.shortUrl}`;
    const url = new URL(shortUrl, baseUrl);
    return url.href;
  }

  copyToClipboard(shortUrl: string | null): void {
    console.log(shortUrl);
    if (!shortUrl) return;
    try {
      const _ = navigator.clipboard.writeText(shortUrl);
    } catch (err) {
      console.error(err);
    }
  }
}
