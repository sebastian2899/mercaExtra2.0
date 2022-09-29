import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserLikeComment, UserLikeComment } from '../user-like-comment.model';

import { UserLikeCommentService } from './user-like-comment.service';

describe('UserLikeComment Service', () => {
  let service: UserLikeCommentService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserLikeComment;
  let expectedResult: IUserLikeComment | IUserLikeComment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserLikeCommentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idComment: 0,
      login: 'AAAAAAA',
      isLike: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a UserLikeComment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UserLikeComment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserLikeComment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idComment: 1,
          login: 'BBBBBB',
          isLike: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserLikeComment', () => {
      const patchObject = Object.assign(
        {
          isLike: true,
        },
        new UserLikeComment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserLikeComment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idComment: 1,
          login: 'BBBBBB',
          isLike: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a UserLikeComment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserLikeCommentToCollectionIfMissing', () => {
      it('should add a UserLikeComment to an empty array', () => {
        const userLikeComment: IUserLikeComment = { id: 123 };
        expectedResult = service.addUserLikeCommentToCollectionIfMissing([], userLikeComment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userLikeComment);
      });

      it('should not add a UserLikeComment to an array that contains it', () => {
        const userLikeComment: IUserLikeComment = { id: 123 };
        const userLikeCommentCollection: IUserLikeComment[] = [
          {
            ...userLikeComment,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserLikeCommentToCollectionIfMissing(userLikeCommentCollection, userLikeComment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserLikeComment to an array that doesn't contain it", () => {
        const userLikeComment: IUserLikeComment = { id: 123 };
        const userLikeCommentCollection: IUserLikeComment[] = [{ id: 456 }];
        expectedResult = service.addUserLikeCommentToCollectionIfMissing(userLikeCommentCollection, userLikeComment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userLikeComment);
      });

      it('should add only unique UserLikeComment to an array', () => {
        const userLikeCommentArray: IUserLikeComment[] = [{ id: 123 }, { id: 456 }, { id: 72512 }];
        const userLikeCommentCollection: IUserLikeComment[] = [{ id: 123 }];
        expectedResult = service.addUserLikeCommentToCollectionIfMissing(userLikeCommentCollection, ...userLikeCommentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userLikeComment: IUserLikeComment = { id: 123 };
        const userLikeComment2: IUserLikeComment = { id: 456 };
        expectedResult = service.addUserLikeCommentToCollectionIfMissing([], userLikeComment, userLikeComment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userLikeComment);
        expect(expectedResult).toContain(userLikeComment2);
      });

      it('should accept null and undefined values', () => {
        const userLikeComment: IUserLikeComment = { id: 123 };
        expectedResult = service.addUserLikeCommentToCollectionIfMissing([], null, userLikeComment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userLikeComment);
      });

      it('should return initial array if no UserLikeComment is added', () => {
        const userLikeCommentCollection: IUserLikeComment[] = [{ id: 123 }];
        expectedResult = service.addUserLikeCommentToCollectionIfMissing(userLikeCommentCollection, undefined, null);
        expect(expectedResult).toEqual(userLikeCommentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
