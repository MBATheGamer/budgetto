import { Component, ElementRef, inject, OnInit, signal, viewChild } from "@angular/core";
import { AuthService } from "../../services/auth.service";
import { Category } from "../../types";

@Component({
  selector: "category-modal",
  imports: [],
  templateUrl: "./index.html",
})
export class CategoryModal implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly username = signal("");

  private readonly defaultCategory: Category = {
    id: null,
    username: this.username(),
    name: "",
    icon: "📱",
    type: "EXPENSE",
    "is-default": false,
  };

  ngOnInit(): void {
    const user = this.authService.getUser();

    if (user) {
      this.username.set(`${user["first-name"]} ${user["last-name"]}`);
    }
  }

  category = signal<Category>(this.defaultCategory);

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  open(category: Category | null) {
    if (category != null) {
      this.category.set(category);
    } else {
      this.category.set(this.defaultCategory);
    }
    this.dialogRef()?.nativeElement.showModal();
  }
}
