import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IProducto } from '../producto.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { ProductoService } from '../service/producto.service';
import { HttpResponse } from '@angular/common/http';
import { AlertService } from 'app/core/util/alert.service';
import { ProductoFavoritosService } from 'app/entities/producto-favoritos/service/producto-favoritos.service';
import { ProductoFavoritos } from 'app/entities/producto-favoritos/producto-favoritos.model';

@Component({
  selector: 'jhi-producto-detail',
  templateUrl: './producto-detail.component.html',
})
export class ProductoDetailComponent implements OnInit {
  producto: IProducto | null = null;
  account?: Account | null;
  valorConDescuento?: number | null;
  productosSimilares?: IProducto[] = [];
  anotherSimilarProduct?: IProducto[] = [];
  isFavProduct?: boolean | null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected storageService: StateStorageService,
    protected router: Router,
    protected productoService: ProductoService,
    protected alertService: AlertService,
    protected productoFavoritosService: ProductoFavoritosService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.producto = producto;
      if (this.producto) {
        this.asignateSimilarProducts(this.producto);
        this.findAnotherSimilarProducts(producto.categoria);
        this.isFavorite(this.producto.id!);
      }
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.calcularDescuentoProducto();
  }

  calcularDescuentoProducto(): void {
    if (this.producto?.precioDescuento) {
      const descuento = (this.producto.precioDescuento * this.producto.precio!) / 100;
      this.valorConDescuento = this.producto.precio! - Number(descuento);
    }
  }

  reload(): void {
    window.location.reload();
  }

  asignateSimilarProducts(producto: IProducto): void {
    this.productoService.getSimilarProductos(producto).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosSimilares = res.body ?? [];
      },
      error: () => {
        this.productosSimilares = [];
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error al cargar los productos similares.',
        });
      },
    });
  }

  findAnotherSimilarProducts(categoria: string): void {
    if (categoria) {
      this.productoService.getAnotherSimilarProducts(categoria).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.anotherSimilarProduct = res.body ?? [];
        },
        error: () => {
          this.anotherSimilarProduct = [];
        },
      });
    }
  }

  managementFavoriteProducts(id: number): void {
    const producFav = new ProductoFavoritos();
    if (!this.producto?.isFavorite) {
      producFav.idProduct = id;
      this.productoFavoritosService.create(producFav).subscribe({
        next: () => {
          this.producto!.isFavorite = true;
          this.alertService.addAlert({
            type: 'success',
            message: 'Producto Agregado a favoritos.',
          });
        },
        error: () => {
          this.alertService.addAlert({
            type: 'danger',
            message: 'No se pudo agregar el producto a favoritos.',
          });
        },
      });
    } else {
      this.producto.isFavorite = false;
      this.productoFavoritosService.delete(id).subscribe(() => {
        this.alertService.addAlert({
          type: 'info',
          message: 'Producto eliminado de favoritos.',
        });
      });
    }
  }

  isFavorite(id: number): void {
    this.productoService.validateIfItFavorite(id).subscribe({
      next: (res: HttpResponse<boolean>) => {
        this.isFavProduct = res.body;
        this.isFavProduct === true ? (this.producto!.isFavorite = true) : (this.producto!.isFavorite = false);
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'No se pudo hacer la validacion correctamente.',
        });
      },
    });
  }

  pasoParametroProducto(producto: IProducto): void {
    this.storageService.pasoParametroProducto(producto);
    this.router.navigate(['factura/new']);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
