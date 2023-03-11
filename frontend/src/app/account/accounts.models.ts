export interface AuthRequestDto {
  username: string,
  password: string
}

export interface AuthResponseDto {
  token: string;
}

export interface RegisterRequestDto {
  email: string,
  firstName: string,
  lastName: string,
  password: string
}

export interface RegisterResponseDto {
  id: string,
  email: string,
  firstName: string,
  lastName: string,
  visits: number
}
