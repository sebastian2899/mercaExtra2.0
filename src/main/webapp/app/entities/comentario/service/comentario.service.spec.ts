import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComentario, Comentario } from '../comentario.model';

import { ComentarioService } from './comentario.service';

describe('Comentario Service', () => {
  let service: ComentarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IComentario;
  let expectedResult: IComentario | IComentario[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComentarioService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idComentario: 0,
      fechaComentario: currentDate,
      login: 'AAAAAAA',
      like: 0,
      descripcion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaComentario: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Comentario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaComentario: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaComentario: currentDate,
        },
        returnedFromService
      );

      service.create(new Comentario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Comentario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idComentario: 1,
          fechaComentario: currentDate.format(DATE_TIME_FORMAT),
          login: 'BBBBBB',
          like: 1,
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaComentario: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Comentario', () => {
      const patchObject = Object.assign(
        {
          login: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        new Comentario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaComentario: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Comentario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idComentario: 1,
          fechaComentario: currentDate.format(DATE_TIME_FORMAT),
          login: 'BBBBBB',
          like: 1,
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaComentario: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Comentario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComentarioToCollectionIfMissing', () => {
      it('should add a Comentario to an empty array', () => {
        const comentario: IComentario = { id: 123 };
        expectedResult = service.addComentarioToCollectionIfMissing([], comentario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comentario);
      });

      it('should not add a Comentario to an array that contains it', () => {
        const comentario: IComentario = { id: 123 };
        const comentarioCollection: IComentario[] = [
          {
            ...comentario,
          },
          { id: 456 },
        ];
        expectedResult = service.addComentarioToCollectionIfMissing(comentarioCollection, comentario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Comentario to an array that doesn't contain it", () => {
        const comentario: IComentario = { id: 123 };
        const comentarioCollection: IComentario[] = [{ id: 456 }];
        expectedResult = service.addComentarioToCollectionIfMissing(comentarioCollection, comentario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comentario);
      });

      it('should add only unique Comentario to an array', () => {
        const comentarioArray: IComentario[] = [{ id: 123 }, { id: 456 }, { id: 22027 }];
        const comentarioCollection: IComentario[] = [{ id: 123 }];
        expectedResult = service.addComentarioToCollectionIfMissing(comentarioCollection, ...comentarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const comentario: IComentario = { id: 123 };
        const comentario2: IComentario = { id: 456 };
        expectedResult = service.addComentarioToCollectionIfMissing([], comentario, comentario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comentario);
        expect(expectedResult).toContain(comentario2);
      });

      it('should accept null and undefined values', () => {
        const comentario: IComentario = { id: 123 };
        expectedResult = service.addComentarioToCollectionIfMissing([], null, comentario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comentario);
      });

      it('should return initial array if no Comentario is added', () => {
        const comentarioCollection: IComentario[] = [{ id: 123 }];
        expectedResult = service.addComentarioToCollectionIfMissing(comentarioCollection, undefined, null);
        expect(expectedResult).toEqual(comentarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
