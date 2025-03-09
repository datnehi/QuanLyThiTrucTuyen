import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const loginGuard: CanActivateFn = (route, state) => {
  const router = inject(Router); // Inject service vào đây
    if (sessionStorage.getItem('userToken')) {
      router.navigate(['/dashboard']);
      return false;
    } else {
      return true;
    }
};
