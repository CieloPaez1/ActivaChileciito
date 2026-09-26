import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotForm: FormGroup;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.forgotForm.invalid) return;

    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const email = this.forgotForm.value.email;

    this.authService.requestPasswordReset(email).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Si el correo electrónico está registrado, se enviará un enlace para restablecer tu contraseña.';
        this.forgotForm.reset();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = 'Ocurrió un error al procesar la solicitud. Por favor, intenta de nuevo.';
        console.error('Error al solicitar recuperacion:', err);
      }
    });
  }
}
