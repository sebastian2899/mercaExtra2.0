import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductoFavoritosService } from '../service/producto-favoritos.service';
import { IProductoFavoritos, ProductoFavoritos } from '../producto-favoritos.model';

import { ProductoFavoritosUpdateComponent } from './producto-favoritos-update.component';

describe('ProductoFavoritos Management Update Component', () => {
  let comp: ProductoFavoritosUpdateComponent;
  let fixture: ComponentFixture<ProductoFavoritosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoFavoritosService: ProductoFavoritosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductoFavoritosUpdateComponent],
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
      .overrideTemplate(ProductoFavoritosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoFavoritosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoFavoritosService = TestBed.inject(ProductoFavoritosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productoFavoritos: IProductoFavoritos = { id: 456 };

      activatedRoute.data = of({ productoFavoritos });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productoFavoritos));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoFavoritos>>();
      const productoFavoritos = { id: 123 };
      jest.spyOn(productoFavoritosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoFavoritos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productoFavoritos }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoFavoritosService.update).toHaveBeenCalledWith(productoFavoritos);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoFavoritos>>();
      const productoFavoritos = new ProductoFavoritos();
      jest.spyOn(productoFavoritosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoFavoritos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productoFavoritos }));
      saveSubject.complete();

      // THEN
      expect(productoFavoritosService.create).toHaveBeenCalledWith(productoFavoritos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoFavoritos>>();
      const productoFavoritos = { id: 123 };
      jest.spyOn(productoFavoritosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoFavoritos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoFavoritosService.update).toHaveBeenCalledWith(productoFavoritos);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
