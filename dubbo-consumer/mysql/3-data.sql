insert into permission(id, parent_id, name, name_zh) values(1, 0, '*:*', '管理员权限');
insert into role(id, name, name_en, name_zh, permission_ids) values(1, 'A', 'admin', '管理员', '1');
insert into user(id, user_name, password, salt, status, role_ids) values(1, 'admin', '259907396c78433babf37375469b88e2', '42v1580n5stfftu9nptrlot1lbadyzaj', 'A', '1');