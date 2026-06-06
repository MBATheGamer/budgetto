import { Component, ElementRef, inject, OnInit, output, signal, viewChild } from "@angular/core";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { BudgetService } from "../../services/budget.service";
import { CategoryService } from "../../services/category.service";
import { ToastService } from "../../services/toast.service";
import { Budget, BudgetRequest, Category } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "budget-modal",
  imports: [ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class BudgetModal implements OnInit {
  private readonly budgetService = inject(BudgetService);
  private readonly categoryService = inject(CategoryService);
  private readonly toastService = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  public readonly currency = signal(env.currency);

  saved = output<Budget>();
  submitting = signal(false);
  categories = signal<{ id: number; name: string }[]>([]);

  private readonly defaultBudget: Omit<Budget, "category-name"> = {
    id: null,
    "category-id": null,
    "amount-limit": 0,
    period: "MONTHLY",
    "start-date": "",
    "end-date": "",
    "alert-threshold": 80,
  };

  budget = signal<Omit<Budget, "category-name">>(this.defaultBudget);

  form = this.fb.group<BudgetRequest>({
    "category-id": null,
    "amount-limit": 0,
    period: "MONTHLY",
    "start-date": null,
    "end-date": null,
    "alert-threshold": 80,
  });

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: (res) => {
        res.map((category) =>
          this.categories().push({ id: category.id as number, name: category.name }),
        );
      },
      error: () => this.toastService.show("Failed to load categories", "error"),
    });
  }

  open(budget: Budget | null) {
    if (budget !== null) {
      this.budget.set(budget);
      this.form.setValue({
        "category-id": budget["category-id"],
        "amount-limit": budget["amount-limit"],
        period: budget.period.toUpperCase(),
        "start-date": budget["start-date"],
        "end-date": budget["end-date"],
        "alert-threshold": budget["alert-threshold"],
      });
    } else {
      this.budget.set(this.defaultBudget);
      this.form.reset({
        "category-id": null,
        "amount-limit": 0,
        period: "MONTHLY",
        "start-date": null,
        "end-date": null,
        "alert-threshold": 80,
      });
    }
    this.dialogRef()?.nativeElement.showModal();
  }

  close() {
    this.dialogRef()?.nativeElement.close();
  }

  onSubmit() {
    const editing = this.budget();
    const body = this.form.value as BudgetRequest;

    console.log(body);

    const request =
      editing.id !== null
        ? this.budgetService.update(editing.id, body)
        : this.budgetService.create(body);

    this.submitting.set(true);

    request.subscribe({
      next: (saved) => {
        this.toastService.show(
          editing.id !== null ? "Budget updated successfully" : "Budget created successfully",
          "success",
        );
        this.saved.emit(saved);
        this.submitting.set(false);
        this.close();
      },
      error: () => {
        this.toastService.show(
          editing.id !== null ? "Failed to update budget" : "Failed to create budget",
          "error",
        );
        this.submitting.set(false);
      },
    });
  }
}
