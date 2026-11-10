import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest, Usuario, Rol } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) { 
    this.checkToken();
  }

  public checkToken() {
    const token = this.getToken();
    if (token) {
      try {
        if (token.startsWith('jwt-token-')) {
          const email = token.split('-')[2];
          this.currentUserSubject.next({ email: email, rol: Rol.DEPORTISTA } as Usuario);
        } else {
          // Decodificar JWT
          const payload = JSON.parse(atob(token.split('.')[1]));
          const rolExtraido = payload.rol || payload.role || payload.Rol || Rol.DEPORTISTA;
          const usuarioInfo: Partial<Usuario> = {
            id: payload.id,
            email: payload.sub,
            rol: rolExtraido as Rol
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
          this.checkToken();
        }
      })
    );
  }

  register(data: RegisterRequest): Observable<string> {
    return this.http.post(`${environment.apiUrl}/usuarios`, data, { responseType: 'text' });
  }

  requestPasswordReset(email: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/password-reset/request`, { email });
  }

  executePasswordReset(token: string, nuevaClave: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/password-reset/execute`, { token, nuevaClave });
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
