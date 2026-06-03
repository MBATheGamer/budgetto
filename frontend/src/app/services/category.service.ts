import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { CategoryResponse } from "../types";

@Injectable({ providedIn: "root" })
export class CategoryService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/categories`);
  }
}
