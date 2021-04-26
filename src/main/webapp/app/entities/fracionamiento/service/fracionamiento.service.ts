import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFracionamiento, getFracionamientoIdentifier } from '../fracionamiento.model';

export type EntityResponseType = HttpResponse<IFracionamiento>;
export type EntityArrayResponseType = HttpResponse<IFracionamiento[]>;

@Injectable({ providedIn: 'root' })
export class FracionamientoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fracionamientos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fracionamiento: IFracionamiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fracionamiento);
    return this.http
      .post<IFracionamiento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fracionamiento: IFracionamiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fracionamiento);
    return this.http
      .put<IFracionamiento>(`${this.resourceUrl}/${getFracionamientoIdentifier(fracionamiento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fracionamiento: IFracionamiento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fracionamiento);
    return this.http
      .patch<IFracionamiento>(`${this.resourceUrl}/${getFracionamientoIdentifier(fracionamiento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFracionamiento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFracionamiento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFracionamientoToCollectionIfMissing(
    fracionamientoCollection: IFracionamiento[],
    ...fracionamientosToCheck: (IFracionamiento | null | undefined)[]
  ): IFracionamiento[] {
    const fracionamientos: IFracionamiento[] = fracionamientosToCheck.filter(isPresent);
    if (fracionamientos.length > 0) {
      const fracionamientoCollectionIdentifiers = fracionamientoCollection.map(
        fracionamientoItem => getFracionamientoIdentifier(fracionamientoItem)!
      );
      const fracionamientosToAdd = fracionamientos.filter(fracionamientoItem => {
        const fracionamientoIdentifier = getFracionamientoIdentifier(fracionamientoItem);
        if (fracionamientoIdentifier == null || fracionamientoCollectionIdentifiers.includes(fracionamientoIdentifier)) {
          return false;
        }
        fracionamientoCollectionIdentifiers.push(fracionamientoIdentifier);
        return true;
      });
      return [...fracionamientosToAdd, ...fracionamientoCollection];
    }
    return fracionamientoCollection;
  }

  protected convertDateFromClient(fracionamiento: IFracionamiento): IFracionamiento {
    return Object.assign({}, fracionamiento, {
      startDate: fracionamiento.startDate?.isValid() ? fracionamiento.startDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fracionamiento: IFracionamiento) => {
        fracionamiento.startDate = fracionamiento.startDate ? dayjs(fracionamiento.startDate) : undefined;
      });
    }
    return res;
  }
}
