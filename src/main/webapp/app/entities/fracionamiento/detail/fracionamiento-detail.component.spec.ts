import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FracionamientoDetailComponent } from './fracionamiento-detail.component';

describe('Component Tests', () => {
  describe('Fracionamiento Management Detail Component', () => {
    let comp: FracionamientoDetailComponent;
    let fixture: ComponentFixture<FracionamientoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FracionamientoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fracionamiento: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FracionamientoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FracionamientoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fracionamiento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fracionamiento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
