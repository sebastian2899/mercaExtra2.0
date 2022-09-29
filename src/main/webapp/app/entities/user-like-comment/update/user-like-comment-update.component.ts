import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUserLikeComment, UserLikeComment } from '../user-like-comment.model';
import { UserLikeCommentService } from '../service/user-like-comment.service';

@Component({
  selector: 'jhi-user-like-comment-update',
  templateUrl: './user-like-comment-update.component.html',
})
export class UserLikeCommentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idComment: [],
    login: [],
    isLike: [],
  });

  constructor(
    protected userLikeCommentService: UserLikeCommentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userLikeComment }) => {
      this.updateForm(userLikeComment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userLikeComment = this.createFromForm();
    if (userLikeComment.id !== undefined) {
      this.subscribeToSaveResponse(this.userLikeCommentService.update(userLikeComment));
    } else {
      this.subscribeToSaveResponse(this.userLikeCommentService.create(userLikeComment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserLikeComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userLikeComment: IUserLikeComment): void {
    this.editForm.patchValue({
      id: userLikeComment.id,
      idComment: userLikeComment.idComment,
      login: userLikeComment.login,
      isLike: userLikeComment.isLike,
    });
  }

  protected createFromForm(): IUserLikeComment {
    return {
      ...new UserLikeComment(),
      id: this.editForm.get(['id'])!.value,
      idComment: this.editForm.get(['idComment'])!.value,
      login: this.editForm.get(['login'])!.value,
      isLike: this.editForm.get(['isLike'])!.value,
    };
  }
}
