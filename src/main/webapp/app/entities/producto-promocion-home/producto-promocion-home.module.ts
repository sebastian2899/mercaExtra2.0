import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductoPromocionHomeComponent } from './list/producto-promocion-home.component';
import { ProductoPromocionHomeDetailComponent } from './detail/producto-promocion-home-detail.component';
import { ProductoPromocionHomeUpdateComponent } from './update/producto-promocion-home-update.component';
import { ProductoPromocionHomeDeleteDialogComponent } from './delete/producto-promocion-home-delete-dialog.component';
import { ProductoPromocionHomeRoutingModule } from './route/producto-promocion-home-routing.module';

@NgModule({
  imports: [SharedModule, ProductoPromocionHomeRoutingModule],
  declarations: [
    ProductoPromocionHomeComponent,
    ProductoPromocionHomeDetailComponent,
    ProductoPromocionHomeUpdateComponent,
    ProductoPromocionHomeDeleteDialogComponent,
  ],
  entryComponents: [ProductoPromocionHomeDeleteDialogComponent],
})
export class ProductoPromocionHomeModule {}
