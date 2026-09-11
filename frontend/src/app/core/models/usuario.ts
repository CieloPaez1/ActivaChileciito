export enum Rol {
  CLIENTE = 'CLIENTE',
  ADMIN_COMPLEJO = 'ADMIN_COMPLEJO'
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
  password: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
  telefono?: string;
  rol: Rol;
}

export interface UpdateUserRequest {
  nombre?: string;
  apellido?: string;
}
