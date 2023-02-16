import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './parent-type.reducer';

export const ParentTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const parentTypeEntity = useAppSelector(state => state.tag.parentType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="parentTypeDetailsHeading">
          <Translate contentKey="tagApp.tagParentType.detail.title">ParentType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{parentTypeEntity.id}</dd>
          <dt>
            <span id="parentId">
              <Translate contentKey="tagApp.tagParentType.parentId">Parent Id</Translate>
            </span>
          </dt>
          <dd>{parentTypeEntity.parentId}</dd>
          <dt>
            <span id="parentType">
              <Translate contentKey="tagApp.tagParentType.parentType">Parent Type</Translate>
            </span>
          </dt>
          <dd>{parentTypeEntity.parentType}</dd>
          <dt>
            <span id="server">
              <Translate contentKey="tagApp.tagParentType.server">Server</Translate>
            </span>
          </dt>
          <dd>{parentTypeEntity.server}</dd>
          <dt>
            <span id="userManageable">
              <Translate contentKey="tagApp.tagParentType.userManageable">User Manageable</Translate>
            </span>
          </dt>
          <dd>{parentTypeEntity.userManageable ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/tag/parent-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/parent-type/${parentTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ParentTypeDetail;
