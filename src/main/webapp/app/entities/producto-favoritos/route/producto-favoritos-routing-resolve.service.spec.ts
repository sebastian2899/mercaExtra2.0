import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProductoFavoritos, ProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';

import { ProductoFavoritosRoutingResolveService } from './producto-favoritos-routing-resolve.service';

describe('ProductoFavoritos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductoFavoritosRoutingResolveService;
  let service: ProductoFavoritosService;
  let resultProductoFavoritos: IProductoFavoritos | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ProductoFavoritosRoutingResolveService);
    service = TestBed.inject(ProductoFavoritosService);
    resultProductoFavoritos = undefined;
  });

  describe('resolve', () => {
    it('should return IProductoFavoritos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoFavoritos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoFavoritos).toEqual({ id: 123 });
    });

    it('should return new IProductoFavoritos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoFavoritos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductoFavoritos).toEqual(new ProductoFavoritos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductoFavoritos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoFavoritos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoFavoritos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
