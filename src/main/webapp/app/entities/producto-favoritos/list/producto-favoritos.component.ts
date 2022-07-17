import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';
import { ProductoFavoritosDeleteDialogComponent } from '../delete/producto-favoritos-delete-dialog.component';
import { IProducto } from 'app/entities/producto/producto.model';
import dayjs from 'dayjs/esm';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-producto-favoritos',
  templateUrl: './producto-favoritos.component.html',
})
export class ProductoFavoritosComponent implements OnInit {
  @ViewChild('openHiddenProducts', { static: true }) content: ElementRef | undefined;

  productoFavoritos?: IProductoFavoritos[];
  productos?: IProducto[];
  productosOcultos?: IProducto[];
  isLoading = false;
  lastUpdateV?: string | null;
  totalProductos?: number | null;
  totalOcultos?: number | null;
  firstProduct?: IProducto | null;

  constructor(
    protected productoFavoritosService: ProductoFavoritosService,
    protected modalService: NgbModal,
    protected alerService: AlertService
  ) {}

  loadFavoriteProductos(): void {
    this.productoFavoritosService.query().subscribe({
      next: (res: HttpResponse<IProductoFavoritos[]>) => {
        this.productoFavoritos = res.body ?? [];
      },
    });
  }

  loadAll(): void {
    this.isLoading = true;
    this.totalProductos = 0;
    this.productoFavoritosService.favoriteProducts().subscribe({
      next: (res: HttpResponse<IProductoFavoritos[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
        this.productos.forEach(element => (element.id ? this.totalProductos!++ : (this.totalProductos = 0)));
        this.firstProduct = this.productos[0];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllHiden(): void {
    this.isLoading = true;
    this.totalOcultos = 0;
    this.productoFavoritosService.favoriteHiddenProducts().subscribe({
      next: (res: HttpResponse<IProductoFavoritos[]>) => {
        this.isLoading = false;
        this.productosOcultos = res.body ?? [];
        this.productosOcultos.forEach(element => (element.id ? this.totalOcultos!++ : (this.totalOcultos = 0)));
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.lastUpdate();
    this.loadAllHiden();
    this.loadFavoriteProductos();
  }

  seeHiddenProducts(): void {
    this.modalService.open(this.content, { backdrop: 'static', scrollable: true, size: 'lg' });
  }

  changeStateProduct(idProducto: number, accion: string): void {
    this.productoFavoritos?.forEach(element => {
      if (element.idProduct === idProducto) {
        const productoFav = element;
        productoFav.estado = accion;
        productoFav.lastUpdate = dayjs(new Date());
        const message =
          accion === 'Visible' ? 'El producto se cambio a la lista principal de favoritos.' : 'Producto ocultado de favoritos';
        this.productoFavoritosService.update(productoFav).subscribe({
          next: () => {
            this.alerService.addAlert({
              type: 'success',
              message: String(message),
            });
            this.loadAll();
            this.loadAllHiden();
            window.scrollTo(0, 0);
          },
          error: () => {
            this.alerService.addAlert({
              type: 'danger',
              message: 'Error al ocultar el producto de favoritos.',
            });
          },
        });
      }
    });
  }

  changeToFirst(producto: IProducto): void {
    this.productoFavoritosService.goFirst(producto).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
        this.loadAll();
        this.alerService.addAlert({
          type: 'success',
          message: 'Producto movido a la primera posición.',
        });
        window.scroll({ top: 0, left: 0, behavior: 'smooth' });
      },
      error: () => {
        this.alerService.addAlert({
          type: 'danger',
          message: 'Error al cambiar la posicino del producto favorito.',
        });
      },
    });
  }

  deleteFavorite(id: number): void {
    this.productoFavoritosService.delete(id).subscribe({
      next: () => {
        this.alerService.addAlert({
          type: 'success',
          message: 'Producto eliminado de favoritos.',
        });
        this.loadAll();
        window.location.assign('/producto-favoritos');
      },
      error: () => {
        this.alerService.addAlert({
          type: 'danger',
          message: 'Error al eliminar el producto de favoritos.',
        });
      },
    });
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  lastUpdate(): void {
    let fech = null;
    this.productoFavoritosService.lastUpdate().subscribe(res => {
      fech = res.body;
      const date = dayjs(fech, 'YYYY-DD-MM');
      this.lastUpdateV = date.format('YYYY-DD-MM');
    });
  }

  trackId(index: number, item: IProductoFavoritos): number {
    return item.id!;
  }

  delete(productoFavoritos: IProductoFavoritos): void {
    const modalRef = this.modalService.open(ProductoFavoritosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productoFavoritos = productoFavoritos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
