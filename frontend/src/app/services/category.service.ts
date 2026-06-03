import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { Category } from "../types";

@Injectable({ providedIn: "root" })
export class CategoryService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/categories`);
  }
}
