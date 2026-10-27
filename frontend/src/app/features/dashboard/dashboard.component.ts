import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ComplejoService } from '../../core/services/complejo.service';
import { Complejo } from '../../core/models/complejo.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName: string = 'Usuario';
  userRole: string = 'DEPORTISTA';

  // Gestión de Complejo (Dueño de Complejo)
  complejo: Complejo | null = null;
  isLoadingComplejo: boolean = false;
  showComplejoModal: boolean = false;
  isSavingComplejo: boolean = false;
  complejoForm: FormGroup;
  complejoMessage: string = '';
  complejoError: string = '';

  prestacionesDisponibles: string[] = [
    'Estacionamiento privado',
    'Vestuarios y Duchas',
    'Buffet / Cantina',
    'Iluminación LED nocturna',
    'Parrillas y Quinchos',
    'Tribunas para público',
    'Wi-Fi gratuito'
  ];

  constructor(
    private authService: AuthService,
    private complejoService: ComplejoService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.complejoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      direccion: ['', [Validators.required, Validators.minLength(5)]],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9+ -]*$')]]
    });
  }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.userName = user.nombre || user.email?.split('@')[0] || 'Deportista';
        this.userRole = user.rol || 'DEPORTISTA';

        if (this.userRole === 'DUENO_DE_COMPLEJO') {
          this.cargarMiComplejo();
        }
      }
    });
  }

  cargarMiComplejo(): void {
    this.isLoadingComplejo = true;
    this.complejoService.getMiComplejo().subscribe({
      next: (data) => {
        this.complejo = data;
        this.isLoadingComplejo = false;
      },
      error: (err) => {
        console.error('Error al cargar complejo', err);
        this.complejo = null;
        this.isLoadingComplejo = false;
      }
    });
  }

  prestacionesSeleccionadas: string[] = [];

  openModalComplejo(): void {
    this.complejoMessage = '';
    this.complejoError = '';

    if (this.complejo) {
      this.complejoForm.patchValue({
        nombre: this.complejo.nombre,
        direccion: this.complejo.direccion,
        telefono: this.complejo.telefono
      });
      this.prestacionesSeleccionadas = this.complejo.prestaciones ? [...this.complejo.prestaciones] : [];
    } else {
      this.complejoForm.reset();
      this.prestacionesSeleccionadas = ['Estacionamiento privado', 'Iluminación LED nocturna'];
    }

    this.showComplejoModal = true;
  }

  closeModalComplejo(): void {
    if (!this.isSavingComplejo) {
      this.showComplejoModal = false;
    }
  }

  togglePrestacion(prestacion: string): void {
    const index = this.prestacionesSeleccionadas.indexOf(prestacion);
    if (index > -1) {
      this.prestacionesSeleccionadas.splice(index, 1);
    } else {
      this.prestacionesSeleccionadas.push(prestacion);
    }
  }

  isPrestacionSelected(prestacion: string): boolean {
    return this.prestacionesSeleccionadas.includes(prestacion);
  }

  guardarComplejo(): void {
    if (this.complejoForm.invalid) return;

    this.isSavingComplejo = true;
    this.complejoMessage = '';
    this.complejoError = '';

    const payload: Complejo = {
      nombre: this.complejoForm.value.nombre,
      direccion: this.complejoForm.value.direccion,
      telefono: this.complejoForm.value.telefono,
      prestaciones: this.prestacionesSeleccionadas
    };

    const request$ = this.complejo 
      ? this.complejoService.modificarMiComplejo(payload)
      : this.complejoService.registrarComplejo(payload);

    request$.subscribe({
      next: (res) => {
        this.complejo = res;
        this.isSavingComplejo = false;
        this.showComplejoModal = false;
      },
      error: (err) => {
        this.isSavingComplejo = false;
        this.complejoError = err?.error?.error || 'Error al guardar los datos del complejo.';
        console.error('Error al guardar complejo:', err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
