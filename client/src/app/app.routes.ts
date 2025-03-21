import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { loginGuard } from './guards/login.guard';
import { ExamsComponent } from './exams/exams.component';
import { CreateExamComponent } from './create-exam/create-exam.component';
import { UpdateExamComponent } from './update-exam/update-exam.component';
import { NotificationsComponent } from './Notification/notifications/notifications.component';
import { CreatenotificationsComponent } from './Notification/createnotifications/createnotifications.component';
import { UpdateNotificationComponent } from './Notification/update-notification/update-notification.component';
import { UserComponent } from './user/user.component';

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
    ]
  },
  {
    path: 'notifications',
    runGuardsAndResolvers: 'always',
    canActivate: [authGuard],
    children: [
      { path: '', component: NotificationsComponent, },
      { path: 'create-notifi', component: CreatenotificationsComponent},
      { path: 'update/:mathongbao', component: UpdateNotificationComponent, }

    ]
  },
  {
    path: 'users',
    runGuardsAndResolvers: 'always',
    canActivate: [authGuard],
    children: [
      { path: '', component: UserComponent, },
    ]
  },
  { path: '**', redirectTo: 'login' }
];
