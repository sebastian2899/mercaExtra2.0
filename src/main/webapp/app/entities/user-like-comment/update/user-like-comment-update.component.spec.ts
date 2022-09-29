import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserLikeCommentService } from '../service/user-like-comment.service';
import { IUserLikeComment, UserLikeComment } from '../user-like-comment.model';

import { UserLikeCommentUpdateComponent } from './user-like-comment-update.component';

describe('UserLikeComment Management Update Component', () => {
  let comp: UserLikeCommentUpdateComponent;
  let fixture: ComponentFixture<UserLikeCommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userLikeCommentService: UserLikeCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserLikeCommentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserLikeCommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserLikeCommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userLikeCommentService = TestBed.inject(UserLikeCommentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userLikeComment: IUserLikeComment = { id: 456 };

      activatedRoute.data = of({ userLikeComment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(userLikeComment));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserLikeComment>>();
      const userLikeComment = { id: 123 };
      jest.spyOn(userLikeCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userLikeComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userLikeComment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(userLikeCommentService.update).toHaveBeenCalledWith(userLikeComment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserLikeComment>>();
      const userLikeComment = new UserLikeComment();
      jest.spyOn(userLikeCommentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userLikeComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userLikeComment }));
      saveSubject.complete();

      // THEN
      expect(userLikeCommentService.create).toHaveBeenCalledWith(userLikeComment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UserLikeComment>>();
      const userLikeComment = { id: 123 };
      jest.spyOn(userLikeCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userLikeComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userLikeCommentService.update).toHaveBeenCalledWith(userLikeComment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
