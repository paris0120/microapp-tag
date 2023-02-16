export interface ITag {
  id?: number;
  tag?: string;
  textColor?: string | null;
  fillColor?: string | null;
  borderColor?: string | null;
  icon?: string | null;
  parentId?: number | null;
  parentType?: string | null;
  parentServer?: string;
  parentUuid?: string | null;
}

export const defaultValue: Readonly<ITag> = {};
