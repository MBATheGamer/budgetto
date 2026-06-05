import { Routes } from "@angular/router";
import { Auth } from "./auth";
import { SignIn } from "./auth/sign-in";
import { SignUp } from "./auth/sign-up";
import { AuthGuard } from "./guards/auth.guard";
import { GuestGuard } from "./guards/guest.guard";
import { Main } from "./main";
import { Categories } from "./main/categories";
import { Dashboard } from "./main/dashboard";
import { Budgets } from "./main/budgets";
import { SharedBudgets } from "./main/shared-budgets";

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
      {
        path: "categories",
        component: Categories,
      },
      {
        path: "budgets",
        component: Budgets,
      },
      {
        path: "shared-budgets",
        component: SharedBudgets,
      },
    ],
  },
];
