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
import { ComentarioService } from 'app/entities/comentario/service/comentario.service';
import { Comentario, IComentario } from 'app/entities/comentario/comentario.model';
import dayjs from 'dayjs/esm';

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
  descripcionComentario?: string | null;
  loadComments?: IComentario[] = [];
  loadWhitOutComments?: IComentario[] = [];
  loadResponseComments?: IComentario[] = [];
  actionResp?: boolean | null;
  idComment?: number | null;
  descripcionComentarioRespuesta?: string | null;
  allCommentsPerProduct?: IComentario[] = [];

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected storageService: StateStorageService,
    protected router: Router,
    protected productoService: ProductoService,
    protected alertService: AlertService,
    protected productoFavoritosService: ProductoFavoritosService,
    protected comentarioService: ComentarioService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.producto = producto;
      if (this.producto) {
        this.asignateSimilarProducts(this.producto);
        this.findAnotherSimilarProducts(producto.categoria);
        this.isFavorite(this.producto.id!);
        this.loadCommentsMethod(producto.id!);
      }
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.calcularDescuentoProducto();
  }

  // loadAllCommentDistincIdComment(){

  // }

  calcularDescuentoProducto(): void {
    if (this.producto?.precioDescuento) {
      const descuento = (this.producto.precioDescuento * this.producto.precio!) / 100;
      this.valorConDescuento = this.producto.precio! - Number(descuento);
    }
  }

  loadCommentsMethod(idProduct: number): void {
    this.comentarioService.uploadCommentsProduct(idProduct).subscribe({
      next: (res: HttpResponse<IComentario[]>) => {
        this.loadComments = res.body ?? [];
        if (this.loadComments.length > 0) {
          this.loadWhitOutComments = this.loadComments.filter(comment => comment.idComentario === null);
          this.loadResponseComments = this.loadComments.filter(comment => comment.idComentario !== null);
        }
      },
      error: () => {
        this.loadComments = [];
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error al cargar los comentarios.',
        });
      },
    });
  }

  // loadCommentsMethod(idProduct:number):void{
  //   this.comentarioService.uploadCommentsProduct(idProduct).subscribe({
  //     next: (res: HttpResponse<IComentario[]>) => {
  //       this.loadComments = res.body ?? [];
  //       if(this.loadComments.length > 0){
  //          this.loadComments.forEach(element => {
  //           if(element.idProducto && element.idComentario){
  //             this.comentarioService.uploadCommentResponse(Number(element.idProducto),Number(element.idComentario)).subscribe(
  //              (res2: HttpResponse<IComentario[]>) => {
  //                 element.commentsResp = res2.body ?? [];
  //               }
  //             );
  //           }
  //          });
  //       }
  //     },
  //     error: () => {
  //       this.loadComments = [];
  //       this.alertService.addAlert({
  //         type: 'danger',
  //         message: 'Error al cargar los comentarios.',
  //       });
  //     }
  //   });
  // }

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

  addCommentLike(comment: IComentario): void {
    this.comentarioService.managementLikes(comment).subscribe({
      next: () => {
        this.loadCommentsMethod(this.producto!.id!);
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error',
        });
      },
    });
    this.loadCommentsMethod(this.producto!.id!);
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

  changeActionRespo(idComment: number | null): void {
    this.actionResp = !this.actionResp;
    if (idComment !== null) {
      this.idComment = idComment;
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

  createCommentResp(idComment: number, idProducto: number): void {
    const comment = new Comentario();
    comment.idProducto = idProducto;
    comment.idComentario = idComment;
    comment.descripcion = this.descripcionComentarioRespuesta;
    comment.fechaComentario = dayjs(new Date());

    this.comentarioService.create(comment).subscribe({
      next: () => {
        this.alertService.addAlert({
          type: 'success',
          message: 'Comentario respondido.',
        });
        this.descripcionComentarioRespuesta = null;
        this.actionResp = false;
        this.idComment = null;
        this.loadCommentsMethod(idProducto);
      },
    });
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

  createComent(idProducto: number): void {
    const comentario = new Comentario();
    comentario.idProducto = idProducto;
    comentario.fechaComentario = dayjs(new Date());
    comentario.descripcion = this.descripcionComentario;

    this.comentarioService.create(comentario).subscribe({
      next: () => {
        this.alertService.addAlert({
          type: 'success',
          message: 'Has realizado un comentario.',
        });
        this.descripcionComentario = null;
        this.loadCommentsMethod(idProducto);
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'No se pudo realizar el comentario.',
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
