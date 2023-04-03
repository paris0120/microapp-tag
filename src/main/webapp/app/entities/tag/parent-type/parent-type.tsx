import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IParentType } from 'app/shared/model/tag/parent-type.model';
import { getEntities } from './parent-type.reducer';
import { Button, List, Space } from 'antd';

export const ParentType = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const parentTypeList = useAppSelector(state => state.tag.parentType.entities);
  const loading = useAppSelector(state => state.tag.parentType.loading);
  const totalItems = useAppSelector(state => state.tag.parentType.totalItems);

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
      <h2 id="parent-type-heading" data-cy="ParentTypeHeading">
        <Translate contentKey="tagApp.tagParentType.home.title">Parent Types</Translate>
        <div className="d-flex justify-content-end">
          <Button type="link" id="create" data-cy="entityCreateButton" href="/admin/tag/parent-type/new" color="info">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="tagApp.tagParentType.home.createLabel">Create new Parent Type</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {parentTypeList && parentTypeList.length > 0 ? (
          <List
            itemLayout="horizontal"
            dataSource={parentTypeList}
            renderItem={(item: IParentType) => (
              <List.Item>
                <List.Item.Meta
                  title={
                    <Space>
                      <a href={`/admin/tag/parent-type/${item.id}/edit`}>
                        {item.parentType} {item.parentId < 0 ? '' : `(${item.parentId})`}: {item.server}{' '}
                      </a>

                      {item.isEncrypted ? <FontAwesomeIcon icon="lock" title="Encrypted" /> : ''}
                      {item.userManageable ? <FontAwesomeIcon icon="user-pen" title="User Managerable" /> : ''}
                    </Space>
                  }
                  description={
                    <div>
                      <span style={{ float: 'left' }}>{'#' + item.topic}</span>
                    </div>
                  }
                />
              </List.Item>
            )}
          />
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tagApp.tagParentType.home.notFound">No Parent Types found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={parentTypeList && parentTypeList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default ParentType;
