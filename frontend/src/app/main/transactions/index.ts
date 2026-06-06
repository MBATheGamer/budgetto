import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { DatePipe } from "@angular/common";
import { DeleteModal } from "../../components/delete-modal";
import { TransactionModal } from "../../components/transaction-modal";
import { ToastService } from "../../services/toast.service";
import { TransactionService } from "../../services/transaction.service";
import { Transaction } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "app-transactions",
  imports: [TransactionModal, DeleteModal, DatePipe],
  templateUrl: "./index.html",
})
export class Transactions implements OnInit {
  private readonly transactionService = inject(TransactionService);
  private readonly toastService = inject(ToastService);

  public transactions = signal<Transaction[]>([]);
  public expenseFilter = signal<"all" | "income" | "expense">("all");
  public searchQuery = signal("");
  public title = signal("Delete a transaction");
  public content = signal("");
  public readonly currency = signal(env.currency);

  transactionId: number | null = null;

  ngOnInit() {
    this.transactionService.getAll().subscribe({
      next: (res) => this.transactions.set(res),
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  getFiltered(): Transaction[] {
    return this.transactions().filter((t) => {
      const matchesType =
        this.expenseFilter() === "all" ||
        (this.expenseFilter() === "income" && t.type === "INCOME") ||
        (this.expenseFilter() === "expense" && t.type === "EXPENSE");

      const query = this.searchQuery().toLowerCase();
      const matchesSearch =
        !query ||
        t.description.toLowerCase().includes(query) ||
        t.category?.toLowerCase().includes(query);

      return matchesType && matchesSearch;
    });
  }

  onSaved(transaction: Transaction) {
    const index = this.transactions().findIndex((t) => t.id === transaction.id);
    if (index !== -1) {
      this.transactions.update((list) =>
        list.map((t) => (t.id === transaction.id ? transaction : t)),
      );
    } else {
      this.transactions.update((list) => [transaction, ...list]);
    }
  }

  confirmDelete() {
    if (this.transactionId) {
      this.transactionService.delete(this.transactionId).subscribe({
        error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
      });
      this.transactions.set(this.transactions().filter((t) => t.id !== this.transactionId));
    }
  }

  updateRequest(transaction: Transaction | null, modal: TransactionModal) {
    modal.open(transaction);
  }

  deleteRequest(transaction: Transaction, modal: DeleteModal) {
    this.transactionId = transaction.id;
    this.content.set(`Are you sure you want to delete "${transaction.description}"?`);
    modal.open();
  }
}
