import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'fracionamiento',
        data: { pageTitle: 'hestiaTestApp.fracionamiento.home.title' },
        loadChildren: () => import('./fracionamiento/fracionamiento.module').then(m => m.FracionamientoModule),
      },
      {
        path: 'address',
        data: { pageTitle: 'hestiaTestApp.address.home.title' },
        loadChildren: () => import('./address/address.module').then(m => m.AddressModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
