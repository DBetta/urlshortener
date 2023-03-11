import { inject, Injectable } from '@angular/core';
import { from, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AuthRequestDto, AuthResponseDto, RegisterRequestDto, RegisterResponseDto } from './accounts.models';
import { ActivatedRoute, Router } from '@angular/router';

const AUTH_TOKEN_KEY = 'urlshortener.accounts.token';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private http = inject(HttpClient);
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);

  authenticate(payload: AuthRequestDto): Observable<AuthResponseDto> {
    return this.http.post<AuthResponseDto>('/api/authenticate', payload);
  }

  register(payload: RegisterRequestDto): Observable<RegisterResponseDto> {
    return this.http.post<RegisterResponseDto>('/api/register', payload);
  }

  navigateToLogin(payload: any): Observable<any> {
    const promise = this.router.navigate(['/auth/login']);
    return from(promise);
  }

  onLoginSuccess(token: AuthResponseDto) {
    localStorage.setItem(AUTH_TOKEN_KEY, JSON.stringify(token));
    const _ = this.router.navigate(['/shortener']);
  }

  authenticateViaPersistence(): AuthResponseDto | null {
    const tokenStr = localStorage.getItem(AUTH_TOKEN_KEY);
    if (tokenStr == null) return null;
    return JSON.parse(tokenStr);
  }
}
