import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductoFavoritos, ProductoFavoritos } from '../producto-favoritos.model';

import { ProductoFavoritosService } from './producto-favoritos.service';

describe('ProductoFavoritos Service', () => {
  let service: ProductoFavoritosService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductoFavoritos;
  let expectedResult: IProductoFavoritos | IProductoFavoritos[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductoFavoritosService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idProduct: 0,
      login: 'AAAAAAA',
      lastUpdate: currentDate,
      estado: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ProductoFavoritos', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.create(new ProductoFavoritos()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductoFavoritos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idProduct: 1,
          login: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductoFavoritos', () => {
      const patchObject = Object.assign(
        {
          idProduct: 1,
          login: 'BBBBBB',
          estado: 'BBBBBB',
        },
        new ProductoFavoritos()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductoFavoritos', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idProduct: 1,
          login: 'BBBBBB',
          lastUpdate: currentDate.format(DATE_TIME_FORMAT),
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          lastUpdate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ProductoFavoritos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductoFavoritosToCollectionIfMissing', () => {
      it('should add a ProductoFavoritos to an empty array', () => {
        const productoFavoritos: IProductoFavoritos = { id: 123 };
        expectedResult = service.addProductoFavoritosToCollectionIfMissing([], productoFavoritos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoFavoritos);
      });

      it('should not add a ProductoFavoritos to an array that contains it', () => {
        const productoFavoritos: IProductoFavoritos = { id: 123 };
        const productoFavoritosCollection: IProductoFavoritos[] = [
          {
            ...productoFavoritos,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductoFavoritosToCollectionIfMissing(productoFavoritosCollection, productoFavoritos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductoFavoritos to an array that doesn't contain it", () => {
        const productoFavoritos: IProductoFavoritos = { id: 123 };
        const productoFavoritosCollection: IProductoFavoritos[] = [{ id: 456 }];
        expectedResult = service.addProductoFavoritosToCollectionIfMissing(productoFavoritosCollection, productoFavoritos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoFavoritos);
      });

      it('should add only unique ProductoFavoritos to an array', () => {
        const productoFavoritosArray: IProductoFavoritos[] = [{ id: 123 }, { id: 456 }, { id: 14408 }];
        const productoFavoritosCollection: IProductoFavoritos[] = [{ id: 123 }];
        expectedResult = service.addProductoFavoritosToCollectionIfMissing(productoFavoritosCollection, ...productoFavoritosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productoFavoritos: IProductoFavoritos = { id: 123 };
        const productoFavoritos2: IProductoFavoritos = { id: 456 };
        expectedResult = service.addProductoFavoritosToCollectionIfMissing([], productoFavoritos, productoFavoritos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoFavoritos);
        expect(expectedResult).toContain(productoFavoritos2);
      });

      it('should accept null and undefined values', () => {
        const productoFavoritos: IProductoFavoritos = { id: 123 };
        expectedResult = service.addProductoFavoritosToCollectionIfMissing([], null, productoFavoritos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoFavoritos);
      });

      it('should return initial array if no ProductoFavoritos is added', () => {
        const productoFavoritosCollection: IProductoFavoritos[] = [{ id: 123 }];
        expectedResult = service.addProductoFavoritosToCollectionIfMissing(productoFavoritosCollection, undefined, null);
        expect(expectedResult).toEqual(productoFavoritosCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
