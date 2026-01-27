import { Author } from '../../features/combination-search/model/author.interface';
import { Comment } from '../../features/combination-search/model/comment.interface';

export enum NotificationType {
  REPLY_TO_COMMENT = 'REPLY_TO_COMMENT',
  COMMENT_ON_COMBINATION = 'COMMENT_ON_COMBINATION'
}

export interface Notification {
  id: number;
  type: NotificationType;
  read: boolean;
  createdAt: string;
  comment?: Comment;
  combination?: { id: number; title: string };
  sender: Author;
}
