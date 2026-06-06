import { HttpErrorResponse } from "@angular/common/http";
import { Component, inject, OnInit, signal } from "@angular/core";
import { AddMemberModal } from "../../components/add-member-modal";
import { DeleteModal } from "../../components/delete-modal";
import { SharedBudgetModal } from "../../components/shared-budget-modal";
import { AuthService } from "../../services/auth.service";
import { SharedBudgetService } from "../../services/shared-budget.service";
import { ToastService } from "../../services/toast.service";
import { SharedBudget, SharedBudgetMember } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "app-shared-budgets",
  imports: [SharedBudgetModal, DeleteModal, AddMemberModal],
  templateUrl: "./index.html",
})
export class SharedBudgets implements OnInit {
  private readonly sharedBudgetService = inject(SharedBudgetService);
  private readonly toastService = inject(ToastService);
  private readonly authService = inject(AuthService);

  public sharedBudgets = signal<SharedBudget[]>([]);
  public title = signal("Delete a group");
  public content = signal("");
  public readonly currency = signal(env.currency);
  

  sharedBudgetId = signal<number | null>(null);
  memberId = signal<number | null>(null);

  ngOnInit() {
    this.sharedBudgetService.getAll().subscribe({
      next: (res) => {
        this.sharedBudgets.set(res);
      },
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  isOwner(budget: SharedBudget): boolean {
    const user = this.authService.getUser();
    if (!user) return false;
    return budget.members.some(
      (m) =>
        m.role === "OWNER" &&
        m["first-name"] === user["first-name"] &&
        m["last-name"] === user["last-name"],
    );
  }

  isPendingForMe(budget: SharedBudget): boolean {
    const user = this.authService.getUser();
    if (!user) return false;
    return budget.members.some(
      (m) =>
        m.status === "PENDING" &&
        m["first-name"] === user["first-name"] &&
        m["last-name"] === user["last-name"],
    );
  }

  getMyMember(budget: SharedBudget): SharedBudgetMember | undefined {
    const user = this.authService.getUser();
    if (!user) return undefined;
    return budget.members.find(
      (m) => m["first-name"] === user["first-name"] && m["last-name"] === user["last-name"],
    );
  }

  onSaved(sharedBudget: SharedBudget) {
    const index = this.sharedBudgets().findIndex((b) => b.id === sharedBudget.id);
    if (index !== -1) {
      this.sharedBudgets.update((list) =>
        list.map((b) => (b.id === sharedBudget.id ? sharedBudget : b)),
      );
    } else {
      this.sharedBudgets.update((list) => [...list, sharedBudget]);
    }
  }

  acceptInvitation(budget: SharedBudget) {
    const member = this.getMyMember(budget);
    if (!member || !budget.id) return;

    this.sharedBudgetService.updateMemberStatus(budget.id, member.id, "ACTIVE").subscribe({
      next: () => {
        this.sharedBudgetService.getById(budget.id!).subscribe({
          next: (saved) => {
            this.toastService.show("Invitation accepted", "success");
            this.onSaved(saved);
          },
          error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
        });
      },
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  rejectInvitation(budget: SharedBudget) {
    const member = this.getMyMember(budget);
    if (!member || !budget.id) return;

    this.sharedBudgetService.updateMemberStatus(budget.id, member.id, "PENDING").subscribe({
      next: () => {
        this.toastService.show("Invitation rejected", "success");
        this.sharedBudgets.update((list) => list.filter((b) => b.id !== budget.id));
      },
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  confirmDelete() {
    const id = this.sharedBudgetId();
    if (!id) return;

    this.sharedBudgetService.delete(id).subscribe({
      next: () => {
        this.sharedBudgets.update((list) => list.filter((b) => b.id !== id));
      },
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  confirmRemoveMember() {
    const budgetId = this.sharedBudgetId();
    const mId = this.memberId();
    if (!budgetId || !mId) return;

    this.sharedBudgetService.removeMember(budgetId, mId).subscribe({
      next: () => {
        this.sharedBudgetService.getById(budgetId).subscribe({
          next: (saved) => {
            this.toastService.show("Member removed", "success");
            this.onSaved(saved);
          },
          error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
        });
      },
      error: (error: HttpErrorResponse) => this.toastService.show(error.error, "error"),
    });
  }

  removeMemberRequest(budget: SharedBudget, member: SharedBudgetMember, modal: DeleteModal) {
    this.sharedBudgetId.set(budget.id);
    this.memberId.set(member.id);
    this.content.set(
      `Remove ${member["first-name"]} ${member["last-name"]} from "${budget.name}"?`,
    );
    modal.open();
  }

  updateRequest(sharedBudget: SharedBudget | null, modal: SharedBudgetModal) {
    modal.open(sharedBudget);
  }

  deleteRequest(sharedBudget: SharedBudget, modal: DeleteModal) {
    this.sharedBudgetId.set(sharedBudget.id);
    this.memberId.set(null);
    this.content.set(`Are you sure you want to delete "${sharedBudget.name}"?`);
    modal.open();
  }

  addMemberRequest(budget: SharedBudget, modal: AddMemberModal) {
    modal.open(budget.id!);
  }
}
