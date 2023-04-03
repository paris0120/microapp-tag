import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Translate, byteSize, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity, updateEntity } from './server.reducer';
import { Button, Col, Descriptions, Form, Input, Row, Select, Space, Tooltip } from 'antd';

export const ServerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();
  const [status, setStatus] = useState('display');

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const saveEntity = values => {
    const entity = {
      ...serverEntity,
      ...values,
    };
    dispatch(updateEntity(entity));
    setStatus('display');
  };

  const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
  };

  const decoders = [
    { value: '', label: 'Plain' },
    { value: 'AES-128', label: 'AES-128' },
    { value: 'AES-192', label: 'AES-192' },
    { value: 'AES-256', label: 'AES-256' },
    { value: 'RC4', label: 'RC4' },
  ];

  const serverEntity = useAppSelector(state => state.tag.server.entity);
  return (
    <Row>
      <Col md="8" offset={1} span={22}>
        <Form initialValues={serverEntity} onFinish={saveEntity}>
          <Descriptions {...layout} title="Server Info">
            <Descriptions.Item span={24} label={translate('tagApp.tagServer.server')}>
              {status == 'server' ? (
                <Form.Item
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
              ) : (
                <Tooltip title="Click to edit.">
                  {serverEntity.server && serverEntity.server.length > 0 ? (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('server')}>
                      {serverEntity.server}
                    </a>
                  ) : (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('server')}>
                      Add
                    </a>
                  )}
                </Tooltip>
              )}
            </Descriptions.Item>

            <Descriptions.Item span={24} label={translate('tagApp.tagServer.uuid')}>
              {status == 'uuid' ? (
                <Form.Item id="server-uuid" name="uuid" data-cy="uuid">
                  <Input placeholder={translate('tagApp.tagServer.uuid')} />
                </Form.Item>
              ) : (
                <Tooltip title="Click to edit.">
                  {serverEntity.uuid && serverEntity.uuid.length > 0 ? (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('uuid')}>
                      {serverEntity.uuid}
                    </a>
                  ) : (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('uuid')}>
                      Add
                    </a>
                  )}
                </Tooltip>
              )}
            </Descriptions.Item>

            <Descriptions.Item span={24} label={translate('tagApp.tagServer.decoder')}>
              {status == 'decoder' ? (
                <>
                  <Form.Item id="server-decoder" name="decoder" data-cy="decoder">
                    <Select options={decoders} placeholder={translate('tagApp.tagServer.decoder')} />
                  </Form.Item>

                  <Button type="primary" htmlType="submit">
                    <FontAwesomeIcon icon="save" />
                    &nbsp;
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </Button>
                </>
              ) : (
                <Tooltip title="Click to edit.">
                  {serverEntity.decoder && serverEntity.decoder.length > 0 ? (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('decoder')}>
                      {serverEntity.decoder}
                    </a>
                  ) : (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('decoder')}>
                      Add
                    </a>
                  )}
                </Tooltip>
              )}
            </Descriptions.Item>

            <Descriptions.Item span={24} label={translate('tagApp.tagServer.password')}>
              {status == 'password' ? (
                <Form.Item id="server-password" name="password" data-cy="password">
                  <Input placeholder={translate('tagApp.tagServer.password')} />
                </Form.Item>
              ) : (
                <Tooltip title="Click to edit.">
                  {serverEntity.password && serverEntity.password.length > 0 ? (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('password')}>
                      {serverEntity.password}
                    </a>
                  ) : (
                    <a style={{ display: 'inline', float: 'right', fontWeight: 'normal' }} onClick={event => setStatus('password')}>
                      Add
                    </a>
                  )}
                </Tooltip>
              )}
            </Descriptions.Item>
          </Descriptions>

          <Button type="link" href="/tag/server" color="info" data-cy="entityDetailsBackButton">
            <Space>
              {' '}
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Space>
          </Button>
        </Form>
      </Col>
    </Row>
  );
};

export default ServerDetail;
