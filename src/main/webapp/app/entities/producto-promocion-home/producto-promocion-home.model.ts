import dayjs from 'dayjs/esm';

export interface IProductoPromocionHome {
  id?: number;
  idProducto?: number | null;
  descripcion?: string | null;
  fechaAgregado?: dayjs.Dayjs | null;
}

export class ProductoPromocionHome implements IProductoPromocionHome {
  constructor(
    public id?: number,
    public idProducto?: number | null,
    public descripcion?: string | null,
    public fechaAgregado?: dayjs.Dayjs | null
  ) {}
}

export function getProductoPromocionHomeIdentifier(productoPromocionHome: IProductoPromocionHome): number | undefined {
  return productoPromocionHome.id;
}
