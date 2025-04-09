import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "./header/header.component";
import { AccountService } from './services/account.service';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    CommonModule,
    SidebarComponent,
    HeaderComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  userToken: string | null = null;
  user: any = null;
  isExamPage: boolean = false;

  constructor(private accountSevice: AccountService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    this.accountSevice.userToken$.subscribe(token => {
      this.userToken = token;
    });
    this.accountSevice.userInfo$.subscribe(user => {
      this.user = user;
    });
    this.router.events.subscribe(() => {
      this.isExamPage = this.router.url.includes('start');
    });
  }
}
