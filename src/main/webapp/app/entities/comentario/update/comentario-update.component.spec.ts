import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComentarioService } from '../service/comentario.service';
import { IComentario, Comentario } from '../comentario.model';

import { ComentarioUpdateComponent } from './comentario-update.component';

describe('Comentario Management Update Component', () => {
  let comp: ComentarioUpdateComponent;
  let fixture: ComponentFixture<ComentarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comentarioService: ComentarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComentarioUpdateComponent],
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
      .overrideTemplate(ComentarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComentarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comentarioService = TestBed.inject(ComentarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const comentario: IComentario = { id: 456 };

      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(comentario));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Comentario>>();
      const comentario = { id: 123 };
      jest.spyOn(comentarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comentario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(comentarioService.update).toHaveBeenCalledWith(comentario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Comentario>>();
      const comentario = new Comentario();
      jest.spyOn(comentarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comentario }));
      saveSubject.complete();

      // THEN
      expect(comentarioService.create).toHaveBeenCalledWith(comentario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Comentario>>();
      const comentario = { id: 123 };
      jest.spyOn(comentarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comentario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comentarioService.update).toHaveBeenCalledWith(comentario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
