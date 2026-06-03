export type UserResponse = {
  "first-name": string;
  "last-name": string;
  email: string;
  role: "USER" | "ADMIN";
  status: "PENDING" | "ACTIVE" | "BLOCKED";
};
