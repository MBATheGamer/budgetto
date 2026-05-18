import { Component, signal } from "@angular/core";

@Component({
  selector: "app-root",
  imports: [],
  template: "<h1>Hello, World!</h1>",
})
export class App {
  protected readonly title = signal("budgetto");
}
