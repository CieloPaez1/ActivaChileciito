import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario, UpdateUserRequest } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private perfilUrl = `${environment.apiUrl}/perfil`;
  private usuariosUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) { }

  getPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.perfilUrl}/me`);
  }

  updatePerfil(data: UpdateUserRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.perfilUrl}/me`, data);
  }

  changePassword(data: { passwordActual: string; nuevaPassword: string }): Observable<void> {
    return this.http.put<void>(`${this.perfilUrl}/password`, data);
  }

  eliminarCuenta(): Observable<void> {
    return this.http.delete<void>(`${this.usuariosUrl}/me`);
  }
}
