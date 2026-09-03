import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-deportista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-deportista.component.html',
  styleUrls: ['./dashboard-deportista.component.css']
})
export class DashboardDeportistaComponent {
  canchas = [
    { nombre: 'Cancha 1 (Sintético)', complejo: 'El Predio', precio: 15000, disponible: true },
    { nombre: 'Cancha 2 (Cemento)', complejo: 'Club Atlético', precio: 12000, disponible: false },
    { nombre: 'Cancha 3 (Techada)', complejo: 'El Predio', precio: 20000, disponible: true }
  ];
}
