import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';

@Component({
  templateUrl: './producto-favoritos-delete-dialog.component.html',
})
export class ProductoFavoritosDeleteDialogComponent {
  productoFavoritos?: IProductoFavoritos;

  constructor(protected productoFavoritosService: ProductoFavoritosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productoFavoritosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
