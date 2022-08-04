import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductoPromocionHomeComponent } from '../list/producto-promocion-home.component';
import { ProductoPromocionHomeDetailComponent } from '../detail/producto-promocion-home-detail.component';
import { ProductoPromocionHomeUpdateComponent } from '../update/producto-promocion-home-update.component';
import { ProductoPromocionHomeRoutingResolveService } from './producto-promocion-home-routing-resolve.service';

const productoPromocionHomeRoute: Routes = [
  {
    path: '',
    component: ProductoPromocionHomeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductoPromocionHomeDetailComponent,
    resolve: {
      productoPromocionHome: ProductoPromocionHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductoPromocionHomeUpdateComponent,
    resolve: {
      productoPromocionHome: ProductoPromocionHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductoPromocionHomeUpdateComponent,
    resolve: {
      productoPromocionHome: ProductoPromocionHomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productoPromocionHomeRoute)],
  exports: [RouterModule],
})
export class ProductoPromocionHomeRoutingModule {}
