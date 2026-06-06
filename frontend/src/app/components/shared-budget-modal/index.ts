import { Component, ElementRef, inject, output, signal, viewChild } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { SharedBudgetService } from "../../services/shared-budget.service";
import { ToastService } from "../../services/toast.service";
import { SharedBudget, SharedBudgetRequest } from "../../types";
import { env } from "../../../env";

@Component({
  selector: "shared-budget-modal",
  imports: [ReactiveFormsModule],
  templateUrl: "./index.html",
})
export class SharedBudgetModal {
  private readonly sharedBudgetService = inject(SharedBudgetService);
  private readonly toastService = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  public readonly currency = signal(env.currency);

  saved = output<SharedBudget>();
  submitting = signal(false);

  private readonly defaultSharedBudget: SharedBudget = {
    id: null,
    name: "",
    "amount-limit": 0,
    "period-type": "monthly",
    "start-date": "",
    "end-date": null,
    members: [],
  };

  sharedBudget = signal<SharedBudget>(this.defaultSharedBudget);

  form = this.fb.group({
    name: ["", Validators.required],
    amountLimit: [0, Validators.required],
    periodType: ["monthly", Validators.required],
    startDate: ["", Validators.required],
    endDate: [""],
    membersMail: [""],
  });

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  open(sharedBudget: SharedBudget | null) {
    if (sharedBudget !== null) {
      this.sharedBudget.set(sharedBudget);
    } else {
      this.sharedBudget.set(this.defaultSharedBudget);
    }

    this.form.reset();
    this.form.patchValue({
      name: sharedBudget?.name ?? "",
      amountLimit: sharedBudget?.["amount-limit"] ?? 0,
      periodType: sharedBudget?.["period-type"] ?? "monthly",
      startDate: sharedBudget?.["start-date"] ?? "",
      endDate: sharedBudget?.["end-date"] ?? "",
      membersMail: "",
    });

    this.dialogRef()?.nativeElement.showModal();
  }

  close() {
    this.dialogRef()?.nativeElement.close();
  }

  onSubmit() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const editing = this.sharedBudget();
    const body: SharedBudgetRequest = {
      name: this.form.value.name!,
      "amount-limit": this.form.value.amountLimit!,
      "period-type": this.form.value.periodType!.toUpperCase(),
      "start-date": this.form.value.startDate!,
      "end-date": this.form.value.endDate || null,
      "members-mail": this.form.value.membersMail || "",
    };

    const request =
      editing.id !== null
        ? this.sharedBudgetService.update(editing.id, body)
        : this.sharedBudgetService.create(body);

    this.submitting.set(true);

    request.subscribe({
      next: (saved) => {
        this.toastService.show(
          editing.id !== null ? "Group updated successfully" : "Group created successfully",
          "success",
        );
        this.saved.emit(saved);
        this.submitting.set(false);
        this.close();
      },
      error: () => {
        this.toastService.show(
          editing.id !== null ? "Failed to update group" : "Failed to create group",
          "error",
        );
        this.submitting.set(false);
      },
    });
  }
}
