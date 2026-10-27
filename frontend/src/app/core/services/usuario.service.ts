import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario, UpdateUserRequest, CambiarPasswordRequest } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/perfil`;
  private usuariosUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) { }

  getPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/me`);
  }

  updatePerfil(data: UpdateUserRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/me`, data);
  }

  changePassword(data: CambiarPasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/password`, data);
  }

  deleteCuenta(): Observable<void> {
    return this.http.delete<void>(`${this.usuariosUrl}/me`);
  }
}
