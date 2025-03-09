import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appHasRole]'
})
export class HasRoleDirective implements OnInit {
  @Input() appHasRole!: string[];

  constructor(
    private viewContainerRef: ViewContainerRef,
    private templateRef: TemplateRef<any>
  ) { }

  ngOnInit(): void {
    const userJson = sessionStorage.getItem('user');
    if (userJson) {
      const userObject = JSON.parse(userJson);
      const userRole = userObject?.role;

      // Kiểm tra vai trò
      if (userRole && this.appHasRole.includes(userRole)) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainerRef.clear();
      }
    } else {
      this.viewContainerRef.clear();
    }
  }
}


