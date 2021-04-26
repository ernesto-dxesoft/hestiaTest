import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFracionamiento } from '../fracionamiento.model';
import { FracionamientoService } from '../service/fracionamiento.service';
import { FracionamientoDeleteDialogComponent } from '../delete/fracionamiento-delete-dialog.component';

@Component({
  selector: 'jhi-fracionamiento',
  templateUrl: './fracionamiento.component.html',
})
export class FracionamientoComponent implements OnInit {
  fracionamientos?: IFracionamiento[];
  isLoading = false;

  constructor(protected fracionamientoService: FracionamientoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fracionamientoService.query().subscribe(
      (res: HttpResponse<IFracionamiento[]>) => {
        this.isLoading = false;
        this.fracionamientos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFracionamiento): number {
    return item.id!;
  }

  delete(fracionamiento: IFracionamiento): void {
    const modalRef = this.modalService.open(FracionamientoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fracionamiento = fracionamiento;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
