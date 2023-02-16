import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './server.reducer';

export const ServerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serverEntity = useAppSelector(state => state.tag.server.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="serverDetailsHeading">
          <Translate contentKey="tagApp.tagServer.detail.title">Server</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{serverEntity.id}</dd>
          <dt>
            <span id="server">
              <Translate contentKey="tagApp.tagServer.server">Server</Translate>
            </span>
          </dt>
          <dd>{serverEntity.server}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="tagApp.tagServer.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{serverEntity.uuid}</dd>
          <dt>
            <span id="decoder">
              <Translate contentKey="tagApp.tagServer.decoder">Decoder</Translate>
            </span>
          </dt>
          <dd>{serverEntity.decoder}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="tagApp.tagServer.password">Password</Translate>
            </span>
          </dt>
          <dd>{serverEntity.password}</dd>
        </dl>
        <Button tag={Link} to="/tag/server" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/server/${serverEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServerDetail;
