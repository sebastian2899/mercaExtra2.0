import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';
import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';

import { ProductoPromocionHomeUpdateComponent } from './producto-promocion-home-update.component';

describe('ProductoPromocionHome Management Update Component', () => {
  let comp: ProductoPromocionHomeUpdateComponent;
  let fixture: ComponentFixture<ProductoPromocionHomeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoPromocionHomeService: ProductoPromocionHomeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductoPromocionHomeUpdateComponent],
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
      .overrideTemplate(ProductoPromocionHomeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoPromocionHomeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoPromocionHomeService = TestBed.inject(ProductoPromocionHomeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productoPromocionHome: IProductoPromocionHome = { id: 456 };

      activatedRoute.data = of({ productoPromocionHome });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productoPromocionHome));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoPromocionHome>>();
      const productoPromocionHome = { id: 123 };
      jest.spyOn(productoPromocionHomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoPromocionHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productoPromocionHome }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoPromocionHomeService.update).toHaveBeenCalledWith(productoPromocionHome);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoPromocionHome>>();
      const productoPromocionHome = new ProductoPromocionHome();
      jest.spyOn(productoPromocionHomeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoPromocionHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productoPromocionHome }));
      saveSubject.complete();

      // THEN
      expect(productoPromocionHomeService.create).toHaveBeenCalledWith(productoPromocionHome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductoPromocionHome>>();
      const productoPromocionHome = { id: 123 };
      jest.spyOn(productoPromocionHomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productoPromocionHome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoPromocionHomeService.update).toHaveBeenCalledWith(productoPromocionHome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
