import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/tag/'));
  }, [lastChange]);

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/admin/tag/tag">
        <Translate contentKey="global.menu.entities.tagTag" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/admin/tag/server">
        <Translate contentKey="global.menu.entities.tagServer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/admin/tag/parent-type">
        <Translate contentKey="global.menu.entities.tagParentType" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
