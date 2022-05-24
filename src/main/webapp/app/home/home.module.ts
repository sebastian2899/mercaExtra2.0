import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]),MatIconModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
