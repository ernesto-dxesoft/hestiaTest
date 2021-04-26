jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFracionamiento, Fracionamiento } from '../fracionamiento.model';
import { FracionamientoService } from '../service/fracionamiento.service';

import { FracionamientoRoutingResolveService } from './fracionamiento-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fracionamiento routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FracionamientoRoutingResolveService;
    let service: FracionamientoService;
    let resultFracionamiento: IFracionamiento | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FracionamientoRoutingResolveService);
      service = TestBed.inject(FracionamientoService);
      resultFracionamiento = undefined;
    });

    describe('resolve', () => {
      it('should return IFracionamiento returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFracionamiento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFracionamiento).toEqual({ id: 123 });
      });

      it('should return new IFracionamiento if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFracionamiento = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFracionamiento).toEqual(new Fracionamiento());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFracionamiento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFracionamiento).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
