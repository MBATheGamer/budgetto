import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { Budget, BudgetRequest } from "../types";

@Injectable({ providedIn: "root" })
export class BudgetService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<Budget[]> {
    return this.http.get<Budget[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/budgets`);
  }

  public create(body: BudgetRequest): Observable<Budget> {
    return this.http.post<Budget>(`${env["BASE_URL"]}${env["API_VERSION"]}/budgets`, body);
  }

  public update(id: number, body: BudgetRequest): Observable<Budget> {
    return this.http.put<Budget>(`${env["BASE_URL"]}${env["API_VERSION"]}/budgets/${id}`, body);
  }

  public delete(id: number) {
    return this.http.delete(`${env["BASE_URL"]}${env["API_VERSION"]}/budgets/${id}`);
  }
}
