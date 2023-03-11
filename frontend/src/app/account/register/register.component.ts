import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { RegisterRequestDto } from '../accounts.models';
import { RegisterPageActions } from '../+state';

@Component({
  selector: 'url-sht-register',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  private readonly fb = inject(FormBuilder);
  private readonly store = inject(Store);

  readonly registerForm = this.fb.group({
    email: this.fb.nonNullable.control(''),
    firstName: this.fb.nonNullable.control(''),
    lastName: this.fb.nonNullable.control(''),
    password: this.fb.nonNullable.control(''),
    confirmPassword: this.fb.nonNullable.control('')
  });

  onRegister(): void {
    if (this.registerForm.invalid) return;

    const { confirmPassword, password, ...rawValue } = this.registerForm.getRawValue();

    if (confirmPassword !== password) {
      // handle error here
      console.log('password mismatch')
      return;
    }

    const registerRequest: RegisterRequestDto = {
      ...rawValue,
      password
    };
    this.store.dispatch(RegisterPageActions.register({ register: registerRequest }));
  }

}
