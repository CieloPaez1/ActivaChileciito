import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // No autorizado o token expirado
        authService.logout();
        router.navigate(['/login']);
      } else if (error.status === 403) {
        // Prohibido (no tiene permisos)
        console.error('Acceso denegado:', error.message);
      }
      
      return throwError(() => error);
    })
  );
};
