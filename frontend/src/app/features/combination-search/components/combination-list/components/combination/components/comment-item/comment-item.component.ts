import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Comment } from '../../../../../../model/comment.interface';
import { CommentService } from '../../../../../../services/comment.service';
import { AuthService } from '../../../../../../../../core/services/auth.service';
import { MessageService } from '../../../../../../../../shared/components/message-toast/services/message.service';
import { MessageType } from '../../../../../../../../shared/components/message-toast/models/message.interface';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.scss',
})
export class CommentItemComponent {
  @Input() comment!: Comment;
  @Input() combinationId!: number;
  @Output() commentAdded = new EventEmitter<Comment>();

  private readonly commentService = inject(CommentService);
  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);

  showReplyForm = false;
  replyContent = '';
  isSubmitting = false;

  toggleReplyForm(): void {
    this.showReplyForm = !this.showReplyForm;
    if (!this.showReplyForm) {
      this.replyContent = '';
    }
  }

  submitReply(): void {
    if (!this.replyContent.trim() || this.isSubmitting) return;

    this.isSubmitting = true;
    this.commentService
      .addComment(this.combinationId, this.replyContent.trim(), this.comment.id)
      .subscribe({
        next: (newReply) => {
          this.commentAdded.emit(newReply);
          this.replyContent = '';
          this.showReplyForm = false;
          this.isSubmitting = false;
        },
        error: (err) => {
          this.messageService.showMessage(
            `Errore nell'aggiunta della risposta: ${err.error?.message || err.message}`,
            MessageType.error
          );
          this.isSubmitting = false;
        },
      });
  }


  get isAuthenticated(): boolean {
    return this.authService.isAuthenticated;
  }

  onImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img) {
      img.style.display = 'none';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'adesso';
    if (diffMins < 60) return `${diffMins} min fa`;
    if (diffHours < 24) return `${diffHours} ore fa`;
    if (diffDays < 7) return `${diffDays} giorni fa`;
    return date.toLocaleDateString('it-IT');
  }
}
