import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductoPromocionHomeDetailComponent } from './producto-promocion-home-detail.component';

describe('ProductoPromocionHome Management Detail Component', () => {
  let comp: ProductoPromocionHomeDetailComponent;
  let fixture: ComponentFixture<ProductoPromocionHomeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductoPromocionHomeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productoPromocionHome: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductoPromocionHomeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductoPromocionHomeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productoPromocionHome on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productoPromocionHome).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
