import { Component, inject } from "@angular/core";
import { ToastService } from "../../services/toast.service";

@Component({
  selector: "app-toast",
  templateUrl: "./index.html",
})
export class ToastComponent {
  toastService = inject(ToastService);
}
