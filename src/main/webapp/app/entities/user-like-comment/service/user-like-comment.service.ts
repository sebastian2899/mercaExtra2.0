import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserLikeComment, getUserLikeCommentIdentifier } from '../user-like-comment.model';

export type EntityResponseType = HttpResponse<IUserLikeComment>;
export type EntityArrayResponseType = HttpResponse<IUserLikeComment[]>;

@Injectable({ providedIn: 'root' })
export class UserLikeCommentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-like-comments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userLikeComment: IUserLikeComment): Observable<EntityResponseType> {
    return this.http.post<IUserLikeComment>(this.resourceUrl, userLikeComment, { observe: 'response' });
  }

  update(userLikeComment: IUserLikeComment): Observable<EntityResponseType> {
    return this.http.put<IUserLikeComment>(
      `${this.resourceUrl}/${getUserLikeCommentIdentifier(userLikeComment) as number}`,
      userLikeComment,
      { observe: 'response' }
    );
  }

  partialUpdate(userLikeComment: IUserLikeComment): Observable<EntityResponseType> {
    return this.http.patch<IUserLikeComment>(
      `${this.resourceUrl}/${getUserLikeCommentIdentifier(userLikeComment) as number}`,
      userLikeComment,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserLikeComment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserLikeComment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserLikeCommentToCollectionIfMissing(
    userLikeCommentCollection: IUserLikeComment[],
    ...userLikeCommentsToCheck: (IUserLikeComment | null | undefined)[]
  ): IUserLikeComment[] {
    const userLikeComments: IUserLikeComment[] = userLikeCommentsToCheck.filter(isPresent);
    if (userLikeComments.length > 0) {
      const userLikeCommentCollectionIdentifiers = userLikeCommentCollection.map(
        userLikeCommentItem => getUserLikeCommentIdentifier(userLikeCommentItem)!
      );
      const userLikeCommentsToAdd = userLikeComments.filter(userLikeCommentItem => {
        const userLikeCommentIdentifier = getUserLikeCommentIdentifier(userLikeCommentItem);
        if (userLikeCommentIdentifier == null || userLikeCommentCollectionIdentifiers.includes(userLikeCommentIdentifier)) {
          return false;
        }
        userLikeCommentCollectionIdentifiers.push(userLikeCommentIdentifier);
        return true;
      });
      return [...userLikeCommentsToAdd, ...userLikeCommentCollection];
    }
    return userLikeCommentCollection;
  }
}
