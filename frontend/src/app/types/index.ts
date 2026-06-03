export type UserResponse = {
  "first-name": string;
  "last-name": string;
  email: string;
  role: "USER" | "ADMIN";
  status: "PENDING" | "ACTIVE" | "BLOCKED";
};

export type CategoryResponse = {
  id: number;
  username: string;
  name: string;
  icon: string;
  type: "EXPENSE" | "INCOME";
  "is-default": string;
};
