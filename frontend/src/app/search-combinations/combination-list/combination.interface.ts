import { Book } from './combination/book.interface';
import { Sign } from './sign.interface';

export interface Combination {
  id?: number;
  title: string;
  shortDescription: string;
  longDescription: string | null;
  originalTextCondition: string | null;
  author: string;
  source: Book | null;
  imagePath: string | null;
  signs: Sign[];
}
