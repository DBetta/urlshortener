import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'url-sht-login',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  readonly #formBuilder = inject(FormBuilder);
  readonly #nonNullableFormBuilder = this.#formBuilder.nonNullable;

  readonly loginForm = this.#nonNullableFormBuilder.group({
    username: this.#nonNullableFormBuilder.control(''),
    password: this.#nonNullableFormBuilder.control('')
  });

  onLogin(): void {

  }

}
