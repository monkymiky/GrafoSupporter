enum classification {
  Sostanziale = 'S',
  Modificante = 'M',
  Accidentale = 'A',
}

export interface Sign {
  id: number;
  name: string;
  max: number;
  min: number;
  optional: boolean;
  classification: classification;
}
