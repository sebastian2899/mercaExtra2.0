import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

import { ProductoPromocionHomeComponent } from './producto-promocion-home.component';

describe('ProductoPromocionHome Management Component', () => {
  let comp: ProductoPromocionHomeComponent;
  let fixture: ComponentFixture<ProductoPromocionHomeComponent>;
  let service: ProductoPromocionHomeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductoPromocionHomeComponent],
    })
      .overrideTemplate(ProductoPromocionHomeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoPromocionHomeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductoPromocionHomeService);

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
    expect(comp.productoPromocionHomes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
