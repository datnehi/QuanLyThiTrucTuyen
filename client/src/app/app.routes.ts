import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { loginGuard } from './guards/login.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent, canActivate: [loginGuard]},
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [authGuard],
    children: [
      { path: 'dashboarddashboard', component: DashboardComponent, }
    ]
  },
];
