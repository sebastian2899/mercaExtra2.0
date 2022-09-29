import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserLikeCommentDetailComponent } from './user-like-comment-detail.component';

describe('UserLikeComment Management Detail Component', () => {
  let comp: UserLikeCommentDetailComponent;
  let fixture: ComponentFixture<UserLikeCommentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserLikeCommentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userLikeComment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserLikeCommentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserLikeCommentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userLikeComment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userLikeComment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
