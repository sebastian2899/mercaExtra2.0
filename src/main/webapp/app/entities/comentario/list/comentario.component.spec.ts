import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ComentarioService } from '../service/comentario.service';

import { ComentarioComponent } from './comentario.component';

describe('Comentario Management Component', () => {
  let comp: ComentarioComponent;
  let fixture: ComponentFixture<ComentarioComponent>;
  let service: ComentarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ComentarioComponent],
    })
      .overrideTemplate(ComentarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComentarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ComentarioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
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
    expect(comp.comentarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
