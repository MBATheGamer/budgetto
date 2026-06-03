import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Component, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { env } from "../../../env";
import { AuthService } from "../../services/auth.service";
import { ToastService } from "../../services/toast.service";

@Component({
  selector: "app-sign-in",
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class SignIn {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private auth = inject(AuthService);
  private toast = inject(ToastService);

  loading = signal(false);

  form = this.fb.group({
    email: ["", [Validators.required, Validators.email]],
    password: ["", Validators.required],
  });

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!(control?.invalid && control.touched);
  }

  onSubmit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    this.loading.set(true);

    const { email, password } = this.form.value;

    this.http
      .post<{
        token: string;
      }>(
        `${env["BASE_URL"]}${env["API_VERSION"]}/auth/login`,
        { email, password },
        { withCredentials: true },
      )
      .subscribe({
        next: (res) => {
          this.auth.setAccessToken(res.token);
          this.toast.show("Successfully logged in!", "success");
          this.router.navigate(["/dashboard"]);
        },
        error: (error: HttpErrorResponse) => {
          this.loading.set(false);
          this.toast.show(error.error, "error");
        },
      });
  }
}
