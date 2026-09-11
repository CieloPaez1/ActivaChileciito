import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../../../core/models/usuario.model';
import { RolUsuario } from '../../../core/models/rol-usuario.enum';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  registrarUsuario(usuario: Usuario): Observable<any> {
    return this.http.post(this.apiUrl, usuario, { responseType: 'text' });
  }

  obtenerUsuariosPorRol(rol: RolUsuario): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/rol/${rol}`);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}
