export interface IUserLikeComment {
  id?: number;
  idComment?: number | null;
  login?: string | null;
  isLike?: boolean | null;
}

export class UserLikeComment implements IUserLikeComment {
  constructor(public id?: number, public idComment?: number | null, public login?: string | null, public isLike?: boolean | null) {
    this.isLike = this.isLike ?? false;
  }
}

export function getUserLikeCommentIdentifier(userLikeComment: IUserLikeComment): number | undefined {
  return userLikeComment.id;
}
