export interface IParentType {
  id?: number;
  parentId?: number;
  parentType?: string;
  server?: string;
  userManageable?: boolean;
}

export const defaultValue: Readonly<IParentType> = {
  userManageable: false,
};
