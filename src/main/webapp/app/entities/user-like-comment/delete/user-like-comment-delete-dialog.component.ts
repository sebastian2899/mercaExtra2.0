import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserLikeComment } from '../user-like-comment.model';
import { UserLikeCommentService } from '../service/user-like-comment.service';

@Component({
  templateUrl: './user-like-comment-delete-dialog.component.html',
})
export class UserLikeCommentDeleteDialogComponent {
  userLikeComment?: IUserLikeComment;

  constructor(protected userLikeCommentService: UserLikeCommentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userLikeCommentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
