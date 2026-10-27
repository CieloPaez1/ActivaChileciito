import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CanchasAdminComponent } from './canchas-admin.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ComplejoService } from '../../../core/services/complejo.service';
import { of } from 'rxjs';

describe('CanchasAdminComponent', () => {
  let component: CanchasAdminComponent;
  let fixture: ComponentFixture<CanchasAdminComponent>;
  let complejoService: jasmine.SpyObj<ComplejoService>;

  beforeEach(async () => {
    const complejoServiceSpy = jasmine.createSpyObj('ComplejoService', [
      'obtenerMiComplejo',
      'actualizarMiComplejo',
      'obtenerMisCanchas',
      'registrarCancha',
      'eliminarCancha'
    ]);
    complejoServiceSpy.obtenerMiComplejo.and.returnValue(of({
      id: 1,
      nombre: 'Complejo Test',
      direccion: 'Chilecito',
      telefono: '3825111',
      prestaciones: ['Iluminacion LED']
    }));
    complejoServiceSpy.obtenerMisCanchas.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [CanchasAdminComponent],
      providers: [
        { provide: ComplejoService, useValue: complejoServiceSpy },
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CanchasAdminComponent);
    component = fixture.componentInstance;
    complejoService = TestBed.inject(ComplejoService) as jasmine.SpyObj<ComplejoService>;
    fixture.detectChanges();
  });

  it('should create CanchasAdminComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should open and close modal', () => {
    component.abrirModal();
    expect(component.showModal).toBeTrue();
    component.cerrarModal();
    expect(component.showModal).toBeFalse();
  });

  it('should add schedule slot', () => {
    component.nuevoDiaSemana = 1;
    component.nuevaHoraInicio = '18:00';
    component.nuevaHoraFin = '22:00';
    component.agregarFranjaHoraria();
    expect(component.horariosAgregados.length).toBe(1);
    expect(component.horariosAgregados[0].diaSemana).toBe(1);
    expect(component.horariosAgregados[0].horaInicio).toBe('18:00:00');
  });

  it('should toggle prestacion in complex form', () => {
    component.abrirModalComplejo();
    expect(component.tienePrestacion('Iluminacion LED')).toBeTrue();
    component.togglePrestacion('Buffet / Cantina');
    expect(component.tienePrestacion('Buffet / Cantina')).toBeTrue();
  });
});
