import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard-super-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-super-admin.component.html',
  styleUrls: ['./dashboard-super-admin.component.css']
})
export class DashboardSuperAdminComponent {
  globalStats = {
    totalUsuarios: 1450,
    totalComplejos: 32,
    totalCanchas: 156,
    reservasMes: 3420
  };

  ultimosComplejos = [
    { nombre: 'El Predio', ciudad: 'Chilecito', estado: 'Activo' },
    { nombre: 'Club Atlético', ciudad: 'Chilecito', estado: 'Pendiente' },
    { nombre: 'Canchas del Sur', ciudad: 'Famatina', estado: 'Activo' }
  ];
}
