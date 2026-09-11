import { RolUsuario } from './rol-usuario.enum';

export interface Usuario {
  id?: number;
  nombre: string;
  apellido: string;
  email: string;
  password?: string;
  rol: RolUsuario;
  telefono: string;
  activo?: boolean;
}
