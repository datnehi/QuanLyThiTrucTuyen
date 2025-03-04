import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { UserService } from './services/user.service';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { NavComponent } from './nav/nav.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HttpClientModule,
    CommonModule,
    LoginComponent,
    NavComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  isLoggedIn(): boolean {
    if (typeof window !== 'undefined') {
      // Chạy trong trình duyệt
      return localStorage.getItem('isLoggedIn') === 'true';
    }
    return false; // Chạy trên server
  }
}
