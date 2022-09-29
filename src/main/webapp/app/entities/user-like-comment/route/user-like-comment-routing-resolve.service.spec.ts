import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUserLikeComment, UserLikeComment } from '../user-like-comment.model';
import { UserLikeCommentService } from '../service/user-like-comment.service';

import { UserLikeCommentRoutingResolveService } from './user-like-comment-routing-resolve.service';

describe('UserLikeComment routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UserLikeCommentRoutingResolveService;
  let service: UserLikeCommentService;
  let resultUserLikeComment: IUserLikeComment | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(UserLikeCommentRoutingResolveService);
    service = TestBed.inject(UserLikeCommentService);
    resultUserLikeComment = undefined;
  });

  describe('resolve', () => {
    it('should return IUserLikeComment returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserLikeComment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserLikeComment).toEqual({ id: 123 });
    });

    it('should return new IUserLikeComment if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserLikeComment = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUserLikeComment).toEqual(new UserLikeComment());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserLikeComment })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUserLikeComment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserLikeComment).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
