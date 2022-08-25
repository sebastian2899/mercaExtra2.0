import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CajaComponent } from './list/caja.component';
import { CajaDetailComponent } from './detail/caja-detail.component';
import { CajaUpdateComponent } from './update/caja-update.component';
import { CajaDeleteDialogComponent } from './delete/caja-delete-dialog.component';
import { CajaRoutingModule } from './route/caja-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, CajaRoutingModule, MatIconModule],
  declarations: [CajaComponent, CajaDetailComponent, CajaUpdateComponent, CajaDeleteDialogComponent],
  entryComponents: [CajaDeleteDialogComponent],
})
export class CajaModule {}
