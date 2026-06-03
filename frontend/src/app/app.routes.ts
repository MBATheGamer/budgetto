import { Routes } from "@angular/router";
import { Auth } from "./auth";
import { SignIn } from "./auth/sign-in";
import { SignUp } from "./auth/sign-up";
import { AuthGuard } from "./guards/auth.guard";
import { GuestGuard } from "./guards/guest.guard";
import { Main } from "./main";
import { Dashboard } from "./main/dashboard";

export const routes: Routes = [
  {
    path: "",
    component: Auth,
    canActivate: [GuestGuard],
    children: [
      {
        path: "sign-up",
        component: SignUp,
      },
      {
        path: "sign-in",
        component: SignIn,
      },
    ],
  },
  {
    path: "",
    component: Main,
    canActivate: [AuthGuard],
    children: [
      {
        path: "dashboard",
        component: Dashboard,
      },
    ],
  },
];
