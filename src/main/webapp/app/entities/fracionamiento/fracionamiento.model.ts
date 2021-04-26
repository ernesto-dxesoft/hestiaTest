import * as dayjs from 'dayjs';
import { IAddress } from 'app/entities/address/address.model';
import { StatusFraccionamiento } from 'app/entities/enumerations/status-fraccionamiento.model';

export interface IFracionamiento {
  id?: number;
  name?: string;
  startDate?: dayjs.Dayjs;
  totalHouses?: number;
  costByHouse?: number;
  status?: StatusFraccionamiento;
  contract?: string;
  address?: IAddress | null;
}

export class Fracionamiento implements IFracionamiento {
  constructor(
    public id?: number,
    public name?: string,
    public startDate?: dayjs.Dayjs,
    public totalHouses?: number,
    public costByHouse?: number,
    public status?: StatusFraccionamiento,
    public contract?: string,
    public address?: IAddress | null
  ) {}
}

export function getFracionamientoIdentifier(fracionamiento: IFracionamiento): number | undefined {
  return fracionamiento.id;
}
