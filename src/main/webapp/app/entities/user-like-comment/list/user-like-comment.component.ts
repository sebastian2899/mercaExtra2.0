import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserLikeComment } from '../user-like-comment.model';
import { UserLikeCommentService } from '../service/user-like-comment.service';
import { UserLikeCommentDeleteDialogComponent } from '../delete/user-like-comment-delete-dialog.component';

@Component({
  selector: 'jhi-user-like-comment',
  templateUrl: './user-like-comment.component.html',
})
export class UserLikeCommentComponent implements OnInit {
  userLikeComments?: IUserLikeComment[];
  isLoading = false;

  constructor(protected userLikeCommentService: UserLikeCommentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.userLikeCommentService.query().subscribe({
      next: (res: HttpResponse<IUserLikeComment[]>) => {
        this.isLoading = false;
        this.userLikeComments = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUserLikeComment): number {
    return item.id!;
  }

  delete(userLikeComment: IUserLikeComment): void {
    const modalRef = this.modalService.open(UserLikeCommentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userLikeComment = userLikeComment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
