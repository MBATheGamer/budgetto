export type User = {
  "first-name": string;
  "last-name": string;
  email: string;
  role: "USER" | "ADMIN";
  status: "PENDING" | "ACTIVE" | "BLOCKED";
};

export type Category = {
  id: number | null;
  username: string;
  name: string;
  icon: string;
  type: "EXPENSE" | "INCOME";
  "is-default": boolean;
};

export type CategoryRequest = Omit<Category, "id" | "username" | "is-default">;
