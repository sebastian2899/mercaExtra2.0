import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductoFavoritos, getProductoFavoritosIdentifier } from '../producto-favoritos.model';

export type EntityResponseType = HttpResponse<IProductoFavoritos>;
export type EntityArrayResponseType = HttpResponse<IProductoFavoritos[]>;

@Injectable({ providedIn: 'root' })
export class ProductoFavoritosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/producto-favoritos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productoFavoritos: IProductoFavoritos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoFavoritos);
    return this.http
      .post<IProductoFavoritos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productoFavoritos: IProductoFavoritos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoFavoritos);
    return this.http
      .put<IProductoFavoritos>(`${this.resourceUrl}/${getProductoFavoritosIdentifier(productoFavoritos) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productoFavoritos: IProductoFavoritos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productoFavoritos);
    return this.http
      .patch<IProductoFavoritos>(`${this.resourceUrl}/${getProductoFavoritosIdentifier(productoFavoritos) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductoFavoritos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductoFavoritos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoFavoritosToCollectionIfMissing(
    productoFavoritosCollection: IProductoFavoritos[],
    ...productoFavoritosToCheck: (IProductoFavoritos | null | undefined)[]
  ): IProductoFavoritos[] {
    const productoFavoritos: IProductoFavoritos[] = productoFavoritosToCheck.filter(isPresent);
    if (productoFavoritos.length > 0) {
      const productoFavoritosCollectionIdentifiers = productoFavoritosCollection.map(
        productoFavoritosItem => getProductoFavoritosIdentifier(productoFavoritosItem)!
      );
      const productoFavoritosToAdd = productoFavoritos.filter(productoFavoritosItem => {
        const productoFavoritosIdentifier = getProductoFavoritosIdentifier(productoFavoritosItem);
        if (productoFavoritosIdentifier == null || productoFavoritosCollectionIdentifiers.includes(productoFavoritosIdentifier)) {
          return false;
        }
        productoFavoritosCollectionIdentifiers.push(productoFavoritosIdentifier);
        return true;
      });
      return [...productoFavoritosToAdd, ...productoFavoritosCollection];
    }
    return productoFavoritosCollection;
  }

  protected convertDateFromClient(productoFavoritos: IProductoFavoritos): IProductoFavoritos {
    return Object.assign({}, productoFavoritos, {
      lastUpdate: productoFavoritos.lastUpdate?.isValid() ? productoFavoritos.lastUpdate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastUpdate = res.body.lastUpdate ? dayjs(res.body.lastUpdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productoFavoritos: IProductoFavoritos) => {
        productoFavoritos.lastUpdate = productoFavoritos.lastUpdate ? dayjs(productoFavoritos.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
