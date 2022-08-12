import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { HttpResponse } from '@angular/common/http';
import { CajaService } from 'app/entities/caja/service/caja.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductoPromocionHomeService } from 'app/entities/producto-promocion-home/service/producto-promocion-home.service';
import { IProductoPromocionHome } from 'app/entities/producto-promocion-home/producto-promocion-home.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('remember', { static: true }) content: ElementRef | undefined;
  account: Account | null = null;
  productos?: IProducto[] | null;
  productosDescuento?: IProducto[] | null;
  intervalId?: any;
  respNumber?: number | null;
  productosDisscountHome?: IProducto[] | null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private productoService: ProductoService,
    protected cajaService: CajaService,
    protected modalService: NgbModal,
    private productoDescuentoService: ProductoPromocionHomeService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.rememberCreationCaja();
    this.findDissmidProduts();
    this.getDisscountProduts();
  }

  getDisscountProduts(): void {
    this.productoDescuentoService.recuperarLiataProductoPromocionHome().subscribe({
      next: (res: HttpResponse<IProductoPromocionHome[]>) => {
        this.productosDisscountHome = res.body ?? [];
        this.productosDisscountHome.forEach(element => {
          const discount = (Number(element.precioDescuento) * Number(element.precio)) / 100;
          element.precioConDescuento = Number(element.precio) - Number(discount.toFixed(0));
        });
      },
    });
  }

  dleteProductHome(id: number): void {
    this.productoDescuentoService.deleteProductoDesc(id).subscribe(() => window.location.reload());
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  rememberCreationCaja(): void {
    this.intervalId = setInterval(() => {
      if (this.account?.login === 'admin') {
        this.cajaService.rememberCreationCaja().subscribe({
          next: (res: HttpResponse<number>) => {
            this.respNumber = res.body;
            this.respNumber === 1 ? clearInterval(this.intervalId) : this.modalService.open(this.content);
          },
        });
      }
    }, 3600000);
  }

  findDissmidProduts(): void {
    this.productoService.getDiscountProduts().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosDescuento = res.body ?? [];
        this.productosDescuento.forEach(element => {
          const discount = (Number(element.precioDescuento) * Number(element.precio)) / 100;
          element.precioConDescuento = Number(element.precio) - Number(discount.toFixed(0));
        });
      },
      error: () => {
        this.productosDescuento = [];
      },
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
