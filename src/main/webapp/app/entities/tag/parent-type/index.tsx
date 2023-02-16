import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ParentType from './parent-type';
import ParentTypeDetail from './parent-type-detail';
import ParentTypeUpdate from './parent-type-update';
import ParentTypeDeleteDialog from './parent-type-delete-dialog';

const ParentTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ParentType />} />
    <Route path="new" element={<ParentTypeUpdate />} />
    <Route path=":id">
      <Route index element={<ParentTypeDetail />} />
      <Route path="edit" element={<ParentTypeUpdate />} />
      <Route path="delete" element={<ParentTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ParentTypeRoutes;
