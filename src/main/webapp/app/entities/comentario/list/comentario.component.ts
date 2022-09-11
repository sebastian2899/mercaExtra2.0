import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IComentario } from '../comentario.model';
import { ComentarioService } from '../service/comentario.service';
import { ComentarioDeleteDialogComponent } from '../delete/comentario-delete-dialog.component';

@Component({
  selector: 'jhi-comentario',
  templateUrl: './comentario.component.html',
  styleUrls: ['./comentario.component.css'],
})
export class ComentarioComponent implements OnInit {
  comentarios?: IComentario[];
  isLoading = false;

  constructor(protected comentarioService: ComentarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.comentarioService.query().subscribe({
      next: (res: HttpResponse<IComentario[]>) => {
        this.isLoading = false;
        this.comentarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IComentario): number {
    return item.id!;
  }

  delete(comentario: IComentario): void {
    const modalRef = this.modalService.open(ComentarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.comentario = comentario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
