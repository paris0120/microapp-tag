import { library } from '@fortawesome/fontawesome-svg-core';

import * as Icons from '@fortawesome/free-solid-svg-icons';

export const iconList = Object.keys(Icons)
  .filter(key => key !== 'fas' && key !== 'prefix')
  .map(icon => Icons[icon]);
export const loadIcons = () => {
  library.add(...iconList);
};

export const iconKeys = [];
iconList.map(icon => {
  if (iconKeys.indexOf(icon.iconName) == -1) iconKeys.push(icon.iconName);
});
