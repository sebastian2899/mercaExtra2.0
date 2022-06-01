import { Component, ElementRef, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReembolso } from '../reembolso.model';
import { ReembolsoService } from '../service/reembolso.service';
import { ReembolsoDeleteDialogComponent } from '../delete/reembolso-delete-dialog.component';
import { AlertService } from 'app/core/util/alert.service';
import { IDatosReembolso } from '../datosRemProces';

@Component({
  selector: 'jhi-reembolso',
  templateUrl: './reembolso.component.html',
})
export class ReembolsoComponent {
  @ViewChild('seeRefundData', { static: true }) content: ElementRef | undefined;

  reembolsos?: IReembolso[];
  isLoading = false;
  title?: string | null;
  assigmentHeigth?: boolean | null;
  completeState?: boolean | null;
  isComplete?: boolean | null;
  dataRefundInProcess?: IDatosReembolso | null;
  metodosReembolso = ['Transferencia', 'Cheque', 'Efectivo'];
  metodoReembolso?: string | null;

  constructor(protected reembolsoService: ReembolsoService, protected modalService: NgbModal, protected alertService: AlertService) {}

  refundByOption(option: number): void {
    switch (option) {
      case 1:
        this.title = 'Todos los reembolsos';
        this.assigmentHeigth = false;
        this.completeState = false;
        this.isComplete = false;
        break;

      case 2:
        this.title = 'Reembolsos en caso de estudio';
        this.assigmentHeigth = true;
        this.completeState = true;
        this.isComplete = false;
        break;

      case 3:
        this.title = 'Reembolsos concluidos';
        this.assigmentHeigth = true;
        this.completeState = false;
        this.isComplete = true;
        break;

      case 4:
        this.title = 'Reembolsados';
        this.assigmentHeigth = true;
        this.completeState = false;
        this.isComplete = false;
        break;
    }

    this.reembolsoService.refundByOption(option).subscribe({
      next: (res: HttpResponse<IReembolso[]>) => {
        this.reembolsos = res.body ?? [];
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'No se pudo obtener los reembolsos',
        });
      },
    });
  }

  dataRefundProcess(id: number): void {
    this.reembolsoService.dataRefundProcess(id).subscribe({
      next: (res: HttpResponse<IDatosReembolso>) => {
        this.dataRefundInProcess = res.body;
        this.modalService.open(this.content, { size: 'lg', backdrop: 'static' });
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'No se pudo obtener los datos del reembolso',
        });
      },
    });
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  completeRefund(id: number): void {
    if (id) {
      this.reembolsoService.find(id).subscribe({
        next: (res: HttpResponse<IReembolso>) => {
          const reembolso = res.body;
          reembolso!.metodoReembolso = this.metodoReembolso;
          this.reembolsoService.update(res.body!).subscribe({
            next: () => {
              this.alertService.addAlert({
                type: 'success',
                message: 'Reembolso actualizado',
              });
              this.refundByOption(1);
              this.modalService.dismissAll();
            },
            error: () => {
              this.alertService.addAlert({
                type: 'danger',
                message: 'No se pudo actualizar el reembolso',
              });
            },
          });
        },
        error: () => {
          this.alertService.addAlert({
            type: 'danger',
            message: 'No se pudo obtener el reembolso',
          });
        },
      });
    }
  }

  // loadAll(): void {
  //   this.isLoading = true;

  //   this.reembolsoService.query().subscribe({
  //     next: (res: HttpResponse<IReembolso[]>) => {
  //       this.isLoading = false;
  //       this.reembolsos = res.body ?? [];
  //     },
  //     error: () => {
  //       this.isLoading = false;
  //     },
  //   });
  //   this.completed = false;
  //   this.title = 'Todos los reembolsos';
  // }

  // loadRefundStudy(): void {
  //   this.reembolsoService.refundStudy().subscribe({
  //     next: (res: HttpResponse<IReembolso[]>) => {
  //       this.reembolsos = res.body ?? [];
  //     },
  //     error: () => {
  //       this.reembolsos = [];
  //     },
  //   });

  //   this.completed = true;
  //   this.title = 'Reembolsos en caso de estudio';
  // }

  concluirReembolso(refund: IReembolso): void {
    this.reembolsoService.update(refund).subscribe({
      next: () => {
        this.alertService.addAlert({
          type: 'success',
          message: 'Reembolso concluido con exito.',
        });

        this.refundByOption(2);
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'No se pudo concluir el reembolso',
        });
      },
    });
  }

  // ngOnInit(): void {

  // }

  trackId(index: number, item: IReembolso): number {
    return item.id!;
  }

  delete(reembolso: IReembolso): void {
    const modalRef = this.modalService.open(ReembolsoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reembolso = reembolso;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.refundByOption(1);
      }
    });
  }
}
