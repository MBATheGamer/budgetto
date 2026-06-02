import { HttpClient } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { Router } from "@angular/router";
import { env } from "../../env";
import { AuthService } from "../services/auth.service";
import { ToastService } from "../toast.service";

interface UserResponse {
  "first-name": string;
  "last-name": string;
  email: string;
}

@Component({
  selector: "app-dashboard",
  templateUrl: "./index.html",
})
export class Dashboard implements OnInit {
  private router = inject(Router);
  private http = inject(HttpClient);
  private toast = inject(ToastService);
  private authService = inject(AuthService);

  public user = signal<UserResponse>({ "first-name": "", "last-name": "", email: "" });

  ngOnInit() {
    this.http.get<UserResponse>(`${env["BASE_URL"]}${env["API_VERSION"]}/auth/me`).subscribe({
      next: (res) => {
        this.user.set(res);
      },
      error: () => {
        this.authService.logout();
      },
    });
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.authService.clearAccessToken();
        this.router.navigate(["/sign-in"]);
      },
      error: () => {
        this.toast.show("Logout failed");
      },
    });
  }
}
