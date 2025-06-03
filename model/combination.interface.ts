import { Book } from './book.interface';
import { Sign } from './sign.interface';

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
