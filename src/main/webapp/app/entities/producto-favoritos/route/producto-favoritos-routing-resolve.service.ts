import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductoFavoritos, ProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';

@Injectable({ providedIn: 'root' })
export class ProductoFavoritosRoutingResolveService implements Resolve<IProductoFavoritos> {
  constructor(protected service: ProductoFavoritosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductoFavoritos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productoFavoritos: HttpResponse<ProductoFavoritos>) => {
          if (productoFavoritos.body) {
            return of(productoFavoritos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductoFavoritos());
  }
}
