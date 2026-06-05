import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { SharedBudget, SharedBudgetRequest } from "../types";

@Injectable({ providedIn: "root" })
export class SharedBudgetService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<SharedBudget[]> {
    return this.http.get<SharedBudget[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets`);
  }

  public getById(id: number): Observable<SharedBudget> {
    return this.http.get<SharedBudget>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}`,
    );
  }

  public create(body: SharedBudgetRequest): Observable<SharedBudget> {
    return this.http.post<SharedBudget>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets`,
      body,
    );
  }

  public update(id: number, body: SharedBudgetRequest): Observable<SharedBudget> {
    return this.http.put<SharedBudget>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}`,
      body,
    );
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}`);
  }

  public addMember(id: number, email: string): Observable<SharedBudget> {
    return this.http.post<SharedBudget>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}/members`,
      { email },
    );
  }

  public removeMember(id: number, memberId: number): Observable<void> {
    return this.http.delete<void>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}/members/${memberId}`,
    );
  }

  public updateMemberStatus(id: number, memberId: number, status: string): Observable<void> {
    return this.http.post<void>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/shared-budgets/${id}/members/${memberId}`,
      {
        status,
      },
    );
  }
}
