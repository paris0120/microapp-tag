export interface IServer {
  id?: number;
  server?: string;
  uuid?: string | null;
  decoder?: string | null;
  password?: string | null;
}

export const defaultValue: Readonly<IServer> = {};
