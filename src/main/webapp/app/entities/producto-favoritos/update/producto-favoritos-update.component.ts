import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProductoFavoritos, ProductoFavoritos } from '../producto-favoritos.model';
import { ProductoFavoritosService } from '../service/producto-favoritos.service';

@Component({
  selector: 'jhi-producto-favoritos-update',
  templateUrl: './producto-favoritos-update.component.html',
})
export class ProductoFavoritosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idProduct: [],
    login: [],
    lastUpdate: [],
    estado: [],
  });

  constructor(
    protected productoFavoritosService: ProductoFavoritosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoFavoritos }) => {
      if (productoFavoritos.id === undefined) {
        const today = dayjs().startOf('day');
        productoFavoritos.lastUpdate = today;
      }

      this.updateForm(productoFavoritos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productoFavoritos = this.createFromForm();
    if (productoFavoritos.id !== undefined) {
      this.subscribeToSaveResponse(this.productoFavoritosService.update(productoFavoritos));
    } else {
      this.subscribeToSaveResponse(this.productoFavoritosService.create(productoFavoritos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductoFavoritos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productoFavoritos: IProductoFavoritos): void {
    this.editForm.patchValue({
      id: productoFavoritos.id,
      idProduct: productoFavoritos.idProduct,
      login: productoFavoritos.login,
      lastUpdate: productoFavoritos.lastUpdate ? productoFavoritos.lastUpdate.format(DATE_TIME_FORMAT) : null,
      estado: productoFavoritos.estado,
    });
  }

  protected createFromForm(): IProductoFavoritos {
    return {
      ...new ProductoFavoritos(),
      id: this.editForm.get(['id'])!.value,
      idProduct: this.editForm.get(['idProduct'])!.value,
      login: this.editForm.get(['login'])!.value,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
