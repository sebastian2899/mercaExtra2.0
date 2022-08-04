import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProductoPromocionHome, ProductoPromocionHome } from '../producto-promocion-home.model';
import { ProductoPromocionHomeService } from '../service/producto-promocion-home.service';

@Component({
  selector: 'jhi-producto-promocion-home-update',
  templateUrl: './producto-promocion-home-update.component.html',
})
export class ProductoPromocionHomeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idProducto: [],
    descripcion: [],
    fechaAgregado: [],
  });

  constructor(
    protected productoPromocionHomeService: ProductoPromocionHomeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoPromocionHome }) => {
      if (productoPromocionHome.id === undefined) {
        const today = dayjs().startOf('day');
        productoPromocionHome.fechaAgregado = today;
      }

      this.updateForm(productoPromocionHome);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productoPromocionHome = this.createFromForm();
    if (productoPromocionHome.id !== undefined) {
      this.subscribeToSaveResponse(this.productoPromocionHomeService.update(productoPromocionHome));
    } else {
      this.subscribeToSaveResponse(this.productoPromocionHomeService.create(productoPromocionHome));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductoPromocionHome>>): void {
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

  protected updateForm(productoPromocionHome: IProductoPromocionHome): void {
    this.editForm.patchValue({
      id: productoPromocionHome.id,
      idProducto: productoPromocionHome.idProducto,
      descripcion: productoPromocionHome.descripcion,
      fechaAgregado: productoPromocionHome.fechaAgregado ? productoPromocionHome.fechaAgregado.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IProductoPromocionHome {
    return {
      ...new ProductoPromocionHome(),
      id: this.editForm.get(['id'])!.value,
      idProducto: this.editForm.get(['idProducto'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaAgregado: this.editForm.get(['fechaAgregado'])!.value
        ? dayjs(this.editForm.get(['fechaAgregado'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
