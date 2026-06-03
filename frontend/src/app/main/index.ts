import { HttpClient } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { Router, RouterLinkWithHref, RouterOutlet } from "@angular/router";
import { env } from "../../env";
import { AuthService } from "../services/auth.service";
import { ToastService } from "../services/toast.service";
import { UserResponse } from "../types";

@Component({
  selector: "app-main",
  imports: [RouterOutlet, RouterLinkWithHref],
  templateUrl: "./index.html",
})
export class Main implements OnInit {
  private router = inject(Router);
  private http = inject(HttpClient);
  private toast = inject(ToastService);
  private authService = inject(AuthService);

  public user = signal<UserResponse>({
    "first-name": "",
    "last-name": "",
    email: "",
    role: "USER",
    status: "ACTIVE",
  });

  ngOnInit() {
    this.http.get<UserResponse>(`${env["BASE_URL"]}${env["API_VERSION"]}/auth/me`).subscribe({
      next: (res) => {
        this.authService.setUser(res);
        const user = this.authService.getUser();

        if (user != null) {
          this.user.set(user);
        }
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
        this.authService.clearUser();
        this.router.navigate(["/sign-in"]);
      },
      error: () => {
        this.toast.show("Logout failed");
      },
    });
  }
}
