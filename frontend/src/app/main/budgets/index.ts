import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { BudgetModal } from "../../components/budget-modal";
import { DeleteModal } from "../../components/delete-modal";
import { BudgetService } from "../../services/budget.service";
import { ToastService } from "../../services/toast.service";
import { Budget, BudgetRequest } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "app-budgets",
  imports: [BudgetModal, DeleteModal],
  templateUrl: "./index.html",
})
export class Budgets implements OnInit {
  private readonly budgetService = inject(BudgetService);
  private readonly toastService = inject(ToastService);

  public readonly currency = signal(env.currency);
  public budgets = signal<Budget[]>([]);
  public title = signal("Delete a budget");
  public content = signal("");

  budgetId: number | null = null;

  ngOnInit() {
    this.budgetService.getAll().subscribe({
      next: (res) => this.budgets.set(res),
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  onSaved(budget: Budget) {
    const index = this.budgets().findIndex((b) => b.id === budget.id);
    if (index !== -1) {
      this.budgets.update((list) => list.map((b) => (b.id === budget.id ? budget : b)));
    } else {
      this.budgets.update((list) => [...list, budget]);
    }
  }

  confirmDelete() {
    if (this.budgetId) {
      this.budgetService.delete(this.budgetId).subscribe({
        error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
      });
      this.budgets.set(this.budgets().filter((b) => b.id !== this.budgetId));
    }
  }

  updateRequest(budget: Budget | null, modal: BudgetModal) {
    modal.open(budget);
  }

  deleteRequest(budget: Budget, modal: DeleteModal) {
    this.budgetId = budget.id;
    this.content.set(`Are you sure you want to delete this budget?`);
    modal.open();
  }
}
