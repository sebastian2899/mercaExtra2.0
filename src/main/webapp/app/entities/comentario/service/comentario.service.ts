import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComentario, getComentarioIdentifier } from '../comentario.model';

export type EntityResponseType = HttpResponse<IComentario>;
export type EntityArrayResponseType = HttpResponse<IComentario[]>;

@Injectable({ providedIn: 'root' })
export class ComentarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comentarios');
  protected uploadCommentProductsUrl = this.applicationConfigService.getEndpointFor('api/comentarioProductos');
  protected uploadCommentRespoProductsUrl = this.applicationConfigService.getEndpointFor('api/comentariosRespuesta');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(comentario: IComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .post<IComentario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  uploadCommentResponse(idProducto: number, idComment: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IComentario[]>(`${this.uploadCommentRespoProductsUrl}/${idProducto}/${idComment}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(comentario: IComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .put<IComentario>(`${this.resourceUrl}/${getComentarioIdentifier(comentario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  uploadCommentsProduct(idProduct: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IComentario[]>(`${this.uploadCommentProductsUrl}/${idProduct}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  partialUpdate(comentario: IComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .patch<IComentario>(`${this.resourceUrl}/${getComentarioIdentifier(comentario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IComentario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IComentario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComentarioToCollectionIfMissing(
    comentarioCollection: IComentario[],
    ...comentariosToCheck: (IComentario | null | undefined)[]
  ): IComentario[] {
    const comentarios: IComentario[] = comentariosToCheck.filter(isPresent);
    if (comentarios.length > 0) {
      const comentarioCollectionIdentifiers = comentarioCollection.map(comentarioItem => getComentarioIdentifier(comentarioItem)!);
      const comentariosToAdd = comentarios.filter(comentarioItem => {
        const comentarioIdentifier = getComentarioIdentifier(comentarioItem);
        if (comentarioIdentifier == null || comentarioCollectionIdentifiers.includes(comentarioIdentifier)) {
          return false;
        }
        comentarioCollectionIdentifiers.push(comentarioIdentifier);
        return true;
      });
      return [...comentariosToAdd, ...comentarioCollection];
    }
    return comentarioCollection;
  }

  protected convertDateFromClient(comentario: IComentario): IComentario {
    return Object.assign({}, comentario, {
      fechaComentario: comentario.fechaComentario?.isValid() ? comentario.fechaComentario.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaComentario = res.body.fechaComentario ? dayjs(res.body.fechaComentario) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((comentario: IComentario) => {
        comentario.fechaComentario = comentario.fechaComentario ? dayjs(comentario.fechaComentario) : undefined;
      });
    }
    return res;
  }
}
