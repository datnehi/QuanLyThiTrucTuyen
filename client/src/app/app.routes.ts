import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { loginGuard } from './guards/login.guard';
import { ExamsComponent } from './exam/exams/exams.component';
import { CreateExamComponent } from './exam/create-exam/create-exam.component';
import { UpdateExamComponent } from './exam/update-exam/update-exam.component';
import { DetailExamComponent } from './exam/detail-exam/detail-exam.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent, canActivate: [loginGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard]},
  {
    path: 'exams',
    runGuardsAndResolvers: 'always',
    canActivate: [authGuard],
    children: [
      { path: '', component: ExamsComponent, },
      { path: 'create', component: CreateExamComponent, },
      { path: 'update/:maKiThi', component: UpdateExamComponent, },
      { path: 'detail/:maKiThi', component: DetailExamComponent, },
    ]
  },
  { path: '**', redirectTo: 'login' }
];
