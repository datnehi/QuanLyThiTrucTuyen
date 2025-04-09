import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HasRoleDirective } from '../directives/has-role.directive';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterModule,
    HasRoleDirective
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

}
