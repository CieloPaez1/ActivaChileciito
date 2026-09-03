export enum Rol {
  DEPORTISTA = 'DEPORTISTA',
  ADMINISTRADOR_CANCHA = 'ADMINISTRADOR_CANCHA',
  SUPERUSUARIO = 'SUPERUSUARIO'
}

export enum EstadoUsuario {
  ACTIVO = 'ACTIVO',
  SUSPENDIDO = 'SUSPENDIDO'
}

export interface Usuario {
  idUsuario?: number;
  nombre: string;
  apellido: string;
  email: string;
  estado: EstadoUsuario;
  rol: Rol;
  fechaRegistro?: string;
  perfilDeportivoId?: number;
}

export interface LoginResponse {
  token: string;
  email: string;
  rol: string;
}

export interface LoginRequest {
  email: string;
  contrasena: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  contrasena: string;
  rol: Rol;
}

export interface UpdateUserRequest {
  nombre?: string;
  apellido?: string;
}
