import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFracionamiento, Fracionamiento } from '../fracionamiento.model';
import { FracionamientoService } from '../service/fracionamiento.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

@Component({
  selector: 'jhi-fracionamiento-update',
  templateUrl: './fracionamiento-update.component.html',
})
export class FracionamientoUpdateComponent implements OnInit {
  isSaving = false;

  addressesCollection: IAddress[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(200)]],
    startDate: [null, [Validators.required]],
    totalHouses: [null, [Validators.required, Validators.min(49)]],
    costByHouse: [null, [Validators.required]],
    status: [null, [Validators.required]],
    contract: [null, [Validators.required]],
    address: [],
  });

  constructor(
    protected fracionamientoService: FracionamientoService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fracionamiento }) => {
      this.updateForm(fracionamiento);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fracionamiento = this.createFromForm();
    if (fracionamiento.id !== undefined) {
      this.subscribeToSaveResponse(this.fracionamientoService.update(fracionamiento));
    } else {
      this.subscribeToSaveResponse(this.fracionamientoService.create(fracionamiento));
    }
  }

  trackAddressById(index: number, item: IAddress): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFracionamiento>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fracionamiento: IFracionamiento): void {
    this.editForm.patchValue({
      id: fracionamiento.id,
      name: fracionamiento.name,
      startDate: fracionamiento.startDate,
      totalHouses: fracionamiento.totalHouses,
      costByHouse: fracionamiento.costByHouse,
      status: fracionamiento.status,
      contract: fracionamiento.contract,
      address: fracionamiento.address,
    });

    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing(this.addressesCollection, fracionamiento.address);
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query({ filter: 'fracionamiento-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing(addresses, this.editForm.get('address')!.value))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));
  }

  protected createFromForm(): IFracionamiento {
    return {
      ...new Fracionamiento(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      totalHouses: this.editForm.get(['totalHouses'])!.value,
      costByHouse: this.editForm.get(['costByHouse'])!.value,
      status: this.editForm.get(['status'])!.value,
      contract: this.editForm.get(['contract'])!.value,
      address: this.editForm.get(['address'])!.value,
    };
  }
}
