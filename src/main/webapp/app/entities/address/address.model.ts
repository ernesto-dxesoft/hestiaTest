export interface IAddress {
  id?: number;
  street?: string;
  number?: string;
  suite?: string | null;
  colonia?: string | null;
  postalCode?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public street?: string,
    public number?: string,
    public suite?: string | null,
    public colonia?: string | null,
    public postalCode?: string | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
