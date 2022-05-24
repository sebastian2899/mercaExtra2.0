import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';
import { EgresoDeleteDialogComponent } from '../delete/egreso-delete-dialog.component';

@Component({
  selector: 'jhi-egreso',
  templateUrl: './egreso.component.html',
})
export class EgresoComponent implements OnInit {
  @ViewChild('seeOptions',{static:true}) content : ElementRef | undefined;

  egresos?: IEgreso[];
  isLoading = false;
  valueEgress?: number | null;
  titleKindEgress?: string | null;

  constructor(protected egresoService: EgresoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.egresoService.query().subscribe({
      next: (res: HttpResponse<IEgreso[]>) => {
        this.isLoading = false;
        this.egresos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
    this.titleKindEgress = 'Egresos generales';
  }

  ngOnInit(): void {
    // this.loadAll();
    this.dayliEgress();
  }

  getKindEgress(option:number):void{
    option === 1 ? this.loadAll() : this.dailyEgress();
    this.modalService.dismissAll();
  }

  dailyEgress():void{
    this.egresoService.getAllDailyEgress().subscribe({
      next:
      (res: HttpResponse<IEgreso[]>) => {
        this.egresos = res.body ?? []       
      },
      error: 
      () => {
        this.egresos =[]
      }
    })
    this.titleKindEgress = 'Egresos de hoy';
  }

  seeOption():void{
    this.modalService.open(this.content,{size:'lg'});
  }

  dayliEgress():void{
  this.egresoService.dayliEgress().subscribe({
    next:
    (res:HttpResponse<number>) => {
      this.valueEgress = res.body
    },
    error:
    () => {
      this.valueEgress = 0
    }
  });
  }

  trackId(index: number, item: IEgreso): number {
    return item.id!;
  }

  delete(egreso: IEgreso): void {
    const modalRef = this.modalService.open(EgresoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.egreso = egreso;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
