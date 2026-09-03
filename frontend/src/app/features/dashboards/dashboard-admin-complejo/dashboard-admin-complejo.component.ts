import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-admin-complejo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-admin-complejo.component.html',
  styleUrls: ['./dashboard-admin-complejo.component.css']
})
export class DashboardAdminComplejoComponent {
  metricas = {
    ingresosHoy: 125000,
    reservasActivas: 8,
    canchasTotales: 5
  };
  
  reservas = [
    { hora: '18:00', cancha: 'Cancha 1', cliente: 'Cielo Paez', estado: 'Confirmada' },
    { hora: '19:00', cancha: 'Cancha 3', cliente: 'Juan Perez', estado: 'Pendiente' },
    { hora: '20:00', cancha: 'Cancha 2', cliente: 'Maria Gomez', estado: 'Confirmada' }
  ];
}
