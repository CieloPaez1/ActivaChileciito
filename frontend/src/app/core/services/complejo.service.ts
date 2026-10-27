import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Complejo } from '../models/complejo.model';

@Injectable({
  providedIn: 'root'
})
export class ComplejoService {
  private apiUrl = `${environment.apiUrl}/complejos`;

  constructor(private http: HttpClient) { }

  registrarComplejo(data: Complejo): Observable<Complejo> {
    return this.http.post<Complejo>(this.apiUrl, data);
  }

  getMiComplejo(): Observable<Complejo | null> {
    return this.http.get<Complejo | null>(`${this.apiUrl}/mi-complejo`);
  }

  modificarMiComplejo(data: Complejo): Observable<Complejo> {
    return this.http.put<Complejo>(`${this.apiUrl}/mi-complejo`, data);
  }
}
