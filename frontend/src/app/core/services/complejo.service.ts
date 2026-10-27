import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Cancha, Complejo, RegistrarCanchaRequest } from '../models/complejo.model';

@Injectable({
  providedIn: 'root'
})
export class ComplejoService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  registrarComplejo(complejo: Partial<Complejo>): Observable<Complejo> {
    return this.http.post<Complejo>(`${this.apiUrl}/complejos`, complejo);
  }

  obtenerMiComplejo(): Observable<Complejo | null> {
    return this.http.get<Complejo | null>(`${this.apiUrl}/complejos/mi-complejo`);
  }

  actualizarMiComplejo(complejo: Partial<Complejo>): Observable<Complejo> {
    return this.http.put<Complejo>(`${this.apiUrl}/complejos/mi-complejo`, complejo);
  }

  obtenerTodosLosComplejos(): Observable<Complejo[]> {
    return this.http.get<Complejo[]>(`${this.apiUrl}/complejos`);
  }

  registrarCancha(cancha: RegistrarCanchaRequest): Observable<Cancha> {
    return this.http.post<Cancha>(`${this.apiUrl}/canchas`, cancha);
  }

  obtenerMisCanchas(): Observable<Cancha[]> {
    return this.http.get<Cancha[]>(`${this.apiUrl}/canchas/mis-canchas`);
  }

  obtenerCanchasPorComplejo(complejoId: number): Observable<Cancha[]> {
    return this.http.get<Cancha[]>(`${this.apiUrl}/canchas/complejo/${complejoId}`);
  }

  eliminarCancha(canchaId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/canchas/${canchaId}`);
  }
}
