import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { fromAccounts } from './account/+state';

@Component({
  standalone: true,
  selector: 'url-sht-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    RouterOutlet,
    CommonModule
  ]
})
export class AppComponent implements OnInit {

  ngOnInit(): void {
  }
}
