import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';

import { ProductoPromocionHomeService } from './producto-promocion-home.service';

describe('ProductoPromocionHome Service', () => {
  let service: ProductoPromocionHomeService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductoPromocionHome;
  let expectedResult: IProductoPromocionHome | IProductoPromocionHome[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductoPromocionHomeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idProducto: 0,
      descripcion: 'AAAAAAA',
      fechaAgregado: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ProductoPromocionHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAgregado: currentDate,
        },
        returnedFromService
      );

      service.create(new ProductoPromocionHome()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductoPromocionHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idProducto: 1,
          descripcion: 'BBBBBB',
          fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAgregado: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductoPromocionHome', () => {
      const patchObject = Object.assign(
        {
          idProducto: 1,
          descripcion: 'BBBBBB',
          fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
        },
        new ProductoPromocionHome()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaAgregado: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductoPromocionHome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idProducto: 1,
          descripcion: 'BBBBBB',
          fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaAgregado: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ProductoPromocionHome', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductoPromocionHomeToCollectionIfMissing', () => {
      it('should add a ProductoPromocionHome to an empty array', () => {
        const productoPromocionHome: IProductoPromocionHome = { id: 123 };
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing([], productoPromocionHome);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoPromocionHome);
      });

      it('should not add a ProductoPromocionHome to an array that contains it', () => {
        const productoPromocionHome: IProductoPromocionHome = { id: 123 };
        const productoPromocionHomeCollection: IProductoPromocionHome[] = [
          {
            ...productoPromocionHome,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing(productoPromocionHomeCollection, productoPromocionHome);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductoPromocionHome to an array that doesn't contain it", () => {
        const productoPromocionHome: IProductoPromocionHome = { id: 123 };
        const productoPromocionHomeCollection: IProductoPromocionHome[] = [{ id: 456 }];
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing(productoPromocionHomeCollection, productoPromocionHome);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoPromocionHome);
      });

      it('should add only unique ProductoPromocionHome to an array', () => {
        const productoPromocionHomeArray: IProductoPromocionHome[] = [{ id: 123 }, { id: 456 }, { id: 20959 }];
        const productoPromocionHomeCollection: IProductoPromocionHome[] = [{ id: 123 }];
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing(
          productoPromocionHomeCollection,
          ...productoPromocionHomeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productoPromocionHome: IProductoPromocionHome = { id: 123 };
        const productoPromocionHome2: IProductoPromocionHome = { id: 456 };
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing([], productoPromocionHome, productoPromocionHome2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoPromocionHome);
        expect(expectedResult).toContain(productoPromocionHome2);
      });

      it('should accept null and undefined values', () => {
        const productoPromocionHome: IProductoPromocionHome = { id: 123 };
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing([], null, productoPromocionHome, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoPromocionHome);
      });

      it('should return initial array if no ProductoPromocionHome is added', () => {
        const productoPromocionHomeCollection: IProductoPromocionHome[] = [{ id: 123 }];
        expectedResult = service.addProductoPromocionHomeToCollectionIfMissing(productoPromocionHomeCollection, undefined, null);
        expect(expectedResult).toEqual(productoPromocionHomeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
