import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  model: any = {}

  constructor(public accountService: AccountService, private router: Router) { }

  login() {
    this.accountService.login(this.model).subscribe({
      next: response => {
        this.router.navigate(['/dashboard']);
      }
    })
  }
}
