import { Component, inject, OnInit, signal } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { UserResponse } from "../../types";

@Component({
  selector: "app-dashboard",
  templateUrl: "./index.html",
})
export class Dashboard implements OnInit {
  private readonly authService = inject(AuthService);

  public user = signal<UserResponse>({
    "first-name": "",
    "last-name": "",
    email: "",
    role: "USER",
    status: "ACTIVE",
  });

  ngOnInit() {
    const user = this.authService.getUser();

    if (user != null) {
      this.user.set(user);
    }
  }
}
