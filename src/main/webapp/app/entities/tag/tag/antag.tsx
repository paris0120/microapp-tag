import { Space, Tag } from 'antd';
import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

export const AnTag = prop => {
  return (
    <Tag icon={<FontAwesomeIcon icon={prop.tag.icon} />} color={prop.tag.fillColor}>
      {prop.link ? <Link to={prop.link}>{prop.tag.tag}</Link> : prop.tag.tag}
    </Tag>
  );
};
