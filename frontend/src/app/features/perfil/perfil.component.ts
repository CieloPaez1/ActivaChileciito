import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { AuthService } from '../../core/services/auth.service';
import { Usuario } from '../../core/models/usuario';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  usuario: Usuario | null = null;
  
  perfilForm: FormGroup;
  passwordForm: FormGroup;
  
  perfilSuccess: string = '';
  perfilError: string = '';
  passwordSuccess: string = '';
  passwordError: string = '';
  deleteError: string = '';
  
  isPerfilLoading: boolean = false;
  isPasswordLoading: boolean = false;
  isInitialLoading: boolean = true;
  isDeleting: boolean = false;

  showCurrentPassword = false;
  showNewPassword = false;
  showConfirmPassword = false;

  // Modal de confirmación para dar de baja
  showDeleteModal: boolean = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {
    this.perfilForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      telefono: ['', [Validators.pattern('^[0-9+ -]*$')]]
    });

    this.passwordForm = this.fb.group({
      passwordActual: ['', [Validators.required]],
      nuevaPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmarPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.cargarPerfil();
  }

  passwordMatchValidator(g: FormGroup) {
    const nueva = g.get('nuevaPassword')?.value;
    const confirm = g.get('confirmarPassword')?.value;
    return nueva === confirm ? null : { mismatch: true };
  }

  cargarPerfil(): void {
    this.isInitialLoading = true;
    this.usuarioService.getPerfil().subscribe({
      next: (data) => {
        this.usuario = data;
        this.perfilForm.patchValue({
          nombre: data.nombre || '',
          apellido: data.apellido || '',
          telefono: data.telefono || ''
        });
        this.isInitialLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar perfil', err);
        this.perfilError = 'No se pudo cargar la información del perfil.';
        this.isInitialLoading = false;
      }
    });
  }

  onUpdatePerfil(): void {
    if (this.perfilForm.invalid) return;
    this.isPerfilLoading = true;
    this.perfilSuccess = '';
    this.perfilError = '';

    const payload = {
      nombre: this.perfilForm.value.nombre,
      apellido: this.perfilForm.value.apellido,
      telefono: this.perfilForm.value.telefono,
      rol: this.usuario?.rol
    };

    this.usuarioService.updatePerfil(payload).subscribe({
      next: (data) => {
        this.usuario = { ...this.usuario, ...data };
        this.isPerfilLoading = false;
        this.perfilSuccess = '¡Tus datos personales fueron actualizados correctamente!';
        setTimeout(() => this.perfilSuccess = '', 4000);
      },
      error: (err) => {
        this.isPerfilLoading = false;
        this.perfilError = err?.error?.error || 'Ocurrió un error al actualizar los datos.';
      }
    });
  }

  onChangePassword(): void {
    if (this.passwordForm.invalid) return;
    this.isPasswordLoading = true;
    this.passwordSuccess = '';
    this.passwordError = '';

    const payload = {
      passwordActual: this.passwordForm.value.passwordActual,
      nuevaPassword: this.passwordForm.value.nuevaPassword
    };

    this.usuarioService.changePassword(payload).subscribe({
      next: () => {
        this.isPasswordLoading = false;
        this.passwordSuccess = '¡Contraseña cambiada con éxito!';
        this.passwordForm.reset();
        setTimeout(() => this.passwordSuccess = '', 4000);
      },
      error: (err) => {
        this.isPasswordLoading = false;
        this.passwordError = err?.error?.error || 'Error al cambiar la contraseña. Verifica tu contraseña actual.';
      }
    });
  }

  openDeleteModal(): void {
    this.deleteError = '';
    this.showDeleteModal = true;
  }

  closeDeleteModal(): void {
    if (!this.isDeleting) {
      this.showDeleteModal = false;
    }
  }

  confirmarBajaCuenta(): void {
    this.isDeleting = true;
    this.deleteError = '';

    this.usuarioService.deleteCuenta().subscribe({
      next: () => {
        this.isDeleting = false;
        this.showDeleteModal = false;
        this.authService.logout();
        this.router.navigate(['/login'], { queryParams: { cuentaEliminada: 'true' } });
      },
      error: (err) => {
        this.isDeleting = false;
        this.deleteError = err?.error?.error || 'No se pudo dar de baja la cuenta. Intenta nuevamente.';
        console.error('Error al dar de baja cuenta:', err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
