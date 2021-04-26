import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FracionamientoService } from '../service/fracionamiento.service';

import { FracionamientoComponent } from './fracionamiento.component';

describe('Component Tests', () => {
  describe('Fracionamiento Management Component', () => {
    let comp: FracionamientoComponent;
    let fixture: ComponentFixture<FracionamientoComponent>;
    let service: FracionamientoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FracionamientoComponent],
      })
        .overrideTemplate(FracionamientoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FracionamientoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FracionamientoService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fracionamientos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
