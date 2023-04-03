import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITag } from 'app/shared/model/tag/tag.model';
import { getEntities } from './tag.reducer';
import { AnTag } from 'app/entities/tag/tag/antag';
import { Button } from 'antd';

export const Tag = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const tagList = useAppSelector(state => state.tag.tag.entities);
  const loading = useAppSelector(state => state.tag.tag.loading);
  const totalItems = useAppSelector(state => state.tag.tag.totalItems);

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
      <h2 id="tag-heading" data-cy="TagHeading">
        <Translate contentKey="tagApp.tagTag.home.title">Tags</Translate>
        <div className="d-flex justify-content-end">
          <Button type="link" id="create" data-cy="entityCreateButton" href="/admin/tag/tag/new" color="info">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="tagApp.tagTag.home.createLabel">Create new Tag</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {tagList && tagList.length > 0 ? (
          <>
            {tagList.map((tag, i) => (
              <AnTag link={`/admin/tag/tag/${tag.id}/edit`} tag={tag}></AnTag>
            ))}
          </>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tagApp.tagTag.home.notFound">No Tags found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Tag;
