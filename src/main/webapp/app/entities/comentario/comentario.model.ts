import dayjs from 'dayjs/esm';

export interface IComentario {
  id?: number;
  idComentario?: number | null;
  fechaComentario?: dayjs.Dayjs | null;
  login?: string | null;
  like?: number | null;
  descripcion?: string | null;
}

export class Comentario implements IComentario {
  constructor(
    public id?: number,
    public idComentario?: number | null,
    public fechaComentario?: dayjs.Dayjs | null,
    public login?: string | null,
    public like?: number | null,
    public descripcion?: string | null
  ) {}
}

export function getComentarioIdentifier(comentario: IComentario): number | undefined {
  return comentario.id;
}
