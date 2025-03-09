import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { NavComponent } from './nav/nav.component';
import { HasRoleDirective } from './directives/has-role.directive';
import { HeaderComponent } from "./header/header.component";
import { AccountService } from './services/account.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    CommonModule,
    LoginComponent,
    NavComponent,
    HasRoleDirective,
    HeaderComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  userToken: string | null = null;
  user: any = null;

  constructor(private accountSevice: AccountService) {}

  ngOnInit() {
    this.accountSevice.userToken$.subscribe(token => {
      this.userToken = token;
    });

    this.accountSevice.userInfo$.subscribe(user => {
      this.user = user;
    });
  }
}
