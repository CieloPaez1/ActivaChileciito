import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest, Usuario, Rol } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // Guardamos el estado de autenticación en un BehaviorSubject para reaccionar a cambios en la UI
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) { 
    this.checkToken();
  }

  // Verifica si hay token al iniciar
  private checkToken() {
    const token = this.getToken();
    if (token) {
      try {
        if (token.startsWith('jwt-token-')) {
          // Mock token from backend (e.g. jwt-token-email@ejemplo.com-uuid)
          const email = token.split('-')[2];
          this.currentUserSubject.next({ email: email, rol: 'CLIENTE' } as Usuario);
        } else {
          // Decode real JWT base64
          const payload = JSON.parse(atob(token.split('.')[1]));
          const usuarioInfo: Partial<Usuario> = {
            email: payload.sub,
            rol: payload.role as Rol
          };
          this.currentUserSubject.next(usuarioInfo as Usuario);
        }
      } catch (e) {
        this.logout();
      }
    }
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
          this.checkToken(); // Actualiza el subject
        }
      })
    );
  }

  register(data: RegisterRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/register`, data);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getRole(): Rol | null {
    const user = this.currentUserSubject.value;
    return user ? user.rol : null;
  }
}
