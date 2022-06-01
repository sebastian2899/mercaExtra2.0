import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReembolsoComponent } from './list/reembolso.component';
import { ReembolsoDetailComponent } from './detail/reembolso-detail.component';
import { ReembolsoUpdateComponent } from './update/reembolso-update.component';
import { ReembolsoDeleteDialogComponent } from './delete/reembolso-delete-dialog.component';
import { ReembolsoRoutingModule } from './route/reembolso-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, ReembolsoRoutingModule, MatIconModule],
  declarations: [ReembolsoComponent, ReembolsoDetailComponent, ReembolsoUpdateComponent, ReembolsoDeleteDialogComponent],
  entryComponents: [ReembolsoDeleteDialogComponent],
})
export class ReembolsoModule {}
