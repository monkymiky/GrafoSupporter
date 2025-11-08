export enum Grado {
  ASSENTE, // 0
  BASSO, // 1
  SOTTO_MEDIA, // 2
  MEDIO, // 3
  SOPRA_MEDIA, // 4
  ALTO, // 5
}

export type FilterMap = Map<number, Grado>;
