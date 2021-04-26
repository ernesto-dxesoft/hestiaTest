import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFracionamiento } from '../fracionamiento.model';

@Component({
  selector: 'jhi-fracionamiento-detail',
  templateUrl: './fracionamiento-detail.component.html',
})
export class FracionamientoDetailComponent implements OnInit {
  fracionamiento: IFracionamiento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fracionamiento }) => {
      this.fracionamiento = fracionamiento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
