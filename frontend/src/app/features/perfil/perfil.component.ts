import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { AuthService } from '../../core/services/auth.service';
import { Usuario } from '../../core/models/usuario';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  usuario: Usuario | null = null;
  
  perfilForm: FormGroup;
  passwordForm: FormGroup;
  
  perfilMessage: string = '';
  perfilError: string = '';
  passwordMessage: string = '';
  passwordError: string = '';
  
  isPerfilLoading: boolean = false;
  isPasswordLoading: boolean = false;
  isDeleteLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {
    this.perfilForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      telefono: ['', [Validators.pattern('^[0-9\\s-]*$')]]
    });

    this.passwordForm = this.fb.group({
      passwordActual: ['', Validators.required],
      nuevaPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmarNuevaPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('nuevaPassword')?.value === g.get('confirmarNuevaPassword')?.value
      ? null : { mismatch: true };
  }

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.usuarioService.getPerfil().subscribe({
      next: (data) => {
        this.usuario = data;
        this.perfilForm.patchValue({
          nombre: data.nombre,
          apellido: data.apellido,
          telefono: data.telefono || ''
        });
      },
      error: (err) => {
        console.error('Error al cargar perfil', err);
        this.perfilError = 'No se pudo cargar la información del perfil.';
      }
    });
  }

  onUpdatePerfil(): void {
    if (this.perfilForm.invalid) return;
    this.isPerfilLoading = true;
    this.perfilMessage = '';
    this.perfilError = '';

    const payload = {
      nombre: this.perfilForm.value.nombre.trim(),
      apellido: this.perfilForm.value.apellido.trim(),
      telefono: this.perfilForm.value.telefono ? this.perfilForm.value.telefono.trim() : ''
    };

    this.usuarioService.updatePerfil(payload).subscribe({
      next: (data) => {
        this.usuario = data;
        this.isPerfilLoading = false;
        this.perfilMessage = '¡Datos personales actualizados exitosamente!';
      },
      error: (err) => {
        this.isPerfilLoading = false;
        this.perfilError = err.error?.error || 'Error al actualizar el perfil.';
      }
    });
  }

  onChangePassword(): void {
    if (this.passwordForm.invalid) return;
    this.isPasswordLoading = true;
    this.passwordMessage = '';
    this.passwordError = '';

    const payload = {
      passwordActual: this.passwordForm.value.passwordActual,
      nuevaPassword: this.passwordForm.value.nuevaPassword
    };

    this.usuarioService.changePassword(payload).subscribe({
      next: () => {
        this.isPasswordLoading = false;
        this.passwordMessage = '¡Contraseña actualizada correctamente!';
        this.passwordForm.reset();
      },
      error: (err) => {
        this.isPasswordLoading = false;
        this.passwordError = err.error?.error || 'Error al cambiar la contraseña. Verifique su clave actual.';
      }
    });
  }

  onEliminarCuenta(): void {
    const confirmacion = confirm(
      '¿Estás seguro de que deseas dar de baja tu cuenta? Esta acción revocará inmediatamente tu acceso al sistema.'
    );
    if (!confirmacion) return;

    this.isDeleteLoading = true;
    this.usuarioService.eliminarCuenta().subscribe({
      next: () => {
        this.isDeleteLoading = false;
        alert('Tu cuenta ha sido dada de baja correctamente.');
        this.authService.logout();
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isDeleteLoading = false;
        alert(err.error?.error || 'Ocurrió un error al intentar dar de baja la cuenta.');
      }
    });
  }
}
