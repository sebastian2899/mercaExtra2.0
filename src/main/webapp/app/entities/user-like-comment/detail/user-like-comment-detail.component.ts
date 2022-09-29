import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserLikeComment } from '../user-like-comment.model';

@Component({
  selector: 'jhi-user-like-comment-detail',
  templateUrl: './user-like-comment-detail.component.html',
})
export class UserLikeCommentDetailComponent implements OnInit {
  userLikeComment: IUserLikeComment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userLikeComment }) => {
      this.userLikeComment = userLikeComment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
