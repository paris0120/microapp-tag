import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IServer } from 'app/shared/model/tag/server.model';
import { getEntity, updateEntity, createEntity, reset } from './server.reducer';
import { Button, Col, Form, Input, Row, Select } from 'antd';

export const ServerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const serverEntity = useAppSelector(state => state.tag.server.entity);
  const loading = useAppSelector(state => state.tag.server.loading);
  const updating = useAppSelector(state => state.tag.server.updating);
  const updateSuccess = useAppSelector(state => state.tag.server.updateSuccess);

  const handleClose = () => {
    navigate('/admin//server' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...serverEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };
  const decoders = [
    { value: '', label: 'Plain' },
    { value: 'AES-128', label: 'AES-128' },
    { value: 'AES-192', label: 'AES-192' },
    { value: 'AES-256', label: 'AES-256' },
    { value: 'RC4', label: 'RC4' },
  ];

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...serverEntity,
        };

  const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
  };
  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tagApp.tagServer.home.createOrEditLabel" data-cy="ServerCreateUpdateHeading">
            <Translate contentKey="tagApp.tagServer.home.createOrEditLabel">Create or edit a Server</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <Form {...layout} initialValues={defaultValues()} onFinish={saveEntity}>
              <Form.Item
                label={translate('tagApp.tagServer.server')}
                id="server-server"
                name="server"
                data-cy="server"
                rules={[
                  { required: true, message: translate('entity.validation.required') },
                  { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                ]}
              >
                <Input placeholder={translate('tagApp.tagServer.server')} />
              </Form.Item>
              <Form.Item label={translate('tagApp.tagServer.uuid')} id="server-uuid" name="uuid" data-cy="uuid">
                <Input placeholder={translate('tagApp.tagServer.uuid')} />
              </Form.Item>
              <Form.Item label={translate('tagApp.tagServer.decoder')} id="server-decoder" name="decoder" data-cy="decoder">
                <Select options={decoders} placeholder={translate('tagApp.tagServer.decoder')} />
              </Form.Item>
              <Form.Item label={translate('tagApp.tagServer.password')} id="server-password" name="password" data-cy="password">
                <Input placeholder={translate('tagApp.tagServer.password')} />
              </Form.Item>

              <Button type="link" id="cancel-save" data-cy="entityCreateCancelButton" href="/admin/tag/server" color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <Translate contentKey="entity.action.back">Back</Translate>
              </Button>

              <Button type="primary" htmlType="submit">
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </Form>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ServerUpdate;
