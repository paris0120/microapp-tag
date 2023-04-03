import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { byteSize, Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IServer } from 'app/shared/model/tag/server.model';
import { getEntities } from './server.reducer';
import { Button, Col, List, Row, Space, Typography } from 'antd';

export const Server = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { Text, Link } = Typography;

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const serverList = useAppSelector(state => state.tag.server.entities);
  const loading = useAppSelector(state => state.tag.server.loading);
  const totalItems = useAppSelector(state => state.tag.server.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="server-heading" data-cy="ServerHeading">
        <Translate contentKey="tagApp.tagServer.home.title">Servers</Translate>
        <div className="d-flex justify-content-end">
          <Button type="link" id="create" data-cy="entityCreateButton" href="/admin/tag/server/new" color="info">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="tagApp.tagServer.home.createLabel">Create new Server</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {serverList && serverList.length > 0 ? (
          <>
            <Row>
              <Col span={24}>
                <List
                  itemLayout="horizontal"
                  dataSource={serverList}
                  renderItem={(item: IServer) => (
                    <List.Item>
                      <List.Item.Meta
                        title={
                          <div>
                            <a style={{ float: 'left' }} href={'/admin/tag/server/' + item.id + '/edit'}>
                              {item.server}
                            </a>
                            {item.decoder ? <Text style={{ float: 'right' }}>{item.decoder}</Text> : ''}
                          </div>
                        }
                      />
                    </List.Item>
                  )}
                />
              </Col>
            </Row>
          </>
        ) : (
          ''
        )}
      </div>
    </div>
  );
};

export default Server;
