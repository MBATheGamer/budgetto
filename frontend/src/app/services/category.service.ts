import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { Category, CategoryRequest } from "../types";

@Injectable({ providedIn: "root" })
export class CategoryService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/categories`);
  }

  public create(body: CategoryRequest): Observable<Category> {
    return this.http.post<Category>(`${env["BASE_URL"]}${env["API_VERSION"]}/categories`, body);
  }

  public update(categoryId: number, body: CategoryRequest): Observable<Category> {
    return this.http.put<Category>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/categories/${categoryId}`,
      body,
    );
  }

  public delete(categoryId: number) {
    return this.http.delete(`${env["BASE_URL"]}${env["API_VERSION"]}/categories/${categoryId}`);
  }
}
