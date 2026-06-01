import { HttpClient } from "@angular/common/http";
import { Component, inject, signal } from "@angular/core";
import { RouterOutlet } from "@angular/router";

@Component({
  selector: "app-root",
  imports: [RouterOutlet],
  template: "<router-outlet />",
})
export class App {
  protected readonly title = signal("budgetto");
  public response = signal("");
  private http = inject(HttpClient);

  ngOnInit() {
    this.http.get<{status: string}>("http://localhost:8080/api/v1/health")
    .subscribe({
      next: (data) => {
        this.response.set(data.status);
      }
    })
  }
}
