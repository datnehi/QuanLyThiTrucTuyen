import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { loginGuard } from './guards/login.guard';
import { QuestionsComponent } from './questions/questions.component';
import { UsersComponent } from './users/users.component';
import { NotificationsComponent } from './Notification/notifications/notifications.component';
import { CreatenotificationsComponent } from './Notification/createnotifications/createnotifications.component';
import { UpdateNotificationComponent } from './Notification/update-notification/update-notification.component';
import { UserComponent } from './user/user.component';
import { ExamsComponent } from './exam/exams/exams.component';
import { CreateExamComponent } from './exam/create-exam/create-exam.component';
import { UpdateExamComponent } from './exam/update-exam/update-exam.component';
import { DetailExamComponent } from './exam/detail-exam/detail-exam.component';
import { HocphanComponent } from './hocphan/hocphan.component';
import { StudentExamsComponent } from './student-exam/student-exams/student-exams.component';
import { StudentExamDetailComponent } from './student-exam/student-exam-detail/student-exam-detail.component';
import { StudentExamStartComponent } from './student-exam/student-exam-start/student-exam-start.component';
import { StudentExamResultComponent } from './student-exam/student-exam-result/student-exam-result.component';
import { NotFoundComponent } from './not-found/not-found.component';

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

  { path: 'courses', component: HocphanComponent, canActivate: [authGuard],},
  { path: 'questions', component: QuestionsComponent, canActivate: [authGuard]},
  { path: 'users', component: UsersComponent, canActivate: [authGuard]},
  {
    path: 'student-exams',
    runGuardsAndResolvers: 'always',
    canActivate: [authGuard],
    children: [
      { path: '', component: StudentExamsComponent, },
      { path: 'detail/:maKiThi', component: StudentExamDetailComponent, },
      { path: 'start/:maKiThi', component: StudentExamStartComponent},
      { path: 'result/:maKiThi', component: StudentExamResultComponent},
    ]
  },
  { path: '**', component: NotFoundComponent }
];
