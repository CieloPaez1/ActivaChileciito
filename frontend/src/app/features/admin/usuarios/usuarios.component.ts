import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../core/services/admin.service';
import { Usuario, Rol, EstadoUsuario } from '../../../core/models/usuario';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {
  usuarios: Usuario[] = [];
  isLoading = true;
  roles = Object.values(Rol);

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.isLoading = true;
    this.adminService.getUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error cargando usuarios', err);
        this.isLoading = false;
      }
    });
  }

  activarUsuario(id: number | undefined) {
    if (!id) return;
    this.adminService.activarUsuario(id).subscribe(() => this.cargarUsuarios());
  }

  suspenderUsuario(id: number | undefined) {
    if (!id) return;
    this.adminService.suspenderUsuario(id).subscribe(() => this.cargarUsuarios());
  }

  cambiarRol(id: number | undefined, event: Event) {
    if (!id) return;
    const selectElement = event.target as HTMLSelectElement;
    const nuevoRol = selectElement.value;
    
    this.adminService.cambiarRol(id, nuevoRol).subscribe({
      next: () => {
        this.cargarUsuarios();
      },
      error: () => {
        alert('Error al cambiar rol');
        this.cargarUsuarios(); // Recargar para deshacer el cambio visual
      }
    });
  }
}
