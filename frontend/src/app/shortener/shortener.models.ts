export interface ShortenerRequestDto {
  longUrl: string;
}

export interface ShortenerResponseDto {
  longUrl: string,
  shortUrl: string,
  visits: number,
  createdAt: Date,
  expiresAt: Date
}
