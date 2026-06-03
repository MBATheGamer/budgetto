import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { CategoryModal } from "../../components/category-modal";
import { DeleteModal } from "../../components/delete-modal";
import { CategoryService } from "../../services/category.service";
import { ToastService } from "../../services/toast.service";
import { Category } from "../../types";

@Component({
  selector: "app-categories",
  imports: [CategoryModal, DeleteModal],
  templateUrl: "./index.html",
})
export class Categories implements OnInit {
  private readonly categoryService = inject(CategoryService);
  private readonly toastService = inject(ToastService);

  public categories = signal<Category[]>([]);
  public defaultCategories = signal(0);
  public customCategories = signal(0);
  public title = signal("Delete a category");
  public content = signal("");

  categoryId: number | null = null;

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

  onSaved({ category, editingId }: { category: Category; editingId: number | null }) {
    if (editingId !== null) {
      this.categories.update((list) => list.map((c) => (c.id === editingId ? category : c)));
    } else {
      this.categories.update((list) => [...list, category]);
      this.customCategories.update((n) => n + 1);
    }
  }

  confirmDelete() {
    if (this.categoryId) {
      this.categoryService.delete(this.categoryId).subscribe({
        error: (error: HttpErrorResponse) => {
          this.toastService.show(error.error, "error");
        },
      });
      this.categories.set(this.categories().filter((category) => category.id !== this.categoryId));
      this.customCategories.update((n) => n - 1);
    }
  }

  updateRequest(category: Category | null, modal: CategoryModal) {
    modal.open(category);
  }

  deleteRequest(category: Category, modal: DeleteModal) {
    this.categoryId = category.id;
    this.content.set(`Are you sure you want to delete "${category.name}"?`);
    modal.open();
  }
}
