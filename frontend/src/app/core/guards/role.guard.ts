import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles: string[] = route.data['roles'];
  const userRole = authService.getRole();

  if (authService.isLoggedIn() && userRole && expectedRoles.includes(userRole)) {
    return true;
  }

  // Si no tiene permisos, redirigir a una ruta segura (ej. perfil o login)
  return router.createUrlTree(['/perfil']);
};
