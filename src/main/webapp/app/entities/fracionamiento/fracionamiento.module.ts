import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FracionamientoComponent } from './list/fracionamiento.component';
import { FracionamientoDetailComponent } from './detail/fracionamiento-detail.component';
import { FracionamientoUpdateComponent } from './update/fracionamiento-update.component';
import { FracionamientoDeleteDialogComponent } from './delete/fracionamiento-delete-dialog.component';
import { FracionamientoRoutingModule } from './route/fracionamiento-routing.module';

@NgModule({
  imports: [SharedModule, FracionamientoRoutingModule],
  declarations: [
    FracionamientoComponent,
    FracionamientoDetailComponent,
    FracionamientoUpdateComponent,
    FracionamientoDeleteDialogComponent,
  ],
  entryComponents: [FracionamientoDeleteDialogComponent],
})
export class FracionamientoModule {}
