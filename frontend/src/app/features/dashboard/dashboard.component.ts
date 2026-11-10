import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UsuarioService } from '../../core/services/usuario.service';
import { ComplejoService } from '../../core/services/complejo.service';
import { Cancha, Complejo, HorarioRequest, ModificarCanchaRequest, RegistrarCanchaRequest } from '../../core/models/complejo.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName: string = 'Propietario';
  userRole: string = 'DUENO_DE_COMPLEJO';
  activeTab: 'complejo' | 'canchas' | 'reservas' | 'metricas' | 'partidos' = 'canchas';

  // Datos del Complejo
  miComplejo: Complejo | null = null;
  canchas: Cancha[] = [];
  isLoading: boolean = true;
  successMessage: string = '';
  errorMessage: string = '';

  // Formulario Complejo
  complejoNombre: string = '';
  complejoDireccion: string = '';
  complejoTelefono: string = '';
  complejoDescripcion: string = '';
  complejoPrestaciones: string[] = [];
  horaApertura: string = '08:00';
  horaCierre: string = '00:00';
  diasAtencion: string[] = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado', 'Domingo'];
  diaSeleccionadoAtencion: string = 'Lunes';
  isSavingComplejo: boolean = false;
  showComplejoModal: boolean = false;

  readonly prestacionesCatalogo: string[] = [
    'Estacionamiento privado',
    'Iluminacion LED',
    'Vestuarios con duchas',
    'Buffet / Cantina',
    'Parrillas / Quincho',
    'Wi-Fi libre',
    'Seguridad / Camaras',
    'Techada',
    'Alquiler de equipamiento'
  ];

  // Modal Cancha (Registrar y Modificar)
  showCanchaModal: boolean = false;
  modoEdicionCancha: boolean = false;
  canchaEditandoId: number | null = null;
  isSavingCancha: boolean = false;

  canchaNombre: string = '';
  canchaDeporte: string = 'Futbol 5';
  canchaTipoSuelo: string = 'Cesped Sintetico';
  canchaPrecioTurno: number | null = null;

  nuevoDiaSemana: number = 1;
  nuevaHoraInicio: string = '18:00';
  nuevaHoraFin: string = '23:00';
  horariosAgregados: HorarioRequest[] = [];

  readonly diasSemanaMap: { [key: number]: string } = {
    1: 'Lunes', 2: 'Martes', 3: 'Miercoles', 4: 'Jueves',
    5: 'Viernes', 6: 'Sabado', 7: 'Domingo'
  };

  readonly deportesDisponibles: string[] = [
    'Futbol 5', 'Futbol 7', 'Futbol 11', 'Padel', 'Tenis', 'Basquet'
  ];

  readonly tiposSueloDisponibles: string[] = [
    'Cesped Sintetico', 'Cristal / Alfombra', 'Polvo de Ladrillo',
    'Cemento Pulido', 'Parquet'
  ];

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private complejoService: ComplejoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // 1. Obtener desde AuthService
    const rolActual = this.authService.getRole();
    if (rolActual) {
      this.userRole = rolActual;
    }

    this.authService.currentUser$.subscribe(user => {
      if (user) {
        if (user.rol) this.userRole = user.rol;
        if (user.nombre) this.userName = user.nombre;
        else if (user.email) this.userName = user.email.split('@')[0];
      }
    });

    // 2. Consultar perfil en el backend para asegurar nombre y rol real
    this.usuarioService.getPerfil().subscribe({
      next: (perfil) => {
        if (perfil) {
          if (perfil.rol) this.userRole = perfil.rol;
          if (perfil.nombre) this.userName = perfil.nombre;
          if (this.userRole === 'DUENO_DE_COMPLEJO') {
            this.cargarDatosComplejoYCanchas();
          } else {
            this.isLoading = false;
          }
        }
      },
      error: () => {
        // En caso de que falle perfil/me, continuar con el rol que tenemos
        if (this.userRole === 'DUENO_DE_COMPLEJO') {
          this.cargarDatosComplejoYCanchas();
        } else {
          this.isLoading = false;
        }
      }
    });
  }

  cambiarTab(tab: 'complejo' | 'canchas' | 'reservas' | 'metricas' | 'partidos'): void {
    this.activeTab = tab;
    this.successMessage = '';
    this.errorMessage = '';
  }

  cargarDatosComplejoYCanchas(): void {
    this.isLoading = true;
    this.complejoService.obtenerMiComplejo().subscribe({
      next: (complejo) => {
        this.miComplejo = complejo || null;
        if (this.miComplejo) {
          this.poblarFormularioComplejo(this.miComplejo);
          this.activeTab = 'canchas';
        } else {
          this.activeTab = 'complejo';
        }
        this.cargarCanchas();
      },
      error: () => {
        this.miComplejo = null;
        this.activeTab = 'complejo';
        this.cargarCanchas();
      }
    });
  }

  cargarCanchas(): void {
    this.complejoService.obtenerMisCanchas().subscribe({
      next: (data) => {
        this.canchas = data || [];
        this.isLoading = false;
      },
      error: () => {
        this.canchas = [];
        this.isLoading = false;
      }
    });
  }

  poblarFormularioComplejo(complejo: Complejo): void {
    this.complejoNombre = complejo.nombre || '';
    this.complejoDireccion = complejo.direccion || '';
    this.complejoTelefono = complejo.telefono || '';
    this.complejoPrestaciones = [...(complejo.prestaciones || [])];
    this.complejoDescripcion = complejo.prestaciones && complejo.prestaciones.length > 0
      ? `Complejo deportivo con canchas e instalaciones. Servicios: ${complejo.prestaciones.join(', ')}.`
      : 'Complejo deportivo con canchas e instalaciones habilitadas.';
  }

  // --- Mi Complejo (CU-11.1 y CU-11.2) ---

  abrirModalRegistroComplejo(): void {
    this.complejoNombre = '';
    this.complejoDireccion = '';
    this.complejoTelefono = '';
    this.complejoPrestaciones = ['Estacionamiento privado', 'Iluminacion LED', 'Vestuarios con duchas'];
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

  guardarDatosComplejo(): void {
    if (!this.complejoNombre.trim()) {
      alert('Debes ingresar el nombre comercial del complejo.');
      return;
    }
    if (!this.complejoDireccion.trim()) {
      alert('Debes ingresar la dirección física del complejo.');
      return;
    }
    if (!this.complejoTelefono.trim()) {
      alert('Debes ingresar un teléfono de contacto.');
      return;
    }

    this.isSavingComplejo = true;
    this.successMessage = '';
    this.errorMessage = '';

    const payload: Partial<Complejo> = {
      nombre: this.complejoNombre.trim(),
      direccion: this.complejoDireccion.trim(),
      telefono: this.complejoTelefono.trim(),
      prestaciones: this.complejoPrestaciones
    };

    const peticion = this.miComplejo
      ? this.complejoService.actualizarMiComplejo(payload)
      : this.complejoService.registrarComplejo(payload);

    peticion.subscribe({
      next: (complejoGuardado) => {
        this.isSavingComplejo = false;
        this.miComplejo = complejoGuardado;
        this.poblarFormularioComplejo(complejoGuardado);
        this.cerrarModalComplejo();
        this.successMessage = '¡Datos de tu complejo guardados exitosamente!';
        this.cargarCanchas();
      },
      error: (err) => {
        this.isSavingComplejo = false;
        this.errorMessage = err.error?.error || 'Error al guardar los datos del complejo.';
      }
    });
  }

  // --- Mis Canchas (CU-12.1, CU-12.2, CU-12.3) ---

  abrirModalNuevaCancha(): void {
    if (!this.miComplejo) {
      alert('Primero debes registrar los datos de tu complejo.');
      this.abrirModalRegistroComplejo();
      return;
    }
    this.modoEdicionCancha = false;
    this.canchaEditandoId = null;
    this.limpiarFormularioCancha();
    this.showCanchaModal = true;
  }

  abrirModalEditarCancha(cancha: Cancha): void {
    this.modoEdicionCancha = true;
    this.canchaEditandoId = cancha.id ?? null;
    this.canchaNombre = cancha.nombre;
    this.canchaDeporte = cancha.deporte;
    this.canchaTipoSuelo = cancha.tipoSuelo;
    this.canchaPrecioTurno = cancha.precioTurno;
    this.horariosAgregados = (cancha.horarios || []).map(h => ({
      diaSemana: h.diaSemana,
      horaInicio: this.formatearHora(h.horaInicio),
      horaFin: this.formatearHora(h.horaFin)
    }));
    this.showCanchaModal = true;
  }

  cerrarModalCancha(): void {
    this.showCanchaModal = false;
    this.limpiarFormularioCancha();
  }

  agregarFranjaHoraria(): void {
    if (!this.nuevaHoraInicio || !this.nuevaHoraFin) {
      alert('Debes indicar hora de inicio y de fin.');
      return;
    }
    if (this.nuevaHoraInicio >= this.nuevaHoraFin) {
      alert('La hora de inicio debe ser anterior a la de fin.');
      return;
    }
    const inicio = this.nuevaHoraInicio.length === 5 ? `${this.nuevaHoraInicio}:00` : this.nuevaHoraInicio;
    const fin = this.nuevaHoraFin.length === 5 ? `${this.nuevaHoraFin}:00` : this.nuevaHoraFin;

    this.horariosAgregados.push({
      diaSemana: Number(this.nuevoDiaSemana),
      horaInicio: inicio,
      horaFin: fin
    });
  }

  eliminarFranja(index: number): void {
    this.horariosAgregados.splice(index, 1);
  }

  guardarCancha(): void {
    if (!this.canchaNombre.trim()) {
      alert('Ingresa el nombre o identificador de la cancha.');
      return;
    }
    if (!this.canchaPrecioTurno || this.canchaPrecioTurno <= 0) {
      alert('Ingresa un precio válido por turno.');
      return;
    }

    this.isSavingCancha = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.modoEdicionCancha && this.canchaEditandoId) {
      const payload: ModificarCanchaRequest = {
        nombre: this.canchaNombre.trim(),
        deporte: this.canchaDeporte,
        tipoSuelo: this.canchaTipoSuelo,
        precioTurno: this.canchaPrecioTurno,
        horarios: this.horariosAgregados
      };
      this.complejoService.modificarCancha(this.canchaEditandoId, payload).subscribe({
        next: (canchaActualizada) => {
          this.isSavingCancha = false;
          this.successMessage = `Cancha "${canchaActualizada.nombre}" actualizada correctamente.`;
          this.cerrarModalCancha();
          this.cargarCanchas();
        },
        error: (err) => {
          this.isSavingCancha = false;
          this.errorMessage = err.error?.error || 'Error al modificar la cancha.';
        }
      });
    } else {
      const payload: RegistrarCanchaRequest = {
        nombre: this.canchaNombre.trim(),
        deporte: this.canchaDeporte,
        tipoSuelo: this.canchaTipoSuelo,
        precioTurno: this.canchaPrecioTurno,
        horarios: this.horariosAgregados
      };
      this.complejoService.registrarCancha(payload).subscribe({
        next: (nuevaCancha) => {
          this.isSavingCancha = false;
          this.successMessage = `Cancha "${nuevaCancha.nombre}" registrada correctamente.`;
          this.cerrarModalCancha();
          this.cargarCanchas();
        },
        error: (err) => {
          this.isSavingCancha = false;
          this.errorMessage = err.error?.error || 'Error al guardar la cancha.';
        }
      });
    }
  }

  toggleEstadoCancha(cancha: Cancha): void {
    if (!cancha.id) return;
    const nuevoEstado = !cancha.activa;
    const payload: ModificarCanchaRequest = {
      activa: nuevoEstado
    };
    this.complejoService.modificarCancha(cancha.id, payload).subscribe({
      next: (actualizada) => {
        cancha.activa = actualizada.activa;
        this.successMessage = `Cancha "${cancha.nombre}" ahora está ${actualizada.activa ? 'Activa' : 'En mantenimiento'}.`;
      },
      error: () => {
        this.errorMessage = 'No se pudo cambiar el estado de la cancha.';
      }
    });
  }

  eliminarCancha(cancha: Cancha): void {
    if (!cancha.id) return;
    if (!confirm(`¿Eliminar la cancha "${cancha.nombre}" y todos sus horarios?`)) {
      return;
    }
    this.complejoService.eliminarCancha(cancha.id).subscribe({
      next: () => {
        this.successMessage = `Cancha "${cancha.nombre}" eliminada correctamente.`;
        this.cargarCanchas();
      },
      error: () => {
        this.errorMessage = 'No se pudo eliminar la cancha.';
      }
    });
  }

  get canchasOperativasCount(): number {
    return this.canchas.filter(c => c.activa).length;
  }

  getDeporteBadgeClass(deporte: string): string {
    const dep = (deporte || '').toLowerCase();
    if (dep.includes('futbol')) return 'badge-futbol';
    if (dep.includes('tenis')) return 'badge-tenis';
    if (dep.includes('padel')) return 'badge-padel';
    if (dep.includes('basquet')) return 'badge-basquet';
    return 'badge-default';
  }

  getNombreDia(dia: number): string {
    return this.diasSemanaMap[dia] || `Día ${dia}`;
  }

  formatearHora(hora: string): string {
    if (!hora) return '';
    return hora.substring(0, 5);
  }

  private limpiarFormularioCancha(): void {
    this.canchaNombre = '';
    this.canchaDeporte = 'Futbol 5';
    this.canchaTipoSuelo = 'Cesped Sintetico';
    this.canchaPrecioTurno = null;
    this.nuevoDiaSemana = 1;
    this.nuevaHoraInicio = '18:00';
    this.nuevaHoraFin = '23:00';
    this.horariosAgregados = [];
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
