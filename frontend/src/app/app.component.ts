import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ActivaChilecitoFront';

  constructor(private router: Router) {}

  get isStandalonePage(): boolean {
    const url = this.router.url;
    // Hide standard navbar on auth pages and dashboard
    return url.includes('/login') || 
           url.includes('/register') || 
           url.includes('/forgot-password') || 
           url.includes('/dashboard');
  }
}
