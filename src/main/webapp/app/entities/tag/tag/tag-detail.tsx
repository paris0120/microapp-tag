import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tag.reducer';

export const TagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tagEntity = useAppSelector(state => state.tag.tag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tagDetailsHeading">
          <Translate contentKey="tagApp.tagTag.detail.title">Tag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tagEntity.id}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="tagApp.tagTag.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{tagEntity.tag}</dd>
          <dt>
            <span id="textColor">
              <Translate contentKey="tagApp.tagTag.textColor">Text Color</Translate>
            </span>
          </dt>
          <dd>{tagEntity.textColor}</dd>
          <dt>
            <span id="fillColor">
              <Translate contentKey="tagApp.tagTag.fillColor">Fill Color</Translate>
            </span>
          </dt>
          <dd>{tagEntity.fillColor}</dd>
          <dt>
            <span id="borderColor">
              <Translate contentKey="tagApp.tagTag.borderColor">Border Color</Translate>
            </span>
          </dt>
          <dd>{tagEntity.borderColor}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="tagApp.tagTag.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{tagEntity.icon}</dd>
          <dt>
            <span id="parentId">
              <Translate contentKey="tagApp.tagTag.parentId">Parent Id</Translate>
            </span>
          </dt>
          <dd>{tagEntity.parentId}</dd>
          <dt>
            <span id="parentType">
              <Translate contentKey="tagApp.tagTag.parentType">Parent Type</Translate>
            </span>
          </dt>
          <dd>{tagEntity.parentType}</dd>
          <dt>
            <span id="parentServer">
              <Translate contentKey="tagApp.tagTag.parentServer">Parent Server</Translate>
            </span>
          </dt>
          <dd>{tagEntity.parentServer}</dd>
          <dt>
            <span id="parentUuid">
              <Translate contentKey="tagApp.tagTag.parentUuid">Parent Uuid</Translate>
            </span>
          </dt>
          <dd>{tagEntity.parentUuid}</dd>
        </dl>
        <Button tag={Link} to="/tag/tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/tag/${tagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TagDetail;
