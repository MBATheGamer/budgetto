import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { env } from "../../env";

interface JwtResponse {
  token: string;
}

@Injectable({ providedIn: "root" })
export class AuthService {
  private http = inject(HttpClient);
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

  isAuthenticated(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  logout() {
    return this.http.post(
      `${env["BASE_URL"]}${env["API_VERSION"]}/auth/logout`,
      {},
      { withCredentials: true },
    );
  }
}
