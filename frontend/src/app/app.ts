import { HttpClient } from "@angular/common/http";
import { Component, inject, signal } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { ToastComponent } from "./toast.component";

@Component({
  selector: "app-root",
  imports: [RouterOutlet, ToastComponent],
  template: "<router-outlet /><app-toast />",
})
export class App {
  protected readonly title = signal("budgetto");
  public response = signal("");
  private http = inject(HttpClient);

  ngOnInit() {
    this.http.get<{ status: string }>("http://localhost:8080/api/v1/health").subscribe({
      next: (data) => {
        this.response.set(data.status);
      },
    });
  }
}
