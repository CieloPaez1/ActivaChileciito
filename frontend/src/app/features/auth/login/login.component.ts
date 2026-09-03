import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario = '';
  contrasena = '';
  mostrarContrasena = false;

  toggleContrasena() {
    this.mostrarContrasena = !this.mostrarContrasena;
  }

  iniciarSesion() {
    console.log('Iniciando sesión para:', this.usuario);
    // Aquí luego conectaremos con el backend real
  }
}
