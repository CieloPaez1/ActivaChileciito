import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ComplejoService } from '../../../core/services/complejo.service';
import { Cancha, HorarioRequest, RegistrarCanchaRequest } from '../../../core/models/complejo.model';

@Component({
  selector: 'app-canchas-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './canchas-admin.component.html',
  styleUrls: ['./canchas-admin.component.css']
})
export class CanchasAdminComponent implements OnInit {
  canchas: Cancha[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';
  successMessage: string = '';

  // Modal / Form state
  showModal: boolean = false;
  isSaving: boolean = false;

  // New Court model
  nombre: string = '';
  deporte: string = 'Futbol 5';
  tipoSuelo: string = 'Cesped Sintetico';
  precioTurno: number | null = null;

  // Schedule slot builder
  nuevoDiaSemana: number = 1;
  nuevaHoraInicio: string = '18:00';
  nuevaHoraFin: string = '23:00';
  horariosAgregados: HorarioRequest[] = [];

  readonly diasSemanaMap: { [key: number]: string } = {
    1: 'Lunes',
    2: 'Martes',
    3: 'Miercoles',
    4: 'Jueves',
    5: 'Viernes',
    6: 'Sabado',
    7: 'Domingo'
  };

  readonly deportesDisponibles: string[] = [
    'Futbol 5',
    'Futbol 7',
    'Futbol 11',
    'Padel',
    'Tenis',
    'Basquet'
  ];

  readonly tiposSueloDisponibles: string[] = [
    'Cesped Sintetico',
    'Cristal / Alfombra',
    'Polvo de Ladrillo',
    'Cemento Pulido',
    'Parquet'
  ];

  constructor(
    private complejoService: ComplejoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCanchas();
  }

  cargarCanchas(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.complejoService.obtenerMisCanchas().subscribe({
      next: (data) => {
        this.canchas = data || [];
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        // If error indicates no complex yet, provide a clear actionable message
        if (err.status === 400 && err.error?.error?.includes('previamente')) {
          this.errorMessage = 'Aun no tienes un complejo registrado. Registra tu complejo para comenzar a cargar tus canchas.';
        } else {
          this.errorMessage = err.error?.error || 'No se pudieron cargar las canchas del complejo.';
        }
      }
    });
  }

  abrirModal(): void {
    this.limpiarFormulario();
    this.showModal = true;
  }

  cerrarModal(): void {
    this.showModal = false;
    this.limpiarFormulario();
  }

  agregarFranjaHoraria(): void {
    if (!this.nuevaHoraInicio || !this.nuevaHoraFin) {
      alert('Debes indicar tanto la hora de inicio como la de fin.');
      return;
    }

    if (this.nuevaHoraInicio >= this.nuevaHoraFin) {
      alert('La hora de inicio debe ser estrictamente anterior a la hora de fin.');
      return;
    }

    // Format to HH:mm:ss if not already
    const formattedInicio = this.nuevaHoraInicio.length === 5 ? `${this.nuevaHoraInicio}:00` : this.nuevaHoraInicio;
    const formattedFin = this.nuevaHoraFin.length === 5 ? `${this.nuevaHoraFin}:00` : this.nuevaHoraFin;

    this.horariosAgregados.push({
      diaSemana: Number(this.nuevoDiaSemana),
      horaInicio: formattedInicio,
      horaFin: formattedFin
    });
  }

  eliminarFranja(index: number): void {
    this.horariosAgregados.splice(index, 1);
  }

  guardarCancha(): void {
    if (!this.nombre.trim()) {
      alert('Debes ingresar el nombre de la cancha.');
      return;
    }
    if (!this.precioTurno || this.precioTurno <= 0) {
      alert('Debes ingresar un precio por turno valido.');
      return;
    }
    if (this.horariosAgregados.length === 0) {
      if (!confirm('No has asignado ninguna franja horaria. ¿Deseas guardar la cancha sin horarios por ahora?')) {
        return;
      }
    }

    const payload: RegistrarCanchaRequest = {
      nombre: this.nombre.trim(),
      deporte: this.deporte,
      tipoSuelo: this.tipoSuelo,
      precioTurno: this.precioTurno,
      horarios: this.horariosAgregados
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.complejoService.registrarCancha(payload).subscribe({
      next: (nuevaCancha) => {
        this.isSaving = false;
        this.successMessage = `¡Cancha "${nuevaCancha.nombre}" registrada exitosamente!`;
        this.cerrarModal();
        this.cargarCanchas();
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.error || 'Error al guardar la cancha.';
      }
    });
  }

  getNombreDia(dia: number): string {
    return this.diasSemanaMap[dia] || `Dia ${dia}`;
  }

  formatearHora(hora: string): string {
    if (!hora) return '';
    return hora.substring(0, 5);
  }

  private limpiarFormulario(): void {
    this.nombre = '';
    this.deporte = 'Futbol 5';
    this.tipoSuelo = 'Cesped Sintetico';
    this.precioTurno = null;
    this.nuevoDiaSemana = 1;
    this.nuevaHoraInicio = '18:00';
    this.nuevaHoraFin = '23:00';
    this.horariosAgregados = [];
  }
}
