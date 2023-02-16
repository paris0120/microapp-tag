import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity, updateEntity, createEntity, reset } from './tag.reducer';
import { AutoComplete, Button, Col, Form, Input, Row, Select, Space, Typography } from 'antd';
import { Colorpicker } from 'antd-colorpicker';
import { iconList } from 'app/config/icon-loader';
import axios from 'axios';

export const TagUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { Text, Link } = Typography;

  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const apiUrl = 'services/tag/api/types';

  const tagEntity = useAppSelector(state => state.tag.tag.entity);
  const loading = useAppSelector(state => state.tag.tag.loading);
  const updating = useAppSelector(state => state.tag.tag.updating);
  const updateSuccess = useAppSelector(state => state.tag.tag.updateSuccess);

  const [typeMap, setTypeMap] = useState({});
  const [types, setTypes] = useState([]);
  const [servers, setServers] = useState([]);

  const iconOptions = iconList.map(icon => {
    return {
      value: icon.iconName,
      label: (
        <Space>
          <FontAwesomeIcon title={icon.iconName} icon={icon.iconName} />
          <Text>{icon.iconName}</Text>
        </Space>
      ),
    };
  });

  const handleClose = () => {
    navigate('/tag/tag' + location.search);
  };

  const handleSelectServer = (server: string) => {
    setTypes(
      typeMap[server].map(t => {
        return { value: t, label: t };
      })
    );
  };

  useEffect(() => {
    const requestUrl = `${apiUrl}`;
    axios.get<Map<string, string[]>>(requestUrl).then(map => {
      setServers(
        Object.keys(map.data).map(name => {
          return { value: name, label: name };
        })
      );
      setTypeMap(map.data);
      console.log(typeMap);
      console.log(map.data);
    });
  }, []);

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
      ...tagEntity,
      ...values,
    };

    if (entity.textColor) entity.textColor = entity.textColor.hex;

    if (entity.fillColor) entity.fillColor = entity.fillColor.hex;

    if (entity.borderColor) entity.borderColor = entity.borderColor.hex;

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
          ...tagEntity,
        };

  const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tagApp.tagTag.home.createOrEditLabel" data-cy="TagCreateUpdateHeading">
            <Translate contentKey="tagApp.tagTag.home.createOrEditLabel">Create or edit a Tag</Translate>
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
                label={translate('tagApp.tagTag.tag')}
                id="tag-tag"
                name="tag"
                data-cy="tag"
                rules={[
                  { required: true, message: translate('entity.validation.required') },
                  { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                ]}
              >
                <Input placeholder={translate('tagApp.tagTag.tag')} />
              </Form.Item>

              <Form.Item
                label={translate('tagApp.tagTag.parentServer')}
                id="tag-parentServer"
                name="parentServer"
                data-cy="parentServer"
                rules={[{ required: true, message: translate('entity.validation.required') }]}
              >
                <Select options={servers} onChange={handleSelectServer}></Select>
              </Form.Item>

              <Form.Item label={translate('tagApp.tagTag.parentType')} id="tag-parentType" name="parentType" data-cy="parentType">
                <Select options={types}></Select>
              </Form.Item>

              <Form.Item label={translate('tagApp.tagTag.textColor')} id="tag-textColor" name="textColor" data-cy="textColor">
                <Colorpicker picker={'CompactPicker'} />
              </Form.Item>

              <Form.Item label={translate('tagApp.tagTag.fillColor')} id="tag-fillColor" name="fillColor" data-cy="fillColor">
                <Colorpicker picker={'CompactPicker'} />
              </Form.Item>

              <Form.Item label={translate('tagApp.tagTag.borderColor')} id="tag-borderColor" name="borderColor" data-cy="borderColor">
                <Colorpicker picker={'CompactPicker'} />
              </Form.Item>

              <Form.Item label={translate('tagApp.tagTag.icon')} id="tag-icon" name="icon" data-cy="icon">
                <AutoComplete
                  style={{ width: 200 }}
                  options={iconOptions}
                  placeholder="Select an icon"
                  filterOption={(inputValue, option) => option!.value.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
                />
              </Form.Item>

              <Button type="link" id="cancel-save" data-cy="entityCreateCancelButton" href="/tag/tag" color="info">
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

export default TagUpdate;
