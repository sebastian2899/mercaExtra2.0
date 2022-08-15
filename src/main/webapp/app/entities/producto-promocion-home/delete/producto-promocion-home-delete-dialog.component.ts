import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

@Component({
  templateUrl: './producto-promocion-home-delete-dialog.component.html',
})
export class ProductoPromocionHomeDeleteDialogComponent {
  productoPromocionHome?: IProductoPromocionHome;

  constructor(protected productoPromocionHomeService: ProductoPromocionHomeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productoPromocionHomeService.deleteProductoDesc(id).subscribe(() => this.activeModal.close('deleted'));
  }
}
