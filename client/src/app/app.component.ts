import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NavComponent } from './nav/nav.component';
import { HeaderComponent } from "./header/header.component";
import { AccountService } from './services/account.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    CommonModule,
    NavComponent,
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
