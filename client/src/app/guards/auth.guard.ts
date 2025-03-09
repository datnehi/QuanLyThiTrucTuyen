import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AccountService } from '../services/account.service';
import { map, take } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const accountService = inject(AccountService);

  return accountService.userToken$.pipe(
    take(1),
    map(token => {
      if (token) {
        return true;
      } else {
        router.navigate(['']);
        return false;
      }
    })
  );
}

