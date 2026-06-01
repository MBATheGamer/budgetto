import { Component, inject } from "@angular/core";
import { ToastService } from "./toast.service";

@Component({
  selector: "app-toast",
  template: `
    <div class="toast toast-top toast-end z-50">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="alert alert-{{ toast.type }}">
          <span>{{ toast.message }}</span>
        </div>
      }
    </div>
  `,
})
export class ToastComponent {
  toastService = inject(ToastService);
}
