import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UsuarioService } from '../../core/services/usuario.service';
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
  passwordMessage: string = '';
  
  isPerfilLoading: boolean = false;
  isPasswordLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService
  ) {
    this.perfilForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required]
    });

    this.passwordForm = this.fb.group({
      contrasenaActual: ['', Validators.required],
      nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil() {
    this.usuarioService.getPerfil().subscribe({
      next: (data) => {
        this.usuario = data;
        this.perfilForm.patchValue({
          nombre: data.nombre,
          apellido: data.apellido
        });
      },
      error: (err) => console.error('Error al cargar perfil', err)
    });
  }

  onUpdatePerfil() {
    if (this.perfilForm.invalid) return;
    this.isPerfilLoading = true;
    this.perfilMessage = '';

    this.usuarioService.updatePerfil(this.perfilForm.value).subscribe({
      next: (data) => {
        this.usuario = data;
        this.isPerfilLoading = false;
        this.perfilMessage = 'Perfil actualizado correctamente.';
      },
      error: () => {
        this.isPerfilLoading = false;
        this.perfilMessage = 'Error al actualizar el perfil.';
      }
    });
  }

  onChangePassword() {
    if (this.passwordForm.invalid) return;
    this.isPasswordLoading = true;
    this.passwordMessage = '';

    this.usuarioService.changePassword(this.passwordForm.value).subscribe({
      next: () => {
        this.isPasswordLoading = false;
        this.passwordMessage = 'Contraseña actualizada correctamente.';
        this.passwordForm.reset();
      },
      error: () => {
        this.isPasswordLoading = false;
        this.passwordMessage = 'Error al actualizar la contraseña (verifique su contraseña actual).';
      }
    });
  }
}
