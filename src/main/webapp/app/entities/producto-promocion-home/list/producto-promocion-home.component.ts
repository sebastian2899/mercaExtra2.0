import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';
import { ProductoPromocionHomeDeleteDialogComponent } from '../delete/producto-promocion-home-delete-dialog.component';
import { IProducto } from 'app/entities/producto/producto.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-producto-promocion-home',
  templateUrl: './producto-promocion-home.component.html',
  styleUrls: ['./producto-promocion-home.component.css'],
})
export class ProductoPromocionHomeComponent implements OnInit {
  productoPromocionHomes?: IProductoPromocionHome[];
  isLoading = false;
  productos?: IProducto[] = [];
  idProducto?: number | null;
  descripcion?: string | null;
  crearDescripcion?: boolean | null;

  constructor(
    protected productoPromocionHomeService: ProductoPromocionHomeService,
    protected modalService: NgbModal,
    protected alerService: AlertService
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.productoPromocionHomeService.query().subscribe({
      next: (res2: HttpResponse<IProductoPromocionHome[]>) => {
        this.isLoading = false;
        this.productoPromocionHomes = res2.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.consultarProductosDescuento();
  }

  generarDescripcion(id: number | null): void {
    this.crearDescripcion = !this.crearDescripcion;
    if (id !== null) {
      this.idProducto = id;
    }
  }

  cancel(): void {
    this.crearDescripcion = false;
  }

  guardarProductoHome(): void {
    const productoParaHome = new ProductoPromocionHome();
    productoParaHome.idProducto = this.idProducto;
    productoParaHome.descripcion = this.descripcion;
    if (productoParaHome.idProducto && productoParaHome.descripcion) {
      this.productoPromocionHomeService.create(productoParaHome).subscribe(() => {
        this.alerService.addAlert({
          type: 'success',
          message: 'Se ha agregado el producto a la lista de inicio.',
        });
        this.limpiar();
        this.crearDescripcion = false;
        window.location.reload();
      });
    }
  }

  limpiar(): void {
    this.idProducto = null;
    this.descripcion = null;
  }

  consultarProductosDescuento(): void {
    this.productoPromocionHomeService.recuperarProductoDescuento().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.alerService.addAlert({
          type: 'danger',
          message: 'Error al recuperar los productos con descuento',
        });
      },
    });
  }

  trackId(index: number, item: IProductoPromocionHome): number {
    return item.id!;
  }

  delete(productoPromocionHome: IProductoPromocionHome): void {
    const modalRef = this.modalService.open(ProductoPromocionHomeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productoPromocionHome = productoPromocionHome;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        //this.loadAll();
      }
    });
  }
}
