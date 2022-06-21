import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductoFavoritosComponent } from '../list/producto-favoritos.component';
import { ProductoFavoritosDetailComponent } from '../detail/producto-favoritos-detail.component';
import { ProductoFavoritosUpdateComponent } from '../update/producto-favoritos-update.component';
import { ProductoFavoritosRoutingResolveService } from './producto-favoritos-routing-resolve.service';

const productoFavoritosRoute: Routes = [
  {
    path: '',
    component: ProductoFavoritosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductoFavoritosDetailComponent,
    resolve: {
      productoFavoritos: ProductoFavoritosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductoFavoritosUpdateComponent,
    resolve: {
      productoFavoritos: ProductoFavoritosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductoFavoritosUpdateComponent,
    resolve: {
      productoFavoritos: ProductoFavoritosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productoFavoritosRoute)],
  exports: [RouterModule],
})
export class ProductoFavoritosRoutingModule {}
