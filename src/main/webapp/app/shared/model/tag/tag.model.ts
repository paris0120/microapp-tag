export interface ITag {
  id?: number;
  tag?: string | null;
  color?: string | null;
  icon?: string | null;
  parentId?: number;
  parentType?: string;
  parentServer?: string;
  parentUuid?: string;
}

export const defaultValue: Readonly<ITag> = {};
