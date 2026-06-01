import { Component, inject, signal } from "@angular/core";
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { HttpClient } from "@angular/common/http";

function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get("password")?.value;
  const confirm = control.get("confirmPassword")?.value;
  return password && confirm && password !== confirm
    ? { passwordMismatch: true }
    : null;
}

@Component({
  selector: "app-sign-up",
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class SignUp {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  loading = signal(false);
  serverError = signal("");

  form = this.fb.group(
    {
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, Validators.minLength(8)]],
      confirmPassword: ["", Validators.required],
    },
    { validators: passwordMatchValidator }
  );

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!(control?.invalid && control.touched);
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    this.loading.set(true);
    this.serverError.set("");

    const { firstName, lastName, email, password } = this.form.value;

    this.http
      .post("http://localhost:8080/api/v1/auth/register", {
        "first-name": firstName,
        "last-name": lastName,
        email,
        password,
      })
      .subscribe({
        next: () => {
          this.router.navigate(["/sign-in"]);
        },
        error: (err) => {
          this.loading.set(false);
          this.serverError.set(err.error?.email ?? "Registration failed. Please try again.");
        },
      });
  }
}