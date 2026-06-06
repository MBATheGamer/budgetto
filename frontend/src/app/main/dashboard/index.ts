import { HttpClient } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { Router } from "@angular/router";
import { DatePipe } from "@angular/common";
import { AuthService } from "../../services/auth.service";
import { TransactionService } from "../../services/transaction.service";
import { BudgetService } from "../../services/budget.service";
import { ToastService } from "../../services/toast.service";
import { TransactionModal } from "../../components/transaction-modal";
import { Transaction, Budget, User } from "../../types";

@Component({
  selector: "app-dashboard",
  imports: [DatePipe, TransactionModal],
  templateUrl: "./index.html",
})
export class Dashboard implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly transactionService = inject(TransactionService);
  private readonly budgetService = inject(BudgetService);
  private readonly toastService = inject(ToastService);

  public user = signal<User | null>(null);
  public transactions = signal<Transaction[]>([]);
  public budgets = signal<Budget[]>([]);
  public loading = signal(true);

  // computed stats
  get totalIncome(): number {
    return this.transactions()
      .filter((t) => t.type === "INCOME")
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get totalExpense(): number {
    return this.transactions()
      .filter((t) => t.type === "EXPENSE")
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get balance(): number {
    return this.totalIncome - this.totalExpense;
  }

  get recentTransactions(): Transaction[] {
    return this.transactions().slice(0, 5);
  }

  ngOnInit() {
    this.user.set(this.authService.getUser());

    this.transactionService.getAll().subscribe({
      next: (res) => {
        this.transactions.set(res);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });

    this.budgetService.getAll().subscribe({
      next: (res) => this.budgets.set(res),
    });
  }

  onTransactionSaved(transaction: Transaction) {
    this.transactions.update((list) => {
      const index = list.findIndex((t) => t.id === transaction.id);
      if (index !== -1) return list.map((t) => (t.id === transaction.id ? transaction : t));
      return [transaction, ...list];
    });
  }
  updateRequest(transaction: Transaction | null, modal: TransactionModal) {
    modal.open(transaction);
  }
  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.authService.clearAccessToken();
        this.authService.clearUser();
        this.router.navigate(["/sign-in"]);
      },
      error: () => {
        this.toastService.show("Logout failed", "error");
      },
    });
  }
}
