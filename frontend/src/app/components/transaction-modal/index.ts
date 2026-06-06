import { Component, ElementRef, inject, OnInit, output, signal, viewChild } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { CategoryService } from "../../services/category.service";
import { SharedBudgetService } from "../../services/shared-budget.service";
import { ToastService } from "../../services/toast.service";
import { TransactionService } from "../../services/transaction.service";
import { Category, SharedBudget, Transaction, TransactionRequest } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "transaction-modal",
  imports: [ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class TransactionModal implements OnInit {
  private readonly transactionService = inject(TransactionService);
  private readonly categoryService = inject(CategoryService);
  private readonly sharedBudgetService = inject(SharedBudgetService);
  private readonly toastService = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  public readonly currency = signal(env.currency);

  saved = output<Transaction>();
  submitting = signal(false);
  categories = signal<Category[]>([]);
  sharedBudgets = signal<SharedBudget[]>([]);

  private readonly defaultTransaction: Transaction = {
    id: null,
    "transaction-date": new Date().toISOString().split("T")[0],
    description: "",
    category: "",
    "author-first-name": "",
    "author-last-name": "",
    type: "EXPENSE",
    amount: 0,
  };

  transaction = signal<Transaction>(this.defaultTransaction);

  form = this.fb.group({
    type: ["EXPENSE", Validators.required],
    description: ["", Validators.required],
    amount: [0, Validators.required],
    transactionDate: [new Date().toISOString().split("T")[0], Validators.required],
    categoryId: [null as number | null, Validators.required],
    sharedBudgetId: [null as number | null],
    comment: [""],
  });

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: (res) => this.categories.set(res),
      error: () => this.toastService.show("Failed to load categories", "error"),
    });

    this.sharedBudgetService.getAll().subscribe({
      next: (res) => this.sharedBudgets.set(res),
      error: () => this.toastService.show("Failed to load shared budgets", "error"),
    });
  }

  get isEditing() {
    return this.transaction().id !== null;
  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!(control?.invalid && control.touched);
  }

  open(transaction: Transaction | null) {
    if (transaction !== null) {
      this.transaction.set(transaction);
    } else {
      this.transaction.set(this.defaultTransaction);
    }

    this.form.reset();
    this.form.patchValue({
      type: transaction?.type ?? "EXPENSE",
      description: transaction?.description ?? "",
      amount: transaction?.amount ?? 0,
      transactionDate: transaction?.["transaction-date"] ?? new Date().toISOString().split("T")[0],
      categoryId: null,
      sharedBudgetId: null,
      comment: "",
    });

    this.dialogRef()?.nativeElement.showModal();
  }

  close() {
    this.dialogRef()?.nativeElement.close();
  }

  onSubmit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const editing = this.transaction();
    const body: TransactionRequest = {
      type: this.form.value.type!,
      description: this.form.value.description!,
      amount: this.form.value.amount!,
      "transaction-date": this.form.value.transactionDate!,
      "category-id": this.form.value.categoryId ?? null,
      "shared-budget-id": this.form.value.sharedBudgetId ?? null,
      comment: this.form.value.comment ?? "",
    };

    const request =
      editing.id !== null
        ? this.transactionService.update(editing.id, body)
        : this.transactionService.create(body);

    this.submitting.set(true);

    request.subscribe({
      next: (saved) => {
        this.toastService.show(
          editing.id !== null
            ? "Transaction updated successfully"
            : "Transaction created successfully",
          "success",
        );
        this.saved.emit(saved);
        this.submitting.set(false);
        this.close();
      },
      error: () => {
        this.toastService.show(
          editing.id !== null ? "Failed to update transaction" : "Failed to create transaction",
          "error",
        );
        this.submitting.set(false);
      },
    });
  }
}
