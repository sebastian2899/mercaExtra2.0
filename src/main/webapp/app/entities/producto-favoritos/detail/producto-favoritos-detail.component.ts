import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductoFavoritos } from '../producto-favoritos.model';

@Component({
  selector: 'jhi-producto-favoritos-detail',
  templateUrl: './producto-favoritos-detail.component.html',
})
export class ProductoFavoritosDetailComponent implements OnInit {
  productoFavoritos: IProductoFavoritos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoFavoritos }) => {
      this.productoFavoritos = productoFavoritos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
