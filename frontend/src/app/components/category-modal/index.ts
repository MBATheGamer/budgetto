import { Component, ElementRef, inject, OnInit, output, signal, viewChild } from "@angular/core";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { AuthService } from "../../services/auth.service";
import { CategoryService } from "../../services/category.service";
import { ToastService } from "../../services/toast.service";
import { Category, CategoryRequest } from "../../types";

@Component({
  selector: "category-modal",
  imports: [ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class CategoryModal implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly categoryService = inject(CategoryService);
  private readonly toastService = inject(ToastService);
  private readonly fb = inject(FormBuilder);
  private readonly username = signal("");

  saved = output<{ category: Category; editingId: number | null }>();
  submitting = signal(false);

  form = this.fb.group({
    name: [""],
    icon: ["📱"],
    type: ["EXPENSE"],
  });

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
      this.form.setValue({ name: category.name, icon: category.icon, type: category.type });
    } else {
      this.category.set(this.defaultCategory);
    }
    this.dialogRef()?.nativeElement.showModal();
  }

  close() {
    this.dialogRef()?.nativeElement.close();
  }
  onSubmit() {
    const editing = this.category();
    const body = {
      name: this.form.value.name!,
      icon: this.form.value.icon!,
      type: this.form.value.type as "EXPENSE" | "INCOME",
    } as CategoryRequest;

    const request$ =
      editing.id !== null
        ? this.categoryService.update(editing.id, body)
        : this.categoryService.create(body);

    this.submitting.set(true);

    request$.subscribe({
      next: (saved) => {
        this.toastService.show(
          editing.id !== null ? "Category updated successfully" : "Category created successfully",
          "success",
        );
        this.saved.emit({ category: saved, editingId: editing.id });
        this.submitting.set(false);
        this.close();
      },
      error: () => {
        this.toastService.show(
          editing.id !== null ? "Failed to update category" : "Failed to create category",
          "error",
        );
        this.submitting.set(false);
      },
    });
  }
}
