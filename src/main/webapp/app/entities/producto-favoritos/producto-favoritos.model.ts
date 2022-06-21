import dayjs from 'dayjs/esm';

export interface IProductoFavoritos {
  id?: number;
  idProduct?: number | null;
  login?: string | null;
  lastUpdate?: dayjs.Dayjs | null;
  estado?: string | null;
}

export class ProductoFavoritos implements IProductoFavoritos {
  constructor(
    public id?: number,
    public idProduct?: number | null,
    public login?: string | null,
    public lastUpdate?: dayjs.Dayjs | null,
    public estado?: string | null
  ) {}
}

export function getProductoFavoritosIdentifier(productoFavoritos: IProductoFavoritos): number | undefined {
  return productoFavoritos.id;
}
