import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { PerfilComponent } from './features/perfil/perfil.component';
import { UsuariosComponent } from './features/admin/usuarios/usuarios.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
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
