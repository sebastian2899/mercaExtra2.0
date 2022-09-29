import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserLikeComment, UserLikeComment } from '../user-like-comment.model';
import { UserLikeCommentService } from '../service/user-like-comment.service';

@Injectable({ providedIn: 'root' })
export class UserLikeCommentRoutingResolveService implements Resolve<IUserLikeComment> {
  constructor(protected service: UserLikeCommentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserLikeComment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userLikeComment: HttpResponse<UserLikeComment>) => {
          if (userLikeComment.body) {
            return of(userLikeComment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserLikeComment());
  }
}
