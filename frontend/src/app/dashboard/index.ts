import { HttpClient } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { env } from "../../env";
import { AuthService } from "../services/auth.service";

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
  private http = inject(HttpClient);
  protected auth = inject(AuthService);

  user = signal<UserResponse>({ "first-name": "", "last-name": "", email: "" });

  ngOnInit() {
    this.http.get<UserResponse>(`${env["BASE_URL"]}${env["API_VERSION"]}/auth/me`).subscribe({
      next: (res) => {
        this.user.set(res);
      },
      error: () => {
        this.auth.logout();
      },
    });
  }
}
