import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFracionamiento, Fracionamiento } from '../fracionamiento.model';
import { FracionamientoService } from '../service/fracionamiento.service';

@Injectable({ providedIn: 'root' })
export class FracionamientoRoutingResolveService implements Resolve<IFracionamiento> {
  constructor(protected service: FracionamientoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFracionamiento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fracionamiento: HttpResponse<Fracionamiento>) => {
          if (fracionamiento.body) {
            return of(fracionamiento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fracionamiento());
  }
}
