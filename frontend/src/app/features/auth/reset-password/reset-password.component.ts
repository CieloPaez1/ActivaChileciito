import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  token: string = '';
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.resetForm = this.fb.group({
      nuevaClave: ['', [Validators.required, Validators.minLength(6)]],
      confirmarClave: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParams['token'] || '';
    if (!this.token) {
      this.errorMessage = 'Token de recuperación no válido o ausente en la URL.';
    }
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('nuevaClave')?.value === g.get('confirmarClave')?.value
      ? null : { mismatch: true };
  }

  toggleShowPassword() {
    this.showPassword = !this.showPassword;
  }

  toggleShowConfirmPassword() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onSubmit(): void {
    if (this.resetForm.invalid || !this.token) return;

    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const { nuevaClave } = this.resetForm.value;

    this.authService.executePasswordReset(this.token, nuevaClave).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = '¡Contraseña restablecida con éxito! Redirigiendo al inicio de sesión...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2500);
      },
      error: (err) => {
        this.isSubmitting = false;
        if (err.status === 400 || err.status === 404) {
          this.errorMessage = 'El token de recuperación es inválido o ha expirado.';
        } else {
          this.errorMessage = 'Ocurrió un error al restablecer la contraseña.';
        }
        console.error('Error al restablecer contraseña:', err);
      }
    });
  }
}
