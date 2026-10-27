import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ComplejoService } from '../../../core/services/complejo.service';
import { Cancha, Complejo } from '../../../core/models/complejo.model';

@Component({
  selector: 'app-explorar-complejos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="explorar-wrapper">
      <div class="glow-orb orb-1"></div>
      <div class="glow-orb orb-2"></div>

      <nav class="glass-nav">
        <div class="container d-flex justify-content-between align-items-center py-3">
          <div class="d-flex align-items-center gap-3">
            <img src="/images/logo.png" alt="Activa Chilecito" class="nav-logo">
            <span class="text-white fw-bold fs-5">COMPLEJOS Y CANCHAS EN CHILECITO</span>
          </div>
          <a routerLink="/dashboard" class="btn-glass-secondary">
            &larr; Volver al Panel
          </a>
        </div>
      </nav>

      <div class="container mt-4 pb-5 position-relative z-index-1">
        <!-- Hero Search & Filter -->
        <div class="hero-card mb-4">
          <div class="row align-items-center g-3">
            <div class="col-lg-6">
              <h2 class="text-white fw-bold mb-1">Explora Espacios Deportivos</h2>
              <p class="text-white-50 mb-0">Consulta las instalaciones, comodidades y horarios semanales disponibles en la ciudad.</p>
            </div>
            <div class="col-lg-6">
              <div class="d-flex gap-2 flex-wrap justify-content-lg-end">
                <button
                  *ngFor="let dep of filtrosDeporte"
                  class="filter-chip"
                  [class.active]="deporteSeleccionado === dep"
                  (click)="seleccionarFiltroDeporte(dep)">
                  {{ dep }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <div *ngIf="isLoading" class="text-center py-5">
          <div class="spinner-border text-brand-lime" role="status"></div>
          <p class="text-white-50 mt-3">Buscando complejos deportivos en Chilecito...</p>
        </div>

        <div *ngIf="!isLoading && complejosFiltrados().length === 0" class="empty-card text-center py-5">
          <h4 class="text-white fw-bold">No se encontraron canchas para el filtro seleccionado</h4>
          <p class="text-white-50">Prueba seleccionando "Todos" para ver la oferta completa de complejos.</p>
        </div>

        <!-- Lista de Complejos -->
        <div *ngFor="let item of complejosFiltrados()" class="complejo-showcase-card mb-4">
          <div class="complejo-header d-flex justify-content-between align-items-start flex-wrap gap-3">
            <div>
              <span class="badge-verified">COMPLEJO HABILITADO</span>
              <h3 class="text-white fw-bold mt-2 mb-1">{{ item.complejo.nombre }}</h3>
              <div class="d-flex flex-wrap gap-3 text-white-50 small">
                <span>📍 {{ item.complejo.direccion }}</span>
                <span>📞 {{ item.complejo.telefono }}</span>
              </div>
            </div>
            <div class="d-flex flex-wrap gap-2">
              <span *ngFor="let p of item.complejo.prestaciones" class="prestacion-pill">
                &#10003; {{ p }}
              </span>
            </div>
          </div>

          <hr class="divider my-3">

          <h5 class="text-white-50 fs-6 fw-bold mb-3">
            Canchas Disponibles ({{ item.canchas.length }})
          </h5>

          <div *ngIf="item.canchas.length === 0" class="text-white-50 small fst-italic">
            Este complejo aún no ha publicado canchas activas.
          </div>

          <div class="row g-3" *ngIf="item.canchas.length > 0">
            <div *ngFor="let cancha of item.canchas" class="col-md-6 col-lg-4">
              <div class="cancha-inner-card">
                <div class="d-flex justify-content-between align-items-start mb-2">
                  <div>
                    <span class="sport-tag">{{ cancha.deporte }}</span>
                    <h4 class="text-white fw-bold fs-5 mt-1 mb-0">{{ cancha.nombre }}</h4>
                  </div>
                  <span class="price-tag">\${{ cancha.precioTurno | number:'1.0-0' }}</span>
                </div>
                <p class="text-white-50 small mb-2">Superficie: <strong class="text-white">{{ cancha.tipoSuelo }}</strong></p>

                <div class="horarios-box mt-2">
                  <span class="d-block text-white-50 small mb-1 fw-semibold">Horarios de Atención:</span>
                  <div *ngIf="cancha.horarios && cancha.horarios.length > 0; else sinHorarios" class="d-flex flex-wrap gap-1">
                    <span *ngFor="let h of cancha.horarios" class="slot-badge">
                      {{ getNombreDia(h.diaSemana) }}: {{ formatearHora(h.horaInicio) }}-{{ formatearHora(h.horaFin) }}
                    </span>
                  </div>
                  <ng-template #sinHorarios>
                    <span class="text-white-50 small fst-italic">Consultar disponibilidad telefónica</span>
                  </ng-template>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  `,
  styles: [`
    .explorar-wrapper {
      min-height: 100vh;
      background-color: #030712;
      background-image:
        radial-gradient(at 0% 0%, rgba(6, 182, 212, 0.12) 0px, transparent 50%),
        radial-gradient(at 100% 100%, rgba(132, 204, 22, 0.1) 0px, transparent 50%);
      color: #f9fafb;
      position: relative;
      overflow-x: hidden;
    }
    .glow-orb { position: absolute; border-radius: 50%; filter: blur(120px); pointer-events: none; }
    .orb-1 { width: 400px; height: 400px; background: rgba(132, 204, 22, 0.12); top: -80px; left: -80px; }
    .orb-2 { width: 450px; height: 450px; background: rgba(6, 182, 212, 0.1); bottom: 0; right: -100px; }
    .glass-nav {
      background: rgba(15, 23, 42, 0.75);
      backdrop-filter: blur(16px);
      border-bottom: 1px solid rgba(255, 255, 255, 0.08);
    }
    .nav-logo { height: 40px; }
    .btn-glass-secondary {
      background: rgba(255, 255, 255, 0.06);
      border: 1px solid rgba(255, 255, 255, 0.15);
      color: #f3f4f6;
      border-radius: 9999px;
      padding: 0.5rem 1.2rem;
      text-decoration: none;
      font-size: 0.88rem;
    }
    .hero-card, .complejo-showcase-card, .empty-card {
      background: rgba(15, 23, 42, 0.65);
      backdrop-filter: blur(16px);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 20px;
      padding: 1.75rem;
    }
    .filter-chip {
      background: rgba(255, 255, 255, 0.06);
      border: 1px solid rgba(255, 255, 255, 0.14);
      color: #cbd5e1;
      border-radius: 9999px;
      padding: 0.4rem 1rem;
      font-size: 0.85rem;
      cursor: pointer;
      transition: all 0.2s;
    }
    .filter-chip.active, .filter-chip:hover {
      background: linear-gradient(135deg, #84cc16 0%, #10b981 100%);
      color: #022c22;
      font-weight: 700;
      border-color: transparent;
    }
    .badge-verified {
      background: rgba(16, 185, 129, 0.15);
      color: #34d399;
      font-size: 0.7rem;
      font-weight: 700;
      padding: 0.25rem 0.65rem;
      border-radius: 6px;
      letter-spacing: 0.05em;
    }
    .prestacion-pill {
      background: rgba(132, 204, 22, 0.12);
      border: 1px solid rgba(132, 204, 22, 0.3);
      color: #bef264;
      font-size: 0.75rem;
      padding: 0.25rem 0.7rem;
      border-radius: 9999px;
    }
    .divider { border-color: rgba(255, 255, 255, 0.1); }
    .cancha-inner-card {
      background: rgba(30, 41, 59, 0.6);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 14px;
      padding: 1.2rem;
      height: 100%;
    }
    .sport-tag {
      background: rgba(6, 182, 212, 0.15);
      color: #22d3ee;
      font-size: 0.72rem;
      font-weight: 700;
      padding: 0.2rem 0.55rem;
      border-radius: 5px;
      text-transform: uppercase;
    }
    .price-tag {
      color: #a3e635;
      font-weight: 800;
      font-size: 1.05rem;
    }
    .slot-badge {
      background: rgba(255, 255, 255, 0.06);
      border: 1px solid rgba(255, 255, 255, 0.1);
      color: #e2e8f0;
      font-size: 0.74rem;
      padding: 0.2rem 0.5rem;
      border-radius: 6px;
    }
    .text-brand-lime { color: #84cc16; }
  `]
})
export class ExplorarComplejosComponent implements OnInit {
  complejosConCanchas: { complejo: Complejo; canchas: Cancha[] }[] = [];
  isLoading: boolean = true;
  deporteSeleccionado: string = 'Todos';

  readonly filtrosDeporte: string[] = ['Todos', 'Futbol 5', 'Futbol 7', 'Padel', 'Tenis', 'Basquet'];

  readonly diasSemanaMap: { [key: number]: string } = {
    1: 'Lun', 2: 'Mar', 3: 'Mie', 4: 'Jue', 5: 'Vie', 6: 'Sab', 7: 'Dom'
  };

  constructor(private complejoService: ComplejoService) {}

  ngOnInit(): void {
    this.cargarCatalogo();
  }

  cargarCatalogo(): void {
    this.isLoading = true;
    this.complejoService.obtenerTodosLosComplejos().subscribe({
      next: (complejos) => {
        if (!complejos || complejos.length === 0) {
          this.complejosConCanchas = [];
          this.isLoading = false;
          return;
        }

        let pendientes = complejos.length;
        const resultado: { complejo: Complejo; canchas: Cancha[] }[] = [];

        complejos.forEach((comp) => {
          if (comp.id) {
            this.complejoService.obtenerCanchasPorComplejo(comp.id).subscribe({
              next: (canchas) => {
                resultado.push({ complejo: comp, canchas: canchas || [] });
                pendientes--;
                if (pendientes === 0) {
                  this.complejosConCanchas = resultado;
                  this.isLoading = false;
                }
              },
              error: () => {
                resultado.push({ complejo: comp, canchas: [] });
                pendientes--;
                if (pendientes === 0) {
                  this.complejosConCanchas = resultado;
                  this.isLoading = false;
                }
              }
            });
          } else {
            resultado.push({ complejo: comp, canchas: [] });
            pendientes--;
            if (pendientes === 0) {
              this.complejosConCanchas = resultado;
              this.isLoading = false;
            }
          }
        });
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  seleccionarFiltroDeporte(deporte: string): void {
    this.deporteSeleccionado = deporte;
  }

  complejosFiltrados(): { complejo: Complejo; canchas: Cancha[] }[] {
    if (this.deporteSeleccionado === 'Todos') {
      return this.complejosConCanchas;
    }
    return this.complejosConCanchas
      .map(item => ({
        complejo: item.complejo,
        canchas: item.canchas.filter(c => c.deporte.toLowerCase() === this.deporteSeleccionado.toLowerCase())
      }))
      .filter(item => item.canchas.length > 0);
  }

  getNombreDia(dia: number): string {
    return this.diasSemanaMap[dia] || `Dia ${dia}`;
  }

  formatearHora(hora: string): string {
    return hora ? hora.substring(0, 5) : '';
  }
}
