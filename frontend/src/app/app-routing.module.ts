import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/layout/home/home.component';
import { canActivate, canActivateChild } from './services/auth/auth-guard';
import { AuthInterceptor } from './services/interceptors/auth-interceptor.service';
import { LoginComponent } from './components/layout/login-page/login/login.component';

// import { SidemenuComponent } from './admin-panel/sidemenu/sidemenu.component';

const routes: Routes = [
  {
    // path: '', component: DashboardComponent,
    path: '',
    component: HomeComponent,
    canActivate: [canActivate],
    canActivateChild: [canActivateChild],
    children: [
      { path: 'home', component: HomeComponent }
    ],
  },
  {
    path: 'login',
    component: LoginComponent,
  },

  // {path: 'dashboard-list', component: LeaveListComponent}
];

// @NgModule({
//   imports: [RouterModule.forRoot(routes)],
//   exports: [RouterModule]
// })
// export class AppRoutingModule { }
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
})
export class AppRoutingModule {}
