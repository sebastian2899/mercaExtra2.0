import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { CajaDeleteDialogComponent } from '../delete/caja-delete-dialog.component';
import dayjs from 'dayjs';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-caja',
  templateUrl: './caja.component.html',
})
export class CajaComponent implements OnInit {
  @ViewChild('remember', { static: true }) content: ElementRef | undefined;
  @ViewChild('pdfDates', { static: true }) content2: ElementRef | undefined;

  cajas?: ICaja[];
  isLoading = false;
  respNumber?: boolean | null;
  intervalId?: any;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;

  constructor(protected cajaService: CajaService, protected modalService: NgbModal, protected alertService: AlertService) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaService.query().subscribe({
      next: (res: HttpResponse<ICaja[]>) => {
        this.isLoading = false;
        this.cajas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.rememberCreationCaja();
  }

  printInvoice(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.alertService.addAlert({
        type: 'warning',
        message: 'La fecha inicio no puede ser superior a la fecha Fin.',
      });
      this.cajaService.printInvoice(this.fechaInicio.toString(), this.fechaFin.toString()).subscribe((response: any) => {
        const file = new Blob([response], { type: 'application/pdf' });
        const fileURL = URL.createObjectURL(file);
        window.open(fileURL);
      });
    } else {
      this.alertService.addAlert({
        type: 'warning',
        message: 'Por favor ingrese las fechas.',
      });
    }
  }

  openDateCajaReport(): void {
    this.modalService.open(this.content2, { size: 'lg' });
  }

  rememberCreationCaja(): void {
    this.intervalId = setInterval(() => {
      this.cajaService.rememberCreationCaja().subscribe({
        next: (res: HttpResponse<boolean>) => {
          this.respNumber = res.body;
          this.respNumber === false ? clearInterval(this.intervalId) : this.modalService.open(this.content);
        },
      });
    }, 300000);

    // this.cajaService.rememberCreationCaja().subscribe({
    //   next:
    //   (res:HttpResponse<string>) =>{
    //     this.mensaje = res.body;
    //     this.mensaje === " " ? null : this.modalService.open(this.content);
    //   }
    // });
  }

  trackId(index: number, item: ICaja): number {
    return item.id!;
  }

  delete(caja: ICaja): void {
    const modalRef = this.modalService.open(CajaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caja = caja;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
