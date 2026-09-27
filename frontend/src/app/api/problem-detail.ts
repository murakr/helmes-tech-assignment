export interface ProblemDetail {
  readonly title: string;
  readonly status: number;
  readonly detail?: string;
  readonly errors?: Readonly<Record<string, string>>;
}
