import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

@Injectable({ providedIn: 'root' })
export class ProductoPromocionHomeRoutingResolveService implements Resolve<IProductoPromocionHome> {
  constructor(protected service: ProductoPromocionHomeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductoPromocionHome> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productoPromocionHome: HttpResponse<ProductoPromocionHome>) => {
          if (productoPromocionHome.body) {
            return of(productoPromocionHome.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductoPromocionHome());
  }
}
