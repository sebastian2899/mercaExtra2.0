import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductoFavoritosDetailComponent } from './producto-favoritos-detail.component';

describe('ProductoFavoritos Management Detail Component', () => {
  let comp: ProductoFavoritosDetailComponent;
  let fixture: ComponentFixture<ProductoFavoritosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductoFavoritosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productoFavoritos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductoFavoritosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductoFavoritosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productoFavoritos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productoFavoritos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
