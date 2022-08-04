import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pedido',
        data: { pageTitle: 'mercaExtraApp.pedido.home.title' },
        loadChildren: () => import('./pedido/pedido.module').then(m => m.PedidoModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'mercaExtraApp.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'notificacion',
        data: { pageTitle: 'mercaExtraApp.notificacion.home.title' },
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'mercaExtraApp.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      {
        path: 'domiciliario',
        data: { pageTitle: 'mercaExtraApp.domiciliario.home.title' },
        loadChildren: () => import('./domiciliario/domiciliario.module').then(m => m.DomiciliarioModule),
      },
      {
        path: 'caja',
        data: { pageTitle: 'mercaExtraApp.caja.home.title' },
        loadChildren: () => import('./caja/caja.module').then(m => m.CajaModule),
      },
      {
        path: 'categoria-producto',
        data: { pageTitle: 'mercaExtraApp.categoriaProducto.home.title' },
        loadChildren: () => import('./categoria-producto/categoria-producto.module').then(m => m.CategoriaProductoModule),
      },
      {
        path: 'compra',
        data: { pageTitle: 'mercaExtraApp.compra.home.title' },
        loadChildren: () => import('./compra/compra.module').then(m => m.CompraModule),
      },
      {
        path: 'proveedor',
        data: { pageTitle: 'mercaExtraApp.proveedor.home.title' },
        loadChildren: () => import('./proveedor/proveedor.module').then(m => m.ProveedorModule),
      },
      {
        path: 'egreso',
        data: { pageTitle: 'mercaExtraApp.egreso.home.title' },
        loadChildren: () => import('./egreso/egreso.module').then(m => m.EgresoModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'mercaExtraApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'item-factura-venta',
        data: { pageTitle: 'mercaExtraApp.itemFacturaVenta.home.title' },
        loadChildren: () => import('./item-factura-venta/item-factura-venta.module').then(m => m.ItemFacturaVentaModule),
      },
      {
        path: 'reembolso',
        data: { pageTitle: 'mercaExtraApp.reembolso.home.title' },
        loadChildren: () => import('./reembolso/reembolso.module').then(m => m.ReembolsoModule),
      },
      {
        path: 'producto-favoritos',
        data: { pageTitle: 'mercaExtraApp.productoFavoritos.home.title' },
        loadChildren: () => import('./producto-favoritos/producto-favoritos.module').then(m => m.ProductoFavoritosModule),
      },
      {
        path: 'comentario',
        data: { pageTitle: 'mercaExtraApp.comentario.home.title' },
        loadChildren: () => import('./comentario/comentario.module').then(m => m.ComentarioModule),
      },
      {
        path: 'producto-promocion-home',
        data: { pageTitle: 'mercaExtraApp.productoPromocionHome.home.title' },
        loadChildren: () => import('./producto-promocion-home/producto-promocion-home.module').then(m => m.ProductoPromocionHomeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
