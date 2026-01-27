import { Component, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';
import { Comment } from '../../../../../../model/comment.interface';
import { CommentService } from '../../../../../../services/comment.service';
import { AuthService } from '../../../../../../../../core/services/auth.service';
import { MessageService } from '../../../../../../../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../../../../../../../shared/components/message-toast/models/message.interface';
import { CommentItemComponent } from '../comment-item/comment-item.component';

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [CommonModule, FormsModule, NgbAccordionModule, CommentItemComponent],
  templateUrl: './comment-section.component.html',
  styleUrl: './comment-section.component.scss',
})
export class CommentSectionComponent implements OnInit {
  @Input() combinationId!: number;

  private readonly commentService = inject(CommentService);
  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);

  comments: Comment[] = [];
  isLoading = false;
  isOpen = false;
  newCommentContent = '';
  isSubmitting = false;

  ngOnInit(): void {
    if (this.isOpen) {
      this.loadComments();
    }
  }

  toggleSection(): void {
    this.isOpen = !this.isOpen;
    if (this.isOpen && this.comments.length === 0) {
      this.loadComments();
    }
  }

  loadComments(): void {
    if (this.isLoading) return;

    this.isLoading = true;
    this.commentService.getComments(this.combinationId).subscribe({
      next: (comments) => {
        this.comments = comments;
        this.isLoading = false;
      },
      error: (err) => {
        this.messageService.showMessage(
          `Errore nel caricamento dei commenti: ${err.error?.message || err.message}`,
          MessageType.error
        );
        this.isLoading = false;
      },
    });
  }

  submitComment(): void {
    if (!this.newCommentContent.trim() || this.isSubmitting) return;

    this.isSubmitting = true;
    this.commentService.addComment(this.combinationId, this.newCommentContent.trim()).subscribe({
      next: () => {
        this.newCommentContent = '';
        this.isSubmitting = false;
        this.loadComments();
      },
      error: (err) => {
        this.messageService.showMessage(
          `Errore nell'aggiunta del commento: ${err.error?.message || err.message}`,
          MessageType.error
        );
        this.isSubmitting = false;
      },
    });
  }

  onCommentAdded(comment: Comment): void {
    this.loadComments();
  }

  get isAuthenticated(): boolean {
    return this.authService.isAuthenticated;
  }
}
