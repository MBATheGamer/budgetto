import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { env } from "../../env";

interface JwtResponse {
  token: string;
}

@Injectable({ providedIn: "root" })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly tokenKey = "access-token";

  setAccessToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  clearAccessToken() {
    localStorage.removeItem(this.tokenKey);
  }

  refresh() {
    return this.http.post<JwtResponse>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/auth/refresh`,
      {},
      { withCredentials: true },
    );
  }

  logout() {
    this.clearAccessToken();
    this.router.navigate(["/sign-in"]);
  }
}
