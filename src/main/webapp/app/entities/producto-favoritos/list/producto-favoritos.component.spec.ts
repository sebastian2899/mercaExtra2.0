import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductoFavoritosService } from '../service/producto-favoritos.service';

import { ProductoFavoritosComponent } from './producto-favoritos.component';

describe('ProductoFavoritos Management Component', () => {
  let comp: ProductoFavoritosComponent;
  let fixture: ComponentFixture<ProductoFavoritosComponent>;
  let service: ProductoFavoritosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductoFavoritosComponent],
    })
      .overrideTemplate(ProductoFavoritosComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoFavoritosComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductoFavoritosService);

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
    expect(comp.productoFavoritos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
