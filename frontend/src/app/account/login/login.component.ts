import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { LoginPageActions } from '../+state';

@Component({
  selector: 'url-sht-login',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  private readonly fb = inject(FormBuilder);
  private readonly store = inject(Store);

  readonly loginForm = this.fb.nonNullable.group({
    username: this.fb.nonNullable.control(''),
    password: this.fb.nonNullable.control('')
  });

  onLogin(): void {
    if (this.loginForm.invalid) return;

    const rawValue = this.loginForm.getRawValue();
    this.store.dispatch(LoginPageActions.login({ auth: rawValue }));
  }

}
