import { CompleteProfileComponent } from './features/auth/complete-profile/complete-profile.component';
import { Oauth2RedirectComponent } from './features/auth/oauth2-redirect/oauth2-redirect.component';
import { Routes } from '@angular/router';

import { PerfilComponent } from './features/perfil/perfil.component';
import { UsuariosComponent } from './features/admin/usuarios/usuarios.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { AuthPageComponent } from './features/auth/auth-page/auth-page.component';
import { HomeComponent } from './features/home/home.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'complete-profile', component: CompleteProfileComponent },
  { path: 'oauth2/redirect', component: Oauth2RedirectComponent },
  { path: '', component: HomeComponent },
  { path: 'login', component: AuthPageComponent },
  { path: 'register', component: AuthPageComponent },
  { 
    path: 'forgot-password', 
    loadComponent: () => import('./features/auth/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent) 
  },
  { 
    path: 'reset-password', 
    loadComponent: () => import('./features/auth/reset-password/reset-password.component').then(m => m.ResetPasswordComponent) 
  },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'perfil', 
    component: PerfilComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'admin/usuarios', 
    component: UsuariosComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['SUPERUSUARIO'] }
  },
  { path: '**', redirectTo: 'login' }
];
