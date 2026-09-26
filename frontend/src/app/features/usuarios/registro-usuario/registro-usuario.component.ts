import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { RolUsuario } from '../../../core/models/rol-usuario.enum';

@Component({
  selector: 'app-registro-usuario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registro-usuario.component.html',
  styleUrls: ['./registro-usuario.component.css']
})
export class RegistroUsuarioComponent {
  registroForm: FormGroup;
  roles = Object.values(RolUsuario);
  mensaje: string = '';
  error: string = '';
  isSubmitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    this.registroForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rol: [RolUsuario.DEPORTISTA, Validators.required],
      telefono: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.registroForm.valid) {
      this.isSubmitting = true;
      this.mensaje = '';
      this.error = '';
      
      this.usuarioService.registrarUsuario(this.registroForm.value).subscribe({
        next: (res) => {
          this.mensaje = '¡Usuario creado con éxito!';
          this.registroForm.reset({ rol: RolUsuario.DEPORTISTA });
          this.isSubmitting = false;
          // Redirigir después de 2 segundos a la lista (o lo que queramos)
          setTimeout(() => this.router.navigate(['/usuarios']), 2000);
        },
        error: (err) => {
          this.error = 'Error al crear el usuario. Por favor verifica los datos.';
          this.isSubmitting = false;
        }
      });
    }
  }
}
