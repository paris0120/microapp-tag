export interface IParentType {
  id?: number;
  topic?: string;
  parentId?: number;
  parentType?: string;
  server?: string;
  userManageable?: boolean;
  isEncrypted?: boolean;
}

export const defaultValue: Readonly<IParentType> = {
  userManageable: false,
  isEncrypted: false,
};
