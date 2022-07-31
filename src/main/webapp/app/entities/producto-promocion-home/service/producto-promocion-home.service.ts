import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductoPromocionHome, getProductoPromocionHomeIdentifier } from '../producto-promocion-home.model';
import { IProducto } from 'app/entities/producto/producto.model';

export type EntityResponseType = HttpResponse<IProductoPromocionHome>;
export type EntityArrayResponseType = HttpResponse<IProductoPromocionHome[]>;
export type EntityProductoResponseType = HttpResponse<IProducto[]>;

@Injectable({ providedIn: 'root' })
export class ProductoPromocionHomeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/producto-promocion-homes');
  protected resourceProductoDescuentoUrl = this.applicationConfigService.getEndpointFor('api/producto-home-all-disscount');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productoPromocionHome: IProductoPromocionHome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoPromocionHome);
    return this.http
      .post<IProductoPromocionHome>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  recuperarProductoDescuento(): Observable<EntityProductoResponseType> {
    return this.http.get<IProducto[]>(this.resourceProductoDescuentoUrl, { observe: 'response' });
  }

  update(productoPromocionHome: IProductoPromocionHome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoPromocionHome);
    return this.http
      .put<IProductoPromocionHome>(`${this.resourceUrl}/${getProductoPromocionHomeIdentifier(productoPromocionHome) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productoPromocionHome: IProductoPromocionHome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoPromocionHome);
    return this.http
      .patch<IProductoPromocionHome>(`${this.resourceUrl}/${getProductoPromocionHomeIdentifier(productoPromocionHome) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductoPromocionHome>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductoPromocionHome[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoPromocionHomeToCollectionIfMissing(
    productoPromocionHomeCollection: IProductoPromocionHome[],
    ...productoPromocionHomesToCheck: (IProductoPromocionHome | null | undefined)[]
  ): IProductoPromocionHome[] {
    const productoPromocionHomes: IProductoPromocionHome[] = productoPromocionHomesToCheck.filter(isPresent);
    if (productoPromocionHomes.length > 0) {
      const productoPromocionHomeCollectionIdentifiers = productoPromocionHomeCollection.map(
        productoPromocionHomeItem => getProductoPromocionHomeIdentifier(productoPromocionHomeItem)!
      );
      const productoPromocionHomesToAdd = productoPromocionHomes.filter(productoPromocionHomeItem => {
        const productoPromocionHomeIdentifier = getProductoPromocionHomeIdentifier(productoPromocionHomeItem);
        if (
          productoPromocionHomeIdentifier == null ||
          productoPromocionHomeCollectionIdentifiers.includes(productoPromocionHomeIdentifier)
        ) {
          return false;
        }
        productoPromocionHomeCollectionIdentifiers.push(productoPromocionHomeIdentifier);
        return true;
      });
      return [...productoPromocionHomesToAdd, ...productoPromocionHomeCollection];
    }
    return productoPromocionHomeCollection;
  }

  protected convertDateFromClient(productoPromocionHome: IProductoPromocionHome): IProductoPromocionHome {
    return Object.assign({}, productoPromocionHome, {
      fechaAgregado: productoPromocionHome.fechaAgregado?.isValid() ? productoPromocionHome.fechaAgregado.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaAgregado = res.body.fechaAgregado ? dayjs(res.body.fechaAgregado) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productoPromocionHome: IProductoPromocionHome) => {
        productoPromocionHome.fechaAgregado = productoPromocionHome.fechaAgregado ? dayjs(productoPromocionHome.fechaAgregado) : undefined;
      });
    }
    return res;
  }
}
