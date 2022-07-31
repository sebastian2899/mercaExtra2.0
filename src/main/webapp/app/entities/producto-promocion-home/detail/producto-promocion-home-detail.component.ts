import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductoPromocionHome } from '../producto-promocion-home.model';

@Component({
  selector: 'jhi-producto-promocion-home-detail',
  templateUrl: './producto-promocion-home-detail.component.html',
})
export class ProductoPromocionHomeDetailComponent implements OnInit {
  productoPromocionHome: IProductoPromocionHome | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoPromocionHome }) => {
      this.productoPromocionHome = productoPromocionHome;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
