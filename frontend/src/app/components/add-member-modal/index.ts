import { Component, ElementRef, inject, output, signal, viewChild } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { SharedBudgetService } from "../../services/shared-budget.service";
import { ToastService } from "../../services/toast.service";
import { SharedBudget } from "../../types";

@Component({
  selector: "add-member-modal",
  imports: [ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class AddMemberModal {
  private readonly sharedBudgetService = inject(SharedBudgetService);
  private readonly toastService = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  saved = output<SharedBudget>();
  submitting = signal(false);

  private budgetId = signal<number | null>(null);

  form = this.fb.group({
    email: ["", [Validators.required, Validators.email]],
  });

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  open(budgetId: number) {
    this.budgetId.set(budgetId);
    this.form.reset({ email: "" });
    this.dialogRef()?.nativeElement.showModal();
  }

  close() {
    this.dialogRef()?.nativeElement.close();
  }

  onSubmit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const id = this.budgetId();
    if (!id) return;

    this.submitting.set(true);

    this.sharedBudgetService.addMember(id, this.form.value.email!).subscribe({
      next: (saved) => {
        this.toastService.show("Member added successfully", "success");
        this.saved.emit(saved);
        this.submitting.set(false);
        this.close();
      },
      error: () => {
        this.toastService.show("Failed to add member", "error");
        this.submitting.set(false);
      },
    });
  }
}
