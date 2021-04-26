import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { StatusFraccionamiento } from 'app/entities/enumerations/status-fraccionamiento.model';
import { IFracionamiento, Fracionamiento } from '../fracionamiento.model';

import { FracionamientoService } from './fracionamiento.service';

describe('Service Tests', () => {
  describe('Fracionamiento Service', () => {
    let service: FracionamientoService;
    let httpMock: HttpTestingController;
    let elemDefault: IFracionamiento;
    let expectedResult: IFracionamiento | IFracionamiento[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FracionamientoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        startDate: currentDate,
        totalHouses: 0,
        costByHouse: 0,
        status: StatusFraccionamiento.ACTIVO,
        contract: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fracionamiento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Fracionamiento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fracionamiento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            totalHouses: 1,
            costByHouse: 1,
            status: 'BBBBBB',
            contract: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fracionamiento', () => {
        const patchObject = Object.assign(
          {
            status: 'BBBBBB',
          },
          new Fracionamiento()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fracionamiento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            totalHouses: 1,
            costByHouse: 1,
            status: 'BBBBBB',
            contract: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fracionamiento', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFracionamientoToCollectionIfMissing', () => {
        it('should add a Fracionamiento to an empty array', () => {
          const fracionamiento: IFracionamiento = { id: 123 };
          expectedResult = service.addFracionamientoToCollectionIfMissing([], fracionamiento);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fracionamiento);
        });

        it('should not add a Fracionamiento to an array that contains it', () => {
          const fracionamiento: IFracionamiento = { id: 123 };
          const fracionamientoCollection: IFracionamiento[] = [
            {
              ...fracionamiento,
            },
            { id: 456 },
          ];
          expectedResult = service.addFracionamientoToCollectionIfMissing(fracionamientoCollection, fracionamiento);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fracionamiento to an array that doesn't contain it", () => {
          const fracionamiento: IFracionamiento = { id: 123 };
          const fracionamientoCollection: IFracionamiento[] = [{ id: 456 }];
          expectedResult = service.addFracionamientoToCollectionIfMissing(fracionamientoCollection, fracionamiento);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fracionamiento);
        });

        it('should add only unique Fracionamiento to an array', () => {
          const fracionamientoArray: IFracionamiento[] = [{ id: 123 }, { id: 456 }, { id: 78685 }];
          const fracionamientoCollection: IFracionamiento[] = [{ id: 123 }];
          expectedResult = service.addFracionamientoToCollectionIfMissing(fracionamientoCollection, ...fracionamientoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fracionamiento: IFracionamiento = { id: 123 };
          const fracionamiento2: IFracionamiento = { id: 456 };
          expectedResult = service.addFracionamientoToCollectionIfMissing([], fracionamiento, fracionamiento2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fracionamiento);
          expect(expectedResult).toContain(fracionamiento2);
        });

        it('should accept null and undefined values', () => {
          const fracionamiento: IFracionamiento = { id: 123 };
          expectedResult = service.addFracionamientoToCollectionIfMissing([], null, fracionamiento, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fracionamiento);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
