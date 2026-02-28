export enum classification {
  Sostanziale = 'S',
  Modificante = 'M',
  Accidentale = 'A',
}

export interface Sign {
  signId: number;
  name: string;
  max: number;
  min: number;
  isOptional: boolean;
  classification: classification;
  temperamento: string | null;
}
