import { Component, ElementRef, input, output, viewChild } from "@angular/core";

@Component({
  selector: "delete-modal",
  imports: [],
  templateUrl: "./index.html",
})
export class DeleteModal {
  title = input("");
  content = input("");
  confirmDelete = output();

  private dialogRef = viewChild<ElementRef<HTMLDialogElement>>("nativeDialog");

  open() {
    this.dialogRef()?.nativeElement.showModal();
  }

  onConfirm() {
    this.confirmDelete.emit();
    this.dialogRef()?.nativeElement.close();
  }
}
