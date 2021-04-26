import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFracionamiento } from '../fracionamiento.model';
import { FracionamientoService } from '../service/fracionamiento.service';

@Component({
  templateUrl: './fracionamiento-delete-dialog.component.html',
})
export class FracionamientoDeleteDialogComponent {
  fracionamiento?: IFracionamiento;

  constructor(protected fracionamientoService: FracionamientoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fracionamientoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
