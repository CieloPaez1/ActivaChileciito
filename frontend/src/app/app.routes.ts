import { Routes } from '@angular/router';
import { RegistroUsuarioComponent } from './features/usuarios/registro-usuario/registro-usuario.component';
import { ListaUsuariosComponent } from './features/usuarios/lista-usuarios/lista-usuarios.component';

export const routes: Routes = [
  { path: 'registro', component: RegistroUsuarioComponent },
  { path: 'usuarios', component: ListaUsuariosComponent },
  { path: '', redirectTo: '/usuarios', pathMatch: 'full' }
];
