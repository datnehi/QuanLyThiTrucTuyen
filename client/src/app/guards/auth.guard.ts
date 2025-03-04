import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { map } from 'rxjs/operators';
import { AccountService } from '../services/account.service';

export const authGuard: CanActivateFn = (route, state) => {
  const accountService = inject(AccountService); // Inject service vào đây

  return accountService.currentUser$.pipe(
    map(user => {
      if (user) {
        return true;
      } else {
        alert("You shall not pass!");
        return false;
      }
    })
  );
}
