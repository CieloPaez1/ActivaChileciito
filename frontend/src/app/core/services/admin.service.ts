import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin/usuarios`;

  constructor(private http: HttpClient) { }

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  activarUsuario(id: number): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/activar`, {});
  }

  suspenderUsuario(id: number): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/suspender`, {});
  }

  cambiarRol(id: number, rol: string): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/rol`, { rol });
  }
}
