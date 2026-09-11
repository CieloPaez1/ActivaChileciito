import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-auth-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './auth-page.component.html',
  styleUrls: ['./auth-page.component.css']
})
export class AuthPageComponent implements OnInit {
  isRightPanelActive: boolean = false; // false = Login, true = Register
  
  loginForm: FormGroup;
  registerForm: FormGroup;
  
  loginError: string = '';
  registerError: string = '';
  registerSuccess: string = '';
  
  isLoadingLogin: boolean = false;
  isLoadingRegister: boolean = false;
  
  roles: string[] = ['CLIENTE', 'ADMIN_COMPLEJO'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
    
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      telefono: [''], // Optional field as per model
      rol: ['CLIENTE', Validators.required]
    });
  }

  ngOnInit(): void {
    // Si la ruta actual es /register, mostramos el panel de registro inicialmente
    const currentUrl = this.router.url;
    if (currentUrl.includes('/register')) {
      this.isRightPanelActive = true;
    }
  }

  togglePanel(isRegister: boolean): void {
    this.isRightPanelActive = isRegister;
    // Update the URL without reloading to reflect the state
    const newUrl = isRegister ? '/register' : '/login';
    this.router.navigateByUrl(newUrl, { replaceUrl: true });
    
    // Clear errors when switching
    this.loginError = '';
    this.registerError = '';
    this.registerSuccess = '';
  }

  onLoginSubmit(): void {
    if (this.loginForm.invalid) return;

    this.isLoadingLogin = true;
    this.loginError = '';

    const { email, password } = this.loginForm.value;

    this.authService.login({ email, password }).subscribe({
      next: () => {
        this.isLoadingLogin = false;
        this.router.navigate(['/']); // O redirigir al perfil/dashboard
      },
      error: (error) => {
        this.isLoadingLogin = false;
        this.loginError = 'Credenciales incorrectas o error en el servidor';
        console.error('Error en login:', error);
      }
    });
  }
  
  onRegisterSubmit(): void {
    if (this.registerForm.invalid) return;

    this.isLoadingRegister = true;
    this.registerError = '';
    this.registerSuccess = '';

    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        this.isLoadingRegister = false;
        this.registerSuccess = '¡Registro exitoso! Ahora puedes iniciar sesión.';
        this.registerForm.reset({ rol: 'CLIENTE' });
        
        // Volver al login tras 2 segundos
        setTimeout(() => {
          this.togglePanel(false);
        }, 2000);
      },
      error: (error) => {
        this.isLoadingRegister = false;
        if (error.status === 409) {
          this.registerError = 'El correo electrónico ya está registrado.';
        } else {
          this.registerError = 'Ocurrió un error al registrar el usuario.';
        }
        console.error('Error en registro:', error);
      }
    });
  }
}
