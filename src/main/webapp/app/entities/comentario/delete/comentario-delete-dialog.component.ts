import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComentario } from '../comentario.model';
import { ComentarioService } from '../service/comentario.service';

@Component({
  templateUrl: './comentario-delete-dialog.component.html',
})
export class ComentarioDeleteDialogComponent {
  comentario?: IComentario;

  constructor(protected comentarioService: ComentarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comentarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
