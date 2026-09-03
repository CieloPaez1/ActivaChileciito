import { Routes } from '@angular/router';
import { RegistroUsuarioComponent } from './features/usuarios/registro-usuario/registro-usuario.component';
import { ListaUsuariosComponent } from './features/usuarios/lista-usuarios/lista-usuarios.component';

import { DashboardDeportistaComponent } from './features/dashboards/dashboard-deportista/dashboard-deportista.component';
import { DashboardAdminComplejoComponent } from './features/dashboards/dashboard-admin-complejo/dashboard-admin-complejo.component';
import { DashboardSuperAdminComponent } from './features/dashboards/dashboard-super-admin/dashboard-super-admin.component';
import { LoginComponent } from './features/auth/login/login.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroUsuarioComponent },
  { path: 'usuarios', component: ListaUsuariosComponent },
  { path: 'deportista', component: DashboardDeportistaComponent },
  { path: 'admin-complejo', component: DashboardAdminComplejoComponent },
  { path: 'super-admin', component: DashboardSuperAdminComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
