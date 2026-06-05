import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { env } from "../../env";
import { Transaction, TransactionRequest } from "../types";

@Injectable({ providedIn: "root" })
export class TransactionService {
  private readonly http = inject(HttpClient);

  public getAll(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${env["BASE_URL"]}${env["API_VERSION"]}/transactions`);
  }

  public getById(id: number): Observable<Transaction> {
    return this.http.get<Transaction>(`${env["BASE_URL"]}${env["API_VERSION"]}/transactions/${id}`);
  }

  public create(body: TransactionRequest): Observable<Transaction> {
    return this.http.post<Transaction>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/transactions`,
      body,
    );
  }

  public update(id: number, body: TransactionRequest): Observable<Transaction> {
    return this.http.put<Transaction>(
      `${env["BASE_URL"]}${env["API_VERSION"]}/transactions/${id}`,
      body,
    );
  }

  public delete(id: number) {
    return this.http.delete(`${env["BASE_URL"]}${env["API_VERSION"]}/transactions/${id}`);
  }
}
