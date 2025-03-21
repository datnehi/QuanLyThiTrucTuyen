import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { loginGuard } from './guards/login.guard';
import { ExamsComponent } from './exams/exams.component';
import { CreateExamComponent } from './create-exam/create-exam.component';
import { UpdateExamComponent } from './update-exam/update-exam.component';
import { QuestionsComponent } from './questions/questions.component';
import { UsersComponent } from './users/users.component';


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
      { path: 'update/:maKiThi', component: UpdateExamComponent, }

    ]
  },
  { path: 'questions', component: QuestionsComponent, canActivate: [authGuard]},
  { path: 'users', component: UsersComponent, canActivate: [authGuard]},
  { path: '**', redirectTo: 'login' }
];
