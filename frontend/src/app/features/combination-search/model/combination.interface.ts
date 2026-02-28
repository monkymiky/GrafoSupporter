import { Book } from './book.interface';
import { Sign } from './sign.interface';

export interface Combination {
  id?: number;
  title: string;
  shortDescription: string;
  longDescription: string | null;
  originalTextCondition: string | null;
  author: string;
  sourceBook: Book | null;
  imagePath: string | null | File;
  signs: Sign[];
}
