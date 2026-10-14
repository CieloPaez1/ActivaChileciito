import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-complete-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="d-flex align-items-center justify-content-center min-vh-100" style="background-color: #0b1c14;">
      <div class="card p-4 shadow-lg" style="max-width: 400px; width: 100%; border-radius: 12px;">
        <div class="text-center mb-4">
          <img src="/images/logo.png" alt="Logo" style="height: 50px;">
          <h4 class="mt-3 text-dark fw-bold">Casi listo!</h4>
          <p class="text-muted" style="font-size: 0.9rem;">Necesitamos un par de datos mas para terminar de configurar tu cuenta.</p>
        </div>
        
        <form [formGroup]="profileForm" (ngSubmit)="onSubmit()">
          
          <div class="mb-3">
            <label class="form-label text-dark fw-bold" style="font-size: 0.85rem;">Apellido</label>
            <input type="text" class="form-control" formControlName="apellido" placeholder="Tu apellido">
          </div>

          <div class="mb-3">
            <label class="form-label text-dark fw-bold" style="font-size: 0.85rem;">Telefono</label>
            <input type="tel" class="form-control" formControlName="telefono" placeholder="Ej: 3825123456">
            <div *ngIf="profileForm.get('telefono')?.hasError('pattern')" class="text-danger mt-1" style="font-size: 0.8rem;">
              Solo debe contener numeros.
            </div>
          </div>

          <div class="mb-4">
            <label class="form-label text-dark fw-bold" style="font-size: 0.85rem;">Que rol vas a tener?</label>
            <select class="form-select" formControlName="rol">
              <option value="DEPORTISTA">Deportista (Buscar canchas y partidos)</option>
              <option value="DUENO_DE_COMPLEJO">Dueno de Complejo (Administrar mis canchas)</option>
            </select>
          </div>

          <button type="submit" class="btn w-100 fw-bold" style="background-color: #a4e613; color: #0b1c14; border-radius: 8px;" [disabled]="profileForm.invalid || isLoading">
            <span *ngIf="isLoading" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
            GUARDAR Y ENTRAR
          </button>
        </form>
      </div>
    </div>
  `
})
export class CompleteProfileComponent implements OnInit {
  profileForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient
  ) {
    this.profileForm = this.fb.group({
      apellido: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      rol: ['DEPORTISTA', Validators.required]
    });
  }

  ngOnInit(): void {
    // Check if we already have some profile data to pre-fill
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    this.http.get<any>('http://localhost:8080/api/perfil/me', { headers }).subscribe({
      next: (profile) => {
        if (profile.apellido) {
          this.profileForm.patchValue({ apellido: profile.apellido });
        }
      },
      error: () => {
        // Ignorar si falla, igual le pedimos los datos
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;

    this.isLoading = true;
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    
    // Asumimos que podemos enviar estos datos a /api/perfil/me
    // Primero, traemos el perfil actual, y lo actualizamos completo
    this.http.get<any>('http://localhost:8080/api/perfil/me', { headers }).subscribe({
      next: (profile) => {
        const payload = {
          nombre: profile.nombre,
          apellido: this.profileForm.value.apellido,
          telefono: this.profileForm.value.telefono,
          rol: this.profileForm.value.rol
        };
        
        // Llamada a un endpoint especial o al de modificar perfil
        this.http.put('http://localhost:8080/api/perfil/me', payload, { headers }).subscribe({
          next: () => {
            // Faltara actualizar el rol en backend
            this.router.navigate(['/dashboard']);
          },
          error: (err) => {
            console.error('Error actualizando perfil', err);
            this.isLoading = false;
          }
        });
      },
      error: () => this.isLoading = false
    });
  }
}