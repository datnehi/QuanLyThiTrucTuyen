import { Component } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  user: any = null;

  constructor(public accountService: AccountService, private router: Router) {}

  ngOnInit() {
    this.accountService.userInfo$.subscribe(user => {
      this.user = user;
    });
  }

  logout() {
    this.accountService.logout().subscribe({
      next: () => {
        this.router.navigate(['']);
      }
    });
  }
}
