export interface UserProfileRequest {
  readonly name: string;
  readonly sectorIds: readonly number[];
  readonly agreedToTerms: boolean;
}

export interface UserProfile extends UserProfileRequest {
  readonly id: number;
}
