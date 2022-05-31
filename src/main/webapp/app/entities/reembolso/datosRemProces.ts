export interface IDatosReembolso {
  id?: number | null;
  fechaPedido?: string | null;
  valorFactura?: number | null;
  descripcion?: string | null;
  nombreUsuario?: string | null;
}

export class DatosReembolso implements IDatosReembolso {
  constructor(
    public id?: number | null,
    public fechaPedido?: string | null,
    public valorFactura?: number | null,
    public descripcion?: string | null,
    public nombreUsuario?: string | null
  ) {}
}
