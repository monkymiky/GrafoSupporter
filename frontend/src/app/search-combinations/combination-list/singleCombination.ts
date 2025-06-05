import { SharedStateService } from '../../shared/shared-state.service';
import { Combination } from './combination.interface';
import { Book } from './combination/book.interface';
import { Sign } from './sign.interface';

export class singleCombinationImpl implements Combination {
  private stateService: SharedStateService;
  id: number;
  title: string = '';
  description_short: string = '';
  longDescription: string | null = null;
  originalTextCondition: string | null = null;
  author: string = '';
  source: Book | null = null;
  imagePath: string | null = null;
  signs: Sign[] = [];

  constructor(id: number, stateService: SharedStateService) {
    this.stateService = stateService;
    this.id = id;
    for (let comb of this.stateService.getcombinations()()) {
      if (comb.id == this.id) {
        this.title = comb.title;
        this.description_short = comb.description_short;
        this.longDescription = comb.description_long;
        this.originalTextCondition = comb.original_text_condition;
        this.author = comb.author;
        this.source = comb.source;
        this.imagePath = comb.imagePath;
        this.signs = comb.signs;
      }
    }
  }
}
