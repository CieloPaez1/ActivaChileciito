export interface HorarioDisponibilidad {
  id?: number;
  diaSemana: number; // 1 = Lunes, 7 = Domingo
  horaInicio: string; // "18:00:00"
  horaFin: string;    // "23:00:00"
}

export interface Cancha {
  id?: number;
  complejoId: number;
  nombre: string;
  deporte: string;
  tipoSuelo: string;
  precioTurno: number;
  activa: boolean;
  horarios: HorarioDisponibilidad[];
}

export interface HorarioRequest {
  diaSemana: number;
  horaInicio: string;
  horaFin: string;
}

export interface RegistrarCanchaRequest {
  complejoId?: number;
  nombre: string;
  deporte: string;
  tipoSuelo: string;
  precioTurno: number;
  horarios: HorarioRequest[];
}

export interface Complejo {
  id?: number;
  usuarioId?: number;
  nombre: string;
  direccion: string;
  telefono: string;
  prestaciones: string[];
}
