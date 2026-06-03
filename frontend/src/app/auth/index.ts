import { Component } from "@angular/core";
import { RouterLink, RouterOutlet } from "@angular/router";

@Component({
  selector: "app-auth",
  imports: [RouterOutlet, RouterLink],
  templateUrl: "./index.html",
})
export class Auth {}
