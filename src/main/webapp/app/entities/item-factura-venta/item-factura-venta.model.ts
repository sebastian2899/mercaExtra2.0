export interface IItemFacturaVenta {
  id?: number;
  idFactura?: number | null;
  idProducto?: number | null;
  cantidad?: number | null;
  precio?: number | null;
  nombreProducto?: string | null;
  precioOriginal?: number | null;
  precioDesc?: number | null;
  imagenContentType?: string | null;
  imagen?: any | null;
}

export class ItemFacturaVenta implements IItemFacturaVenta {
  constructor(
    public id?: number,
    public idFactura?: number | null,
    public idProducto?: number | null,
    public cantidad?: number | null,
    public precio?: number | null,
    public nombreProducto?: string | null,
    public precioOriginal?: number | null,
    public precioDesc?: number | null,
    public imagenContentType?: string | null,
    public imagen?: any | null
  ) {}
}

export function getItemFacturaVentaIdentifier(itemFacturaVenta: IItemFacturaVenta): number | undefined {
  return itemFacturaVenta.id;
}
