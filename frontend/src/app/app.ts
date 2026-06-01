import { Component, signal } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { ToastComponent } from "./toast.component";

@Component({
  selector: "app-root",
  imports: [RouterOutlet, ToastComponent],
  template: "<router-outlet /><app-toast />",
})
export class App {
  protected readonly title = signal("budgetto");
}
