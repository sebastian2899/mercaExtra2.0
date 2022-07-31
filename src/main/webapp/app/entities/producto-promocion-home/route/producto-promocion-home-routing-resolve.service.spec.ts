import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

import { ProductoPromocionHomeRoutingResolveService } from './producto-promocion-home-routing-resolve.service';

describe('ProductoPromocionHome routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductoPromocionHomeRoutingResolveService;
  let service: ProductoPromocionHomeService;
  let resultProductoPromocionHome: IProductoPromocionHome | undefined;

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
    routingResolveService = TestBed.inject(ProductoPromocionHomeRoutingResolveService);
    service = TestBed.inject(ProductoPromocionHomeService);
    resultProductoPromocionHome = undefined;
  });

  describe('resolve', () => {
    it('should return IProductoPromocionHome returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoPromocionHome = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoPromocionHome).toEqual({ id: 123 });
    });

    it('should return new IProductoPromocionHome if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoPromocionHome = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductoPromocionHome).toEqual(new ProductoPromocionHome());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductoPromocionHome })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoPromocionHome = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoPromocionHome).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
