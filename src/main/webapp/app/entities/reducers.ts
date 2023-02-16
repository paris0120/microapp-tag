import tag from 'app/entities/tag/tag/tag.reducer';
import server from 'app/entities/tag/server/server.reducer';
import parentType from 'app/entities/tag/parent-type/parent-type.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  tag,
  server,
  parentType,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
