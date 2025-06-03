enum classification {
  Sostanziale = 'S',
  Modificante = 'M',
  Accidentale = 'A',
}

interface Sign {
  id: number;
  name: string;
  max: number;
  min: number;
  optional: boolean;
  classification: classification;
}

interface Book {
  title: string;
  edition: number;
  editor: string;
  isbn: string;
}

export interface Combination {
  id?: number;
  title: string;
  description_short: string;
  description_long: string | null;
  original_text_condition: string | null;
  author: string;
  source: Book | null;
  imagePath: string | null;
  signs: Sign[];
}
