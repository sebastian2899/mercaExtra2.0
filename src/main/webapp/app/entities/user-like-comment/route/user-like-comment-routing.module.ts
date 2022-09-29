import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserLikeCommentComponent } from '../list/user-like-comment.component';
import { UserLikeCommentDetailComponent } from '../detail/user-like-comment-detail.component';
import { UserLikeCommentUpdateComponent } from '../update/user-like-comment-update.component';
import { UserLikeCommentRoutingResolveService } from './user-like-comment-routing-resolve.service';

const userLikeCommentRoute: Routes = [
  {
    path: '',
    component: UserLikeCommentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserLikeCommentDetailComponent,
    resolve: {
      userLikeComment: UserLikeCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserLikeCommentUpdateComponent,
    resolve: {
      userLikeComment: UserLikeCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserLikeCommentUpdateComponent,
    resolve: {
      userLikeComment: UserLikeCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userLikeCommentRoute)],
  exports: [RouterModule],
})
export class UserLikeCommentRoutingModule {}
