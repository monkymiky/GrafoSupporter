import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../model/comment.interface';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  readonly apiUrl = `${environment.apiUrl}/combinations`;
  readonly http = inject(HttpClient);

  getComments(combinationId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/${combinationId}/comments`);
  }

  addComment(
    combinationId: number,
    content: string,
    parentCommentId?: number
  ): Observable<Comment> {
    const requestBody: { content: string; parentCommentId?: number } = {
      content,
    };
    if (parentCommentId) {
      requestBody.parentCommentId = parentCommentId;
    }
    return this.http.post<Comment>(
      `${this.apiUrl}/${combinationId}/comments`,
      requestBody
    );
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/comments/${commentId}`);
  }
}
