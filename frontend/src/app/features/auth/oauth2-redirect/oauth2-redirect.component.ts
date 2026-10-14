import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-oauth2-redirect',
  standalone: true,
  imports: [CommonModule],
  template: '<div class="d-flex justify-content-center mt-5"><div class="spinner-border text-success"></div><span class="ms-3">Iniciando sesión con Google...</span></div>'
})
export class Oauth2RedirectComponent implements OnInit {
  constructor(private route: ActivatedRoute, private router: Router, private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParams['token'];
    if (token) {
      // Guardar el token
      localStorage.setItem('token', token);
      (this.authService as any).checkToken();
      
      // Comprobar si el perfil está completo
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
      this.http.get<any>('http://localhost:8080/api/perfil/me', { headers }).subscribe({
        next: (profile) => {
          if (!profile.telefono || profile.telefono.trim() === '') {
            // Faltan datos, mandarlo a completar perfil
            this.router.navigate(['/complete-profile']);
          } else {
            // Perfil completo
            this.router.navigate(['/dashboard']);
          }
        },
        error: () => {
          // Si falla, por las dudas lo mandamos al dashboard
          this.router.navigate(['/dashboard']);
        }
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
}