import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserLikeCommentService } from '../service/user-like-comment.service';

import { UserLikeCommentComponent } from './user-like-comment.component';

describe('UserLikeComment Management Component', () => {
  let comp: UserLikeCommentComponent;
  let fixture: ComponentFixture<UserLikeCommentComponent>;
  let service: UserLikeCommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UserLikeCommentComponent],
    })
      .overrideTemplate(UserLikeCommentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserLikeCommentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UserLikeCommentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.userLikeComments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
