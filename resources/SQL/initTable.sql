INSERT INTO `sys_permission`(`permissionId`, `available`, `deleted`, `permission_name`, `description`, `permission_type`, `url`, `group_name`) VALUES 
(1, b'1', b'0', 'user:list', '分页查询用户列表', 'menu', 'user/list', '用户'),
(2, b'1', b'0', 'user:add', '用户添加', 'button', 'user/add', '用户'),
(3, b'1', b'0', 'user:delete', '用户删除', 'button', 'user/delete', '用户'),
(4, b'1', b'0', 'user:update', '用户更新', 'button', 'user/update', '用户'),
(5, b'1', b'0', 'user:info', '查询用户角色和权限', 'button', 'user/info', '用户'),
(6, b'1', b'0', 'user:suggestion', '反馈建议', 'button', 'user/suggestion', '用户'),
(7, b'1', b'0', 'user:help', '帮助文档', 'button', 'user/help', '用户'),
(8, b'1', b'0', 'user:forceResetPassword', '强制重置密码', 'button', 'user/forceResetPassword', '用户'),
(9, b'1', b'0', 'user:resetPassword', '修改密码', 'button', 'user/resetPassword', '用户'),
(10, b'1', b'0', 'role:list', '查询角色列表', 'menu', 'role/list', '角色'),
(11, b'1', b'0', 'role:listpage', '分页查询角色列表', 'menu', 'role/listpage', '角色'),
(12, b'1', b'0', 'role:listRolesByUserId', '根据用户ID查询用户拥有的角色列表', 'menu', 'role/listRolesByUserId', '角色'),
(13, b'1', b'0', 'role:listGroupRolesByUserId', '根据用户ID查询用户拥有的部门角色列表', 'menu', 'role/listGroupRolesByUserId', '角色'),
(14, b'1', b'0', 'role:listByRoleGroupId', '根据角色组(部门)ID查询角色组(部门)拥有的角色列表', 'menu', 'role/listByRoleGroupId', '角色'),
(15, b'1', b'0', 'role:add', '添加角色', 'button', 'role/add', '角色'),
(16, b'1', b'0', 'role:update', '更新角色', 'button', 'role/update', '角色'),
(17, b'1', b'0', 'role:delete', '删除角色', 'button', 'role/delete', '角色'),
(18, b'1', b'0', 'permission:list', '查询权限列表', 'menu', 'permission/list', '权限'),
(19, b'1', b'0', 'permission:listpage', '分页查询权限列表', 'menu', 'permission/listpage', '权限'),
(20, b'1', b'0', 'permission:listByRoleId', '根据角色ID查询角色拥有的权限列表', 'menu', 'permission/listByRoleId', '权限'),
(21, b'1', b'0', 'permission:add', '添加权限', 'button', 'permission/add', '权限'),
(22, b'1', b'0', 'permission:update', '更新权限', 'button', 'permission/update', '权限'),
(23, b'1', b'0', 'permission:delete', '删除权限', 'button', 'permission/delete', '权限'),
(24, b'1', b'0', 'permission:groups', '查询权限划分组', 'button', 'permission/groups', '权限'),
(25, b'1', b'0', 'userRole:grantRole2User', '为用户赋角色', 'button', 'userRole/grantRole2User', '角色分配'),
(26, b'1', b'0', 'userRole:clearUserRoles', '根据用户ID清空用户角色', 'button', 'userRole/clearUserRoles', '角色分配'),
(27, b'1', b'0', 'userRole:clearUserGroupRoles', '根据用户ID清空用户已分配的部门角色', 'button', 'userRole/clearUserGroupRoles', '角色分配'),
(28, b'1', b'0', 'rolePermission:grantPermission2Role', '为角色赋权限', 'button', 'rolePermission/grantPermission2Role', '权限分配'),
(29, b'1', b'0', 'rolePermission:clearRolePermissions', '根据角色ID清空权限', 'button', 'rolePermission/clearRolePermissions', '权限分配'),
(30, b'1', b'0', 'roleGroup:list', '查询部门列表', 'menu', 'roleGroup/list', '部门'),
(31, b'1', b'0', 'roleGroup:listpage', '分页查询部门列表', 'menu', 'roleGroup/listpage', '部门'),
(32, b'1', b'0', 'roleGroup:add', '添加部门', 'menu', 'roleGroup/add', '部门'),
(33, b'1', b'0', 'roleGroup:update', '更新部门', 'menu', 'roleGroup/update', '部门'),
(34, b'1', b'0', 'roleGroup:delete', '删除部门', 'menu', 'roleGroup/delete', '部门'),
(35, b'1', b'0', 'datamart:listVariables', '风机变量信息列表', 'button', 'datamart/listVariables', '数据抽取'),
(36, b'1', b'0', 'datamart:saveDataSet', '创建数据集', 'button', 'datamart/saveDataSet', '数据抽取'),
(37, b'1', b'0', 'datamart:list', '个人数据集列表', 'menu', 'datamart/list', '数据抽取'),
(38, b'1', b'0', 'datamart:rename', '数据集重命名', 'button', 'datamart/rename', '数据抽取'),
(39, b'1', b'0', 'datamart:delete', '删除数据集', 'button', 'datamart/delete', '数据抽取'),
(40, b'1', b'0', 'datamart:updateStatus', '数据集状态更新', 'button', 'datamart/updateStatus', '数据抽取'),
(41, b'1', b'0', 'localupload:list', '文件列表', 'menu', 'localupload/list', '文件上传'),
(42, b'1', b'0', 'localupload:uploadFile', '上传文件', 'button', 'localupload/uploadFile', '文件上传'),
(43, b'1', b'0', 'localupload:rename', '文件重命名', 'button', 'localupload/rename', '文件上传'),
(44, b'1', b'0', 'localupload:delete', '文件删除', 'button', 'localupload/delete', '文件上传'),
(45, b'1', b'0', 'data:get:wf_owner', '业主信息查询', 'button', '/data/windfarms/owners', '项目数据'),
(46, b'1', b'0', 'data:get:wf', '风场信息查询', 'button', '/data/windfarms', '项目数据'),
(47, b'1', b'0', 'data:get:wt_type', '机型信息查询', 'button', '/data/windfarms/alltype', '项目数据'),
(48, b'1', b'0', 'data:get:wf_info', '获取所有风场信息', 'button', '/data/windfarms/all', '项目数据'),
(49, b'1', b'0', 'data:get:wf_summary', '按省份统计风场信息汇总', 'button', '/data/windfarms/summary', '项目数据'),
(50, b'1', b'0', 'data:get:wt_by_wfid', '获取指定风场的所有风机信息', 'button', '/data/windturbins', '项目数据'),
(51, b'1', b'0', 'data:get:integrity', '指定风场风机数据完整度计算', 'button', '/data/windturbins', '项目数据'),
(52, b'1', b'0', 'data:startworkflow', '启动AWS_GlueWorkflow', 'button', '/data/startworkflow', '数据集'),
(53, b'1', b'0', 'data:stopworkflow', '停止AWS_GlueWorkflow	', 'button', '/data/startworkflow', '数据集'),
(54, b'1', b'0', 'data:get:workflowstatus', '查询工作流状态', 'button', '/data/startworkflow', '数据集'),
(55, b'1', b'0', 'data:get:athena', 'Athena数据集预览', 'button', '/data/athena', '数据集'),
(56, b'1', b'0', 'data:delete:workflow', '删除工作流', 'button', '/data/workflow', '数据集'),
(57, b'1', b'0', 'data:zeppelin:login', '跳转登录zeppelin', 'button', '/data/workflow', 'Zeppelin登录'),
(58, b'1', b'0', 'data:zeppelin:create', '创建zeppelin实例', 'button', '/data/zeppelin/create', 'Zeppelin管理'),
(59, b'1', b'0', 'data:zeppelin:delete', '删除zeppelin实例', 'button', '/data/zeppelin/delete', 'Zeppelin管理'),
(60, b'1', b'0', 'data:zeppelin:images', '查询zeppelin镜像', 'button', '/data/zeppelin/delete', 'Zeppelin管理'),
(61, b'1', b'0', 'data:zeppelin:status', '查询zeppelin ECS状态', 'button', '/data/zeppelin/status', 'Zeppelin管理'),
(62, b'1', b'0', 'data:zeppelin:flavor', '查询zeppelin镜像规格', 'button', '/data/zeppelin/status', 'Zeppelin管理'),
(63, b'1', b'0', 'data:zeppelin:info', '查询zeppelin配置信息', 'button', '/data/zeppelin/status', 'Zeppelin管理'),
(64, b'1', b'0', 'data:dataProjectMenu', '用户操作数据项目，操作实例的权限', 'menu', '界面'),
(65, b'1', b'0', 'permission:userMenu', '权限系统-用户菜单', 'menu','界面'),
(66, b'1', b'0', 'permission:userManagementScan', '权限系统-查看用户权限', 'menu', '界面'),
(67, b'1', b'0', 'permission:userManagementOperate', '权限系统-操作用户权限', 'button', '界面'),
(68, b'1', b'0', 'permission:rolesManagementScan', '权限系统-查看角色权限', 'menu', '界面'),
(69, b'1', b'0', 'permission:rolesManagementOperate', '权限系统-操作角色权限', 'button', '界面'),
(70, b'1', b'0', 'permission:permissionManagementScan', '权限系统-查看权限点权限', 'menu', '界面'),
(71, b'1', b'0', 'permission:permissionManagementOperate', '权限系统-操作权限点', 'button', '界面'),
(72, b'1', b'0', 'field:delete', '删除【主变量】', 'button', '/field/delete', '主变量维护'),
(73, b'1', b'0', 'field:mainFieldGroups', '查询【主变量】分组', 'button', '/field/mainFieldGroups', '主变量维护'),
(74, b'1', b'0', 'field:update', '更新【主变量】', 'button', '/field:update', '主变量维护'),
(75, b'1', b'0', 'field:pageListMainField', '根据条件分页查询【主变量】列表', 'menu', '/field/pageListMainField', '主变量维护'),
(76, b'1', b'0', 'field:pagelistIecpath', '根据条件分页查询可分配的【源变量】列表', 'menu', '/field/pagelistIecpath', '主变量维护'),
(77, b'1', b'0', 'field:linkIecpathWithMainField', '关联【源变量】与【主变量】', 'button', '/field/linkIecpathWithMainField', '主变量维护'),
(78, b'1', b'0', 'variableManagement:variableManagementOperate', '变量管理-变量管理操作权限', 'menu', NULL, '界面'),
(79, b'1', b'0', 'collection:createCollection', '收藏变量，创建变量收藏夹', 'button', '/collection/createCollection', '变量收藏夹维护'),
(80, b'1', b'0', 'collection:list', '变量收藏夹列表', 'menu', '/collection/list', '变量收藏夹维护'),
(81, b'1', b'0', 'collection:remove', '变量收藏夹删除', 'button', '/collection/remove', '变量收藏夹维护');



INSERT INTO `sys_role`(`roleId`, `available`, `description`, `role_name`, `deleted`, `roleGroupId`) VALUES 
(1, b'1', '系统管理员', 'ADMIN', b'0', 1),
(2, b'1', '用户审核员', 'END_USER_AUDITOR', b'0', 1),
(3, b'1', '普通用户', 'END_USER', b'0', null);


INSERT INTO `sys_role_group`(`id`, `available`, `deleted`, `role_group_name`, `description`) VALUES 
(1, b'1', b'0', '默认部门', '普通用户角色在这个部门'),
(2, b'0', b'1', '默认部门2', '系统管理员在这个部门'),
(3, b'0', b'0', '默认部门3', '用户审核员在这个部门');

INSERT INTO `sys_user`(`userId`, `user_name`, `passwd`, `available`, `deleted`, `goldwinder`, `nick_name`, `email`, `create_time`, `last_login_time`, `update_time`, `expired_time`, `salt`, `department_code`) VALUES 
(1, '29514', '', b'1', b'0', b'1', '朱学婷', 'zhuxueting@goldwind.com.cn', '2019-06-28 18:00:31', '2020-07-27 17:44:43', '2020-07-29 10:32:36', '2019-06-29', '4qYNe41L3ur4e2WK4rEQ', '40005044'),
(5, '31240', '', b'1', b'0', b'1', '王亚雷', 'wangyalei@goldwind.com.cn', '2019-06-28 18:00:31', '2020-07-08 11:31:31', '2020-07-29 10:32:37', '2019-06-29', '7b3Hz9YNx4Sb1R2N5426', '40005044'),
(6, '31892', '', b'1', b'0', b'1', '袁振', 'yuanzhen31892@goldwind.com.cn', '2019-06-28 18:00:31', '2019-06-28 18:00:31', '2020-07-29 10:32:37', '2019-06-29', '17fHyT8Kl118iny1296w', '40005044'),
(7, '51527', '', b'1', b'0', b'1', '张宇辰', 'zhangyuchen@goldwind.com.cn', '2019-06-28 18:00:31', '2020-07-29 08:51:49', '2020-07-29 10:21:29', '2019-06-29', 'WE7f7YT9618911W8x799', '40005044'),
(9, '34453', '', b'1', b'0', b'1', '王贵宇', 'wangguiyu@goldwind.com.cn', NULL, '2020-07-27 14:58:09', '2020-07-29 10:42:34', NULL, NULL, '40005044'),
(10, '32972', '', b'1', b'0', b'1', '柳君', 'liujun32972@goldwind.com.cn', '2020-06-28 02:51:19', '2020-07-14 15:17:19', '2020-07-29 10:21:27', '2020-07-03', '8d78869f470951332959580424d4bf4f', '40005044'),
(11, '31258', '', b'1', b'0', b'1', '辛帅伟', 'xinshuaiwei@goldwind.com.cn', '2020-06-29 11:13:13', '2020-07-28 09:40:00', '2020-07-29 10:21:26', '2021-01-01', '8d78869f470951332959580424d4bf4f', '40005044'),
(13, '50343', '', b'1', b'0', b'1', '秦德阳', 'qindeyang@goldwind.com.cn', '2020-06-29 17:08:00', '2020-07-20 17:00:15', '2020-07-29 10:34:51', NULL, NULL, '40005044'),
(22, '24753', '', b'1', b'0', b'1', '刘源', 'liuyuan@goldwind.com.cn', '2020-07-06 03:34:11', '2020-07-15 09:25:11', '2020-07-29 10:21:25', '2020-12-16', NULL, '40005044'),
(24, '33171', '', b'1', b'0', b'1', '宋明彦', 'songmingyan@goldwind.com.cn', '2020-07-08 10:43:27', '2020-07-28 09:29:34', '2020-07-29 10:21:27', '2020-07-18', NULL, '01009119'),
(25, '34601', '', b'1', b'0', b'1', '樊龙龙', 'fanlonglong@goldwind.com.cn', '2020-07-08 10:49:01', '2020-07-08 11:31:56', '2020-07-29 10:21:28', '2020-07-18', NULL, '40005044'),
(26, '5351', '', b'1', b'0', b'1', '周杰', 'zhoujie2@goldwind.com.cn', '2020-07-08 11:11:40', NULL, '2020-07-29 10:21:29', '2020-07-18', NULL, '01009119'),
(27, '28109', '', b'1', b'0', b'1', '陈飞', 'chenfei28109@goldwind.com.cn', '2020-07-08 14:48:23', '2020-07-21 09:59:13', '2020-07-29 10:21:26', '2020-07-18', NULL, '40004742'),
(28, '28608', '', b'1', b'0', b'1', '闻笔荣', 'wenbirong@goldwind.com.cn', '2020-07-08 14:49:11', '2020-07-08 14:55:33', '2020-07-29 10:21:26', '2020-07-18', NULL, '10017615'),
(29, '50284', '', b'1', b'0', b'1', '陈克朋', 'chenkepeng50284@goldwind.com.cn', '2020-07-08 15:35:53', NULL, '2020-07-29 10:21:29', '2020-07-18', NULL, '40004742'),
(30, '32163', '', b'1', b'0', b'1', '侠惠芳', 'xiahuifang@goldwind.com.cn', '2020-07-09 14:43:53', '2020-07-09 14:45:08', '2020-07-29 10:21:27', '2020-07-19', NULL, '10015596'),
(31, '51050', '', b'1', b'0', b'1', '闫祥祥', 'yanxiangxiang@goldwind.com.cn', '2020-07-13 08:32:07', '2020-07-13 09:03:57', '2020-07-29 10:21:29', '2020-07-23', NULL, '40005044'),
(32, '51005', '', b'1', b'0', b'1', '高伟', 'gaowei51005@goldwind.com.cn', '2020-07-13 09:43:55', '2020-07-13 10:00:15', '2020-07-29 10:21:29', '2020-07-23', NULL, '40005044'),
(33, '33552', '', b'1', b'0', b'1', '刘磊', 'liulei@tianrun.cn', '2020-07-13 10:33:49', '2020-07-13 10:35:41', '2020-07-29 10:34:30', '2020-07-23', 'o20CRg13gYPN7lz6Bh8D', '40003320'),
(34, '3776', '', b'1', b'0', b'1', '明哲', 'mingzhe@goldwind.com.cn', '2020-07-13 10:34:37', '2020-07-13 10:36:13', '2020-07-29 10:21:28', '2020-07-23', NULL, '01005090'),
(35, '2588', '', b'1', b'0', b'1', '张新丽', 'zhangxinli@goldwind.com.cn', '2020-07-13 14:58:17', '2020-07-28 13:34:55', '2020-07-29 10:21:25', '2020-07-23', NULL, '01009119'),
(36, '29228', '', b'1', b'0', b'1', '范爱萍', 'fanaiping@goldwind.com.cn', '2020-07-13 17:23:58', '2020-07-14 15:00:04', '2020-07-29 10:21:26', '2020-07-23', NULL, '40004248'),
(37, '1723', '', b'1', b'0', b'1', '刘健', 'liujian@goldwind.com.cn', '2020-07-14 10:26:53', '2020-07-14 10:40:16', '2020-07-29 10:21:24', '2020-07-24', NULL, '10058420'),
(38, '3618', '', b'1', b'0', b'1', '耿玉龙', 'gengyulong@goldwind.com.cn', '2020-07-14 10:59:03', '2020-07-14 11:12:46', '2020-07-29 10:21:28', '2020-07-24', NULL, '01005073'),
(40, '33612', '', b'1', b'0', b'1', '徐蓉', 'xurong@goldwind.com.cn', '2020-07-14 11:12:09', NULL, '2020-07-29 10:21:27', '2020-07-24', NULL, '01005073'),
(42, '29350', '', b'1', b'0', b'1', '王月娟', 'wangyuejuan29350@goldwind.com.cn', '2020-07-14 11:16:37', NULL, '2020-07-29 10:21:26', '2020-07-24', NULL, '01005073'),
(43, '4800', '', b'1', b'0', b'1', '杨玉庆', 'yangyuqing@goldwind.com.cn', '2020-07-14 11:36:12', NULL, '2020-07-29 10:21:28', '2020-07-24', NULL, '01005073'),
(44, '26161', '', b'1', b'0', b'1', '黄超群', 'huangchaoqun@goldwind.com.cn', '2020-07-14 16:53:43', '2020-07-21 14:28:00', '2020-07-29 10:21:25', '2020-07-24', NULL, '10058420'),
(45, '33888', '', b'1', b'0', b'1', '胡烨', 'huye@goldwind.com.cn', '2020-07-15 14:35:31', '2020-07-28 15:30:25', '2020-07-29 10:21:27', '2020-07-25', NULL, '01009119'),
(51, '25907', '', b'1', b'0', b'1', '王宏亮', 'wanghongliang@goldwind.com.cn', '2020-07-20 11:04:34', '2020-07-26 10:32:14', '2020-07-29 10:21:25', '2020-07-30', NULL, '10057750'),
(52, '34352', '', b'1', b'0', b'1', '樊彦斌', 'fanyanbin@goldwind.com.cn', '2020-07-20 11:07:54', NULL, '2020-07-29 10:21:28', '2020-07-30', NULL, '10082776'),
(54, '31387', '', b'1', b'0', b'1', '董兆龙', 'dongzhaolong@goldwind.com.cn', '2020-07-21 11:19:21', '2020-07-27 09:16:32', '2020-07-29 10:21:26', '2020-07-31', NULL, '10082772'),
(55, '2256', '', b'1', b'0', b'1', '赵祥', 'zhaoxiang@goldwind.com.cn', '2020-07-21 11:22:59', '2020-07-27 09:36:19', '2020-07-29 10:21:24', '2020-07-31', NULL, ''),
(56, '2225', '', b'1', b'0', b'1', '王晓东', 'wangxiaodong@goldwind.com.cn', '2020-07-21 11:34:48', '2020-07-21 13:43:01', '2020-07-29 10:21:24', '2020-07-31', NULL, '10082775'),
(57, '25718', '', b'1', b'0', b'1', '周凯', 'zhoukai@goldwind.com.cn', '2020-07-21 13:39:28', '2020-07-22 09:28:49', '2020-07-29 10:21:25', '2020-07-31', NULL, '40004458'),
(58, '26531', '', b'1', b'0', b'1', '国旭', 'guoxu@goldwind.com.cn', '2020-07-23 13:40:16', NULL, '2020-07-29 10:32:36', '2020-08-02', '38vU4b727BPOsEt3HYse', '10057750'),
(59, '36871', '', b'1', b'0', b'1', '刘圣义', 'liushengyi@goldwind.com.cn', '2020-07-24 10:28:11', '2020-07-24 10:30:03', '2020-07-29 10:21:28', '2020-08-03', NULL, '40005044'),
(75, '50508', '', b'1', b'0', b'1', '孙永刚', 'sunyonggang@goldwind.com.cn', '2020-07-27 10:39:51', '2020-07-29 08:59:13', '2020-07-29 10:21:29', '2020-08-06', NULL, '40005044'),
(77, '26386', '', b'1', b'0', b'1', '刘晶晶', 'liujingjing@goldwind.com.cn', '2020-07-28 13:53:02', NULL, '2020-07-29 10:21:25', '2020-08-07', '7829aE8232bF849Q22wD', '10058420'),
(79, '31407', '', b'0', b'0', b'1', '马羽龙', 'mayulong@goldwind.com.cn', '2020-07-28 15:20:36', NULL, '2020-07-29 10:21:27', '2020-08-07', 'K55hF4Ea28z50UjA6780', '01009119'),
(80, '51093', '', b'0', b'0', b'1', '刘华', 'liuhua51093@goldwind.com.cn', '2020-07-28 15:21:25', NULL, '2020-07-29 10:22:49', '2020-08-07', NULL, '40005044'),
(81, '32968', '', b'0', b'0', b'1', '武铁成', 'wutiecheng@goldwind.com.cn', '2020-07-28 17:32:55', NULL, '2020-07-29 10:21:27', '2020-08-07', 'BH042QOkJh06plx10599', '01009119');


INSERT INTO `sys_user_role`(`userId`, `roleId`) VALUES 
(1, 2),
(2, 1),
(1, 3),
(2, 3),
(3, 3),
(4, 3),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 3),
(12, 3),
(13, 3),
(14, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 3),
(21, 3),
(22, 3),
(23, 3),
(24, 3),
(25, 3),
(26, 3),
(27, 3),
(28, 3),
(29, 3),
(30, 3);

INSERT INTO `sys_role_permission`(`permissionId`, `roleId`) VALUES 
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 1),
(12, 1),
(13, 1),
(14, 1),
(15, 1),
(16, 1),
(17, 1),
(18, 1),
(19, 1),
(20, 1),
(21, 1),
(22, 1),
(23, 1),
(24, 1),
(25, 1),
(26, 1),
(27, 1),
(28, 1),
(29, 1),
(30, 1),
(31, 1),
(32, 1),
(33, 1),
(34, 1),
(35, 1),
(36, 1),
(37, 1),
(38, 1),
(39, 1),
(40, 1),
(41, 1),
(42, 1),
(43, 1),
(44, 1),
(45, 1),
(46, 1),
(47, 1),
(48, 1),
(49, 1),
(50, 1),
(51, 1),
(52, 1),
(53, 1),
(54, 1),
(55, 1),
(56, 1),
(57, 1),
(58, 1),
(59, 1),
(60, 1),
(61, 1),
(62, 1),
(63, 1),
(64, 1),
(65, 1),
(66, 1),
(67, 1),
(68, 1),
(69, 1),
(70, 1),
(71, 1),
(72, 1),
(73, 1),
(74, 1),
(75, 1),
(76, 1),
(77, 1),
(79, 1),
(80, 1),
(81, 1),
(1, 2),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(5, 3),
(6, 3),
(7, 3),
(9, 3),
(35, 3),
(36, 3),
(37, 3),
(38, 3),
(39, 3),
(40, 3),
(41, 3),
(42, 3),
(43, 3),
(44, 3),
(45, 3),
(46, 3),
(47, 3),
(48, 3),
(49, 3),
(50, 3),
(51, 3),
(52, 3),
(53, 3),
(54, 3),
(55, 3),
(56, 3),
(57, 3),
(58, 3),
(59, 3),
(60, 3),
(61, 3),
(62, 3),
(63, 3),
(79, 3),
(80, 3),
(81, 3);

INSERT INTO `sys_token`(`userId`, `expire_time`, `token`, `update_time`) VALUES 
(0, NULL, NULL, NULL),
(1, '2020-07-28 14:39:18', 'bdf19e6a16cc532198699cf00f545267', '2020-07-16 14:39:18'),
(2, '2020-07-29 10:48:40', '307126e282a0ac4d7aa14a12e00edb31', '2020-07-17 10:48:40'),
(4, '2020-07-29 09:09:11', '67506ad4ca15d3c61ac2be6d514ca863', '2020-07-17 09:09:11'),
(5, '2020-07-20 11:31:31', '5212b9dc8ef01e0e3edbf341bbc3de64', '2020-07-08 11:31:31'),
(6, '2020-06-16 01:39:28', '738276596119813daf11d55117f67d33', '2020-06-04 01:39:28'),
(7, '2020-07-28 10:48:36', '247cece92cf6aef0a27dc8ad1bf5b601', '2020-07-16 10:48:36'),
(9, '2020-07-20 11:30:58', '60ced9920b725643d794df3fe8a43ed0', '2020-07-08 11:30:58'),
(10, '2020-07-26 15:17:19', '77c35b1b28709ea6b1ff1d70654d72b5', '2020-07-14 15:17:19'),
(11, '2020-07-26 13:33:04', '99b21a6799d58ec1d8fb96029341e34d', '2020-07-14 13:33:04'),
(12, '2020-07-19 16:58:46', '965003bdcf7eba426c3d7460de7acebc', '2020-07-07 16:58:46'),
(13, '2020-07-28 15:02:18', '2d3a677272fbf263fbb751012fb38209', '2020-07-16 15:02:18'),
(22, '2020-07-27 09:25:11', '1a801c568edfebd9739607943d79df97', '2020-07-15 09:25:11'),
(24, '2020-07-28 08:21:43', '73811886f7b1b857ad1b89de3971f9cb', '2020-07-16 08:21:43'),
(25, '2020-07-20 11:31:56', '9a6ca7cafc60b7333d4966c7140c571a', '2020-07-08 11:31:56'),
(27, '2020-07-27 13:40:15', 'b1737880f78692886d7601ba5c56c1dc', '2020-07-15 13:40:15'),
(28, '2020-07-20 14:55:33', '2825766756887c49687f71ab50b8385b', '2020-07-08 14:55:33'),
(30, '2020-07-21 14:45:08', '9272ed0b8aa4482461255185fedf881d', '2020-07-09 14:45:08'),
(31, '2020-07-25 09:03:57', 'e79607a52ffbdc09372e25738012a9b6', '2020-07-13 09:03:57'),
(32, '2020-07-25 10:00:15', 'd8665dd690f752cae951326f845ddc29', '2020-07-13 10:00:15'),
(33, '2020-07-25 10:35:41', '8c360d99bae5d064c93b0abb7a417875', '2020-07-13 10:35:41'),
(34, '2020-07-25 10:36:12', '8b42f4336fa20b25337ce002d64cac39', '2020-07-13 10:36:12'),
(35, '2020-07-25 15:00:46', '15af69570836712113ef0b0fe28530d3', '2020-07-13 15:00:46'),
(36, '2020-07-26 15:00:04', 'b894f1cee7f610065bc422cfc8eba0bb', '2020-07-14 15:00:04'),
(37, '2020-07-26 10:40:16', '90b7e1e4f2bcf933a2c00a8d413dd79c', '2020-07-14 10:40:16'),
(38, '2020-07-26 11:12:46', 'a47a6200c90a72e6809e95360a13a2d7', '2020-07-14 11:12:46');
