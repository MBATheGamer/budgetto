import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { CategoryService } from "../../services/category.service";
import { ToastService } from "../../services/toast.service";
import { CategoryResponse } from "../../types";

@Component({
  selector: "app-categories",
  imports: [],
  templateUrl: "./index.html",
})
export class Categories implements OnInit {
  private readonly categoryService = inject(CategoryService);
  private readonly toastService = inject(ToastService);

  public categories = signal<CategoryResponse[]>([]);
  public defaultCategories = signal(0);
  public customCategories = signal(0);

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: (res) => {
        this.categories.set(res);
        this.defaultCategories.set(
          this.categories().filter((category) => category["is-default"]).length,
        );
        this.customCategories.set(this.categories().length - this.defaultCategories());
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.show(error.error, "error");
      },
    });
  }
}
