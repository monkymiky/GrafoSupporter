import { Author } from "./author.interface";
import { Book } from "./book.interface";
import { Sign } from "./sign.interface";

export interface VoteStats {
  upvotes: number;
  downvotes: number;
  score: number;
  userVote: 'UP' | 'DOWN' | null;
}

export interface Combination {
  id?: number;
  title: string;
  shortDescription: string;
  longDescription: string | null;
  originalTextCondition: string | null;
  author: Author | null;
  sourceBook: Book | null;
  imagePath: string | null | File;
  signs: Sign[];
  voteStats?: VoteStats;
}
