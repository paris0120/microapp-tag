import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { isNumber, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IParentType } from 'app/shared/model/tag/parent-type.model';
import { getEntity, updateEntity, createEntity, reset } from './parent-type.reducer';
import { Button, Col, Form, Input, InputNumber, Row, Select, Space, Switch } from 'antd';
import axios from 'axios';
import { iconKeys } from 'app/config/icon-loader';
import { IServer } from 'app/shared/model/tag/server.model';

export const ParentTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parentTypeEntity = useAppSelector(state => state.tag.parentType.entity);
  const loading = useAppSelector(state => state.tag.parentType.loading);
  const updating = useAppSelector(state => state.tag.parentType.updating);
  const updateSuccess = useAppSelector(state => state.tag.parentType.updateSuccess);

  const handleClose = () => {
    navigate('/admin/tag/parent-type' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  const [servers, setServers] = useState([]);

  const apiServerUrl = 'services/tag/api/admin/servers';
  useEffect(() => {
    const requestUrl = `${apiServerUrl}`;
    axios.get<IServer[]>(requestUrl).then(iservers => {
      setServers(
        iservers.data.map(server => {
          return { value: server.server, label: server.server };
        })
      );
    });
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...parentTypeEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...parentTypeEntity,
        };

  const layout = {
    labelCol: { span: 10 },
    wrapperCol: { span: 14 },
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tagApp.tagParentType.home.createOrEditLabel" data-cy="ParentTypeCreateUpdateHeading">
            <Translate contentKey="tagApp.tagParentType.home.createOrEditLabel">Create or edit a ParentType</Translate>
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
                label={translate('tagApp.tagParentType.topic')}
                id="parent-type-topic"
                name="topic"
                data-cy="topic"
                rules={[
                  { required: true, message: translate('entity.validation.required') },
                  { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                ]}
              >
                <Input placeholder={translate('tagApp.tagParentType.topic')} />
              </Form.Item>
              <Form.Item
                label={translate('tagApp.tagParentType.parentId')}
                id="parent-type-parentId"
                name="parentId"
                data-cy="parentId"
                rules={[{ required: true, message: translate('entity.validation.required') }]}
              >
                <InputNumber min={-1} placeholder={translate('tagApp.tagParentType.parentId')} />
              </Form.Item>
              <Form.Item
                label={translate('tagApp.tagParentType.parentType')}
                id="parent-type-parentType"
                name="parentType"
                data-cy="parentType"
                rules={[
                  { required: true, message: translate('entity.validation.required') },
                  { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                ]}
              >
                <Input placeholder={translate('tagApp.tagParentType.parentType')} />
              </Form.Item>
              <Form.Item
                label={translate('tagApp.tagParentType.server')}
                id="parent-type-server"
                name="server"
                data-cy="server"
                rules={[{ required: true, message: translate('entity.validation.required') }]}
              >
                <Select options={servers}></Select>
              </Form.Item>
              <Form.Item
                label={translate('tagApp.tagParentType.userManageable')}
                id="parent-type-userManageable"
                name="userManageable"
                data-cy="userManageable"
              >
                <Switch />
              </Form.Item>
              <Form.Item
                label={translate('tagApp.tagParentType.isEncrypted')}
                id="parent-type-isEncrypted"
                name="isEncrypted"
                data-cy="isEncrypted"
              >
                <Switch></Switch>
              </Form.Item>

              <Button type="link" id="cancel-save" data-cy="entityCreateCancelButton" href="/admin/tag/parent-type" color="info">
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

export default ParentTypeUpdate;
