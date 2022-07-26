import dayjs from 'dayjs/esm';

export interface IComentario {
  id?: number;
  idComentario?: number | null;
  idProducto?: number | null;
  fechaComentario?: dayjs.Dayjs | null;
  login?: string | null;
  likes?: number | null;
  descripcion?: string | null;
  commentsResp?: IComentario[] | null;
}

export class Comentario implements IComentario {
  constructor(
    public id?: number,
    public idComentario?: number | null,
    public idProducto?: number | null,
    public fechaComentario?: dayjs.Dayjs | null,
    public login?: string | null,
    public likes?: number | null,
    public descripcion?: string | null,
    public commentsResp?: IComentario[] | null
  ) {}
}

export function getComentarioIdentifier(comentario: IComentario): number | undefined {
  return comentario.id;
}
