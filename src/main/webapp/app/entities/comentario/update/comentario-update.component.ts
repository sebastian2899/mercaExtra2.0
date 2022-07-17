import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IComentario, Comentario } from '../comentario.model';
import { ComentarioService } from '../service/comentario.service';

@Component({
  selector: 'jhi-comentario-update',
  templateUrl: './comentario-update.component.html',
})
export class ComentarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idComentario: [],
    fechaComentario: [],
    login: [],
    like: [],
    descripcion: [],
  });

  constructor(protected comentarioService: ComentarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comentario }) => {
      if (comentario.id === undefined) {
        const today = dayjs().startOf('day');
        comentario.fechaComentario = today;
      }

      this.updateForm(comentario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comentario = this.createFromForm();
    if (comentario.id !== undefined) {
      this.subscribeToSaveResponse(this.comentarioService.update(comentario));
    } else {
      this.subscribeToSaveResponse(this.comentarioService.create(comentario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComentario>>): void {
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

  protected updateForm(comentario: IComentario): void {
    this.editForm.patchValue({
      id: comentario.id,
      idComentario: comentario.idComentario,
      fechaComentario: comentario.fechaComentario ? comentario.fechaComentario.format(DATE_TIME_FORMAT) : null,
      login: comentario.login,
      like: comentario.like,
      descripcion: comentario.descripcion,
    });
  }

  protected createFromForm(): IComentario {
    return {
      ...new Comentario(),
      id: this.editForm.get(['id'])!.value,
      idComentario: this.editForm.get(['idComentario'])!.value,
      fechaComentario: this.editForm.get(['fechaComentario'])!.value
        ? dayjs(this.editForm.get(['fechaComentario'])!.value, DATE_TIME_FORMAT)
        : undefined,
      login: this.editForm.get(['login'])!.value,
      like: this.editForm.get(['like'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
