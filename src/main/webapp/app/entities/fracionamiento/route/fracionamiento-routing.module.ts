import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FracionamientoComponent } from '../list/fracionamiento.component';
import { FracionamientoDetailComponent } from '../detail/fracionamiento-detail.component';
import { FracionamientoUpdateComponent } from '../update/fracionamiento-update.component';
import { FracionamientoRoutingResolveService } from './fracionamiento-routing-resolve.service';

const fracionamientoRoute: Routes = [
  {
    path: '',
    component: FracionamientoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FracionamientoDetailComponent,
    resolve: {
      fracionamiento: FracionamientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FracionamientoUpdateComponent,
    resolve: {
      fracionamiento: FracionamientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FracionamientoUpdateComponent,
    resolve: {
      fracionamiento: FracionamientoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fracionamientoRoute)],
  exports: [RouterModule],
})
export class FracionamientoRoutingModule {}
