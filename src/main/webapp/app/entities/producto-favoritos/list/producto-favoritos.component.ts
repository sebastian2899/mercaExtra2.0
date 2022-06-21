import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';
import { ProductoFavoritosDeleteDialogComponent } from '../delete/producto-favoritos-delete-dialog.component';

@Component({
  selector: 'jhi-producto-favoritos',
  templateUrl: './producto-favoritos.component.html',
})
export class ProductoFavoritosComponent implements OnInit {
  productoFavoritos?: IProductoFavoritos[];
  isLoading = false;

  constructor(protected productoFavoritosService: ProductoFavoritosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productoFavoritosService.query().subscribe({
      next: (res: HttpResponse<IProductoFavoritos[]>) => {
        this.isLoading = false;
        this.productoFavoritos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
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
