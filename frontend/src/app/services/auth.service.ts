import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { env } from "../../env";
import { UserResponse } from "../types";

@Injectable({ providedIn: "root" })
export class AuthService {
  private http = inject(HttpClient);

  public setAccessToken(token: string) {
    localStorage.setItem(env["ACCESS_TOKEN_NAME"], token);
  }

  public setUser(user: UserResponse) {
    localStorage.setItem(env["USER_NAME"], JSON.stringify(user));
  }

  public getAccessToken(): string | null {
    return localStorage.getItem(env["ACCESS_TOKEN_NAME"]);
  }

  public getUser(): UserResponse | null {
    return JSON.parse(localStorage.getItem(env["USER_NAME"]) as string);
  }

  public clearAccessToken() {
    localStorage.removeItem(env["ACCESS_TOKEN_NAME"]);
  }

  public clearUser() {
    localStorage.removeItem(env["USER_NAME"]);
  }

  refresh() {
    return this.http.post<{ token: string }>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/auth/refresh`,
      {},
      { withCredentials: true },
    );
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(env["ACCESS_TOKEN_NAME"]);
  }

  logout() {
    return this.http.post(
      `${env["BASE_URL"]}${env["API_VERSION"]}/auth/logout`,
      {},
      { withCredentials: true },
    );
  }
}
