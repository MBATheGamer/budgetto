import { Routes } from "@angular/router";
import { Dashboard } from "./dashboard";
import { AuthGuard } from "./guards/auth.guard";
import { GuestGuard } from "./guards/guest.guard";
import { SignIn } from "./sign-in";
import { SignUp } from "./sign-up";

export const routes: Routes = [
  {
    path: "sign-up",
    component: SignUp,
    canActivate: [GuestGuard],
  },
  {
    path: "sign-in",
    component: SignIn,
    canActivate: [GuestGuard],
  },
  {
    path: "dashboard",
    component: Dashboard,
    canActivate: [AuthGuard],
  },
];
