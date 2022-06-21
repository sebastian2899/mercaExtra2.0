import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductoFavoritosComponent } from './list/producto-favoritos.component';
import { ProductoFavoritosDetailComponent } from './detail/producto-favoritos-detail.component';
import { ProductoFavoritosUpdateComponent } from './update/producto-favoritos-update.component';
import { ProductoFavoritosDeleteDialogComponent } from './delete/producto-favoritos-delete-dialog.component';
import { ProductoFavoritosRoutingModule } from './route/producto-favoritos-routing.module';

@NgModule({
  imports: [SharedModule, ProductoFavoritosRoutingModule],
  declarations: [
    ProductoFavoritosComponent,
    ProductoFavoritosDetailComponent,
    ProductoFavoritosUpdateComponent,
    ProductoFavoritosDeleteDialogComponent,
  ],
  entryComponents: [ProductoFavoritosDeleteDialogComponent],
})
export class ProductoFavoritosModule {}
