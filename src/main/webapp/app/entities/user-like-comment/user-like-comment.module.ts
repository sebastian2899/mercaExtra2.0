import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserLikeCommentComponent } from './list/user-like-comment.component';
import { UserLikeCommentDetailComponent } from './detail/user-like-comment-detail.component';
import { UserLikeCommentUpdateComponent } from './update/user-like-comment-update.component';
import { UserLikeCommentDeleteDialogComponent } from './delete/user-like-comment-delete-dialog.component';
import { UserLikeCommentRoutingModule } from './route/user-like-comment-routing.module';

@NgModule({
  imports: [SharedModule, UserLikeCommentRoutingModule],
  declarations: [
    UserLikeCommentComponent,
    UserLikeCommentDetailComponent,
    UserLikeCommentUpdateComponent,
    UserLikeCommentDeleteDialogComponent,
  ],
  entryComponents: [UserLikeCommentDeleteDialogComponent],
})
export class UserLikeCommentModule {}
