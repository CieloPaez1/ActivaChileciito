import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';
import { RolUsuario } from '../../../core/models/rol-usuario.enum';

@Component({
  selector: 'app-lista-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-usuarios.component.html',
  styleUrls: ['./lista-usuarios.component.css']
})
export class ListaUsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  filtroRol: RolUsuario = RolUsuario.DEPORTISTA;
  roles = Object.values(RolUsuario);
  isLoading = false;
  error = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.isLoading = true;
    this.error = '';
    
    this.usuarioService.obtenerUsuariosPorRol(this.filtroRol).subscribe({
      next: (data) => {
        this.usuarios = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'No se pudieron cargar los usuarios.';
        this.isLoading = false;
      }
    });
  }

  onRolChange() {
    this.cargarUsuarios();
  }

  eliminarUsuario(id: number | undefined) {
    if (!id) return;
    
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.usuarioService.eliminarUsuario(id).subscribe({
        next: () => {
          this.usuarios = this.usuarios.filter(u => u.id !== id);
        },
        error: () => {
          alert('Hubo un error al eliminar el usuario.');
        }
      });
    }
  }
}
