import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ComplejoService } from '../../../core/services/complejo.service';
import { Cancha, Complejo, HorarioRequest, RegistrarCanchaRequest } from '../../../core/models/complejo.model';

@Component({
  selector: 'app-canchas-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './canchas-admin.component.html',
  styleUrls: ['./canchas-admin.component.css']
})
export class CanchasAdminComponent implements OnInit {
  miComplejo: Complejo | null = null;
  canchas: Cancha[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';
  successMessage: string = '';

  // Modal Complejo (CU-09.1 y CU-09.2)
  showComplejoModal: boolean = false;
  isSavingComplejo: boolean = false;
  complejoNombre: string = '';
  complejoDireccion: string = '';
  complejoTelefono: string = '';
  complejoPrestaciones: string[] = [];

  readonly prestacionesCatalogo: string[] = [
    'Estacionamiento privado',
    'Iluminacion LED',
    'Vestuarios con duchas',
    'Buffet / Cantina',
    'Parrillas / Quincho',
    'Wi-Fi libre',
    'Seguridad / Camaras',
    'Alquiler de equipamiento'
  ];

  // Modal Cancha (CU-10)
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
    this.cargarDatosIniciales();
  }

  cargarDatosIniciales(): void {
    this.isLoading = true;
    this.complejoService.obtenerMiComplejo().subscribe({
      next: (complejo) => {
        this.miComplejo = complejo || null;
        this.cargarCanchas();
      },
      error: () => {
        this.miComplejo = null;
        this.cargarCanchas();
      }
    });
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
        if (err.status === 400 && err.error?.error?.includes('previamente')) {
          this.errorMessage = 'Aun no tienes un complejo registrado. Registra los datos de tu complejo para comenzar a cargar tus canchas.';
        } else {
          this.errorMessage = err.error?.error || 'No se pudieron cargar las canchas del complejo.';
        }
      }
    });
  }

  // --- Gestión del Complejo (CU-09.1 y CU-09.2) ---

  abrirModalComplejo(): void {
    if (this.miComplejo) {
      this.complejoNombre = this.miComplejo.nombre || '';
      this.complejoDireccion = this.miComplejo.direccion || '';
      this.complejoTelefono = this.miComplejo.telefono || '';
      this.complejoPrestaciones = [...(this.miComplejo.prestaciones || [])];
    } else {
      this.complejoNombre = '';
      this.complejoDireccion = '';
      this.complejoTelefono = '';
      this.complejoPrestaciones = ['Estacionamiento privado', 'Iluminacion LED', 'Vestuarios con duchas'];
    }
    this.showComplejoModal = true;
  }

  cerrarModalComplejo(): void {
    this.showComplejoModal = false;
  }

  tienePrestacion(prestacion: string): boolean {
    return this.complejoPrestaciones.includes(prestacion);
  }

  togglePrestacion(prestacion: string): void {
    const idx = this.complejoPrestaciones.indexOf(prestacion);
    if (idx >= 0) {
      this.complejoPrestaciones.splice(idx, 1);
    } else {
      this.complejoPrestaciones.push(prestacion);
    }
  }

  guardarComplejo(): void {
    if (!this.complejoNombre.trim()) {
      alert('Debes ingresar el nombre comercial del complejo.');
      return;
    }
    if (!this.complejoDireccion.trim()) {
      alert('Debes ingresar la direccion fisica del complejo en Chilecito.');
      return;
    }
    if (!this.complejoTelefono.trim()) {
      alert('Debes ingresar un telefono de contacto.');
      return;
    }

    this.isSavingComplejo = true;
    this.errorMessage = '';
    this.successMessage = '';

    const payload: Partial<Complejo> = {
      nombre: this.complejoNombre.trim(),
      direccion: this.complejoDireccion.trim(),
      telefono: this.complejoTelefono.trim(),
      prestaciones: this.complejoPrestaciones
    };

    this.complejoService.actualizarMiComplejo(payload).subscribe({
      next: (complejoActualizado) => {
        this.isSavingComplejo = false;
        this.miComplejo = complejoActualizado;
        this.successMessage = `¡Datos y prestaciones de "${complejoActualizado.nombre}" guardados exitosamente!`;
        this.cerrarModalComplejo();
        this.cargarCanchas();
      },
      error: (err) => {
        this.isSavingComplejo = false;
        this.errorMessage = err.error?.error || 'Error al guardar los datos del complejo.';
      }
    });
  }

  // --- Gestión de Canchas y Horarios (CU-10 y CU-11) ---

  abrirModal(): void {
    if (!this.miComplejo) {
      alert('Primero debes registrar los datos de tu Complejo Deportivo antes de agregar canchas.');
      this.abrirModalComplejo();
      return;
    }
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

  eliminarCancha(cancha: Cancha): void {
    if (!cancha.id) return;
    if (!confirm(`¿Estás seguro de que deseas dar de baja la cancha "${cancha.nombre}"? Se eliminarán también sus horarios asociados.`)) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.complejoService.eliminarCancha(cancha.id).subscribe({
      next: () => {
        this.successMessage = `Cancha "${cancha.nombre}" eliminada correctamente.`;
        this.cargarCanchas();
      },
      error: (err) => {
        this.errorMessage = err.error?.error || 'No se pudo eliminar la cancha.';
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
