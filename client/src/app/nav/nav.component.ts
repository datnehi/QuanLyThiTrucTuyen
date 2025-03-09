import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HasRoleDirective } from '../directives/has-role.directive';

@Component({
  selector: 'app-nav',
  imports: [
    RouterModule,
    HasRoleDirective
  ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {

}
