import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReembolso, getReembolsoIdentifier } from '../reembolso.model';
import { IPedido } from 'app/entities/pedido/pedido.model';
import { IDatosReembolso } from '../datosRemProces';

export type EntityResponseType = HttpResponse<IReembolso>;
export type EntityArrayResponseType = HttpResponse<IReembolso[]>;
export type PedidoArrayResponseType = HttpResponse<IPedido[]>;
export type DatoReembolsoResponseType = HttpResponse<IDatosReembolso>;

@Injectable({ providedIn: 'root' })
export class ReembolsoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reembolsos');
  protected resourceReembolsoPedidosUrl = this.applicationConfigService.getEndpointFor('api/reembolsos-pedidos');
  protected refundByOptionUrl = this.applicationConfigService.getEndpointFor('api/reembolsos-option');
  protected refundProcessURL = this.applicationConfigService.getEndpointFor('api/reembolsos/refundInProcess');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reembolso: IReembolso): Observable<EntityResponseType> {
    return this.http.post<IReembolso>(this.resourceUrl, reembolso, { observe: 'response' });
  }

  dataRefundProcess(id: number): Observable<DatoReembolsoResponseType> {
    return this.http.get<IDatosReembolso>(`${this.refundProcessURL}/${id}`, { observe: 'response' });
  }

  // refundStudy(): Observable<EntityArrayResponseType> {
  //   return this.http
  //     .get<IReembolso[]>(this.refundStudyUrl, { observe: 'response' })
  //     .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  // }

  refundByOption(option: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IReembolso[]>(`${this.refundByOptionUrl}/${option}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(reembolso: IReembolso): Observable<EntityResponseType> {
    return this.http.put<IReembolso>(`${this.resourceUrl}/${getReembolsoIdentifier(reembolso) as number}`, reembolso, {
      observe: 'response',
    });
  }

  partialUpdate(reembolso: IReembolso): Observable<EntityResponseType> {
    return this.http.patch<IReembolso>(`${this.resourceUrl}/${getReembolsoIdentifier(reembolso) as number}`, reembolso, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReembolso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReembolso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReembolsoToCollectionIfMissing(
    reembolsoCollection: IReembolso[],
    ...reembolsosToCheck: (IReembolso | null | undefined)[]
  ): IReembolso[] {
    const reembolsos: IReembolso[] = reembolsosToCheck.filter(isPresent);
    if (reembolsos.length > 0) {
      const reembolsoCollectionIdentifiers = reembolsoCollection.map(reembolsoItem => getReembolsoIdentifier(reembolsoItem)!);
      const reembolsosToAdd = reembolsos.filter(reembolsoItem => {
        const reembolsoIdentifier = getReembolsoIdentifier(reembolsoItem);
        if (reembolsoIdentifier == null || reembolsoCollectionIdentifiers.includes(reembolsoIdentifier)) {
          return false;
        }
        reembolsoCollectionIdentifiers.push(reembolsoIdentifier);
        return true;
      });
      return [...reembolsosToAdd, ...reembolsoCollection];
    }
    return reembolsoCollection;
  }

  protected convertDateFromClient(reembolso: IReembolso): IReembolso {
    return Object.assign({}, reembolso, {
      fechaReembolso: reembolso.fechaReembolso?.isValid() ? reembolso.fechaReembolso.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaReembolso = res.body.fechaReembolso ? dayjs(res.body.fechaReembolso) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reembolso: IReembolso) => {
        reembolso.fechaReembolso = reembolso.fechaReembolso ? dayjs(reembolso.fechaReembolso) : undefined;
      });
    }
    return res;
  }
}
