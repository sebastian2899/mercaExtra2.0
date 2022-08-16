import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

@Component({
  templateUrl: './producto-promocion-home-delete-dialog.component.html',
})
export class ProductoPromocionHomeDeleteDialogComponent {
  productoPromocionHome?: IProductoPromocionHome;

  constructor(
    protected productoPromocionHomeService: ProductoPromocionHomeService,
    protected activeModal: NgbActiveModal,
    private productoDescuentoService: ProductoPromocionHomeService
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productoDescuentoService.deleteProductoDesc(id).subscribe(() => window.location.reload());
  }
}
