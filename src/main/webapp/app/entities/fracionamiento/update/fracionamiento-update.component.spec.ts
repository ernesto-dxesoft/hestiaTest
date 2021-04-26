jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FracionamientoService } from '../service/fracionamiento.service';
import { IFracionamiento, Fracionamiento } from '../fracionamiento.model';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

import { FracionamientoUpdateComponent } from './fracionamiento-update.component';

describe('Component Tests', () => {
  describe('Fracionamiento Management Update Component', () => {
    let comp: FracionamientoUpdateComponent;
    let fixture: ComponentFixture<FracionamientoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fracionamientoService: FracionamientoService;
    let addressService: AddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FracionamientoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FracionamientoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FracionamientoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fracionamientoService = TestBed.inject(FracionamientoService);
      addressService = TestBed.inject(AddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call address query and add missing value', () => {
        const fracionamiento: IFracionamiento = { id: 456 };
        const address: IAddress = { id: 91108 };
        fracionamiento.address = address;

        const addressCollection: IAddress[] = [{ id: 41034 }];
        spyOn(addressService, 'query').and.returnValue(of(new HttpResponse({ body: addressCollection })));
        const expectedCollection: IAddress[] = [address, ...addressCollection];
        spyOn(addressService, 'addAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ fracionamiento });
        comp.ngOnInit();

        expect(addressService.query).toHaveBeenCalled();
        expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
        expect(comp.addressesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fracionamiento: IFracionamiento = { id: 456 };
        const address: IAddress = { id: 69571 };
        fracionamiento.address = address;

        activatedRoute.data = of({ fracionamiento });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fracionamiento));
        expect(comp.addressesCollection).toContain(address);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fracionamiento = { id: 123 };
        spyOn(fracionamientoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fracionamiento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fracionamiento }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fracionamientoService.update).toHaveBeenCalledWith(fracionamiento);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fracionamiento = new Fracionamiento();
        spyOn(fracionamientoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fracionamiento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fracionamiento }));
        saveSubject.complete();

        // THEN
        expect(fracionamientoService.create).toHaveBeenCalledWith(fracionamiento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fracionamiento = { id: 123 };
        spyOn(fracionamientoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fracionamiento });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fracionamientoService.update).toHaveBeenCalledWith(fracionamiento);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAddressById', () => {
        it('Should return tracked Address primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
