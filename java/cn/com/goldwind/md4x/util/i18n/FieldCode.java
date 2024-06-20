package cn.com.goldwind.md4x.util.i18n;

public class FieldCode {
	// 查询源变量列表成功！
	public static final String IECPATH_LIST_SUCCESS = "iecpath_list_success";

	// 查询源变量列表失败！
	public static final String IECPATH_LIST_FAILED = "iecpath_list_failed";

	// 查询主变量列表成功！
	public static final String MAINFIELD_LIST_SUCCESS = "mainfield_list_success";

	// 查询主变量列表失败！
	public static final String MAINFIELD_LIST_FAILED = "mainfield_list_failed";

	// 主变量更新成功！
	public static final String MAINFIELD_UPDATE_SUCCESS = "mainfield_update_success";

	// 主变量更新失败！
	public static final String MAINFIELD_UPDATE_FAILED = "mainfield_update_failed";

	// 查询主变量分组信息成功！
	public static final String MAINFIELD_GROUPS_LIST_SUCCESS = "mainfield_groups_list_success";

	// 查询主变量分组信息失败！
	public static final String MAINFIELD_GROUPS_LIST_FAILED = "mainfield_groups_list_failed";

	// 主变量删除成功！
	public static final String MAINFIELD_DELETE_SUCCESS = "mainfield_delete_success";

	// 主变量删除失败！
	public static final String MAINFIELD_DELETE_FAILED = "mainfield_delete_failed";

	// 关联源变量与主变量失败:主变量ID不能为空！
	public static final String MAINFIELD_ID_BLANK = "mainfield_id_blank";

	// 关联源变量与主变量失败:API参数解析失败！
	public static final String API_PARAM_ERROR_ON_MAINFIELD_IECPATH_LINK = "api_param_error_on_mainfield_iecpath_link";

	// 关联源变量与主变量失败：主变量必须为Master！
	public static final String MAINFIELD_MUST_BE_MASTER = "mainfield_must_be_master";

	// 根据要解除关联关系的源变量创建主变量失败！
	public static final String MAINFIELD_CREATE_FAILED_ON_THE_BREAKER = "mainfield_create_failed_on_the_breaker";

	// 解除【源变量】
	public static final String PART_UNLINK_IECPATH = "part_unlink_iecpath";
	// 与【主变量】
	public static final String PART_WITH_MAINFIELD = "part_with_mainfield";
	// 的关联关系，并恢复其旧关联关系失败！
	public static final String PART_RELEATIONSHIP_TO_RESTORE = "part_releationship_to_restore";

	// 关联源变量与主变量失败：源变量：
	public static final String PART_LINK_FAILED_AS_IECPATH = "part_link_failed_as_iecpath";
	// 关联主变量：
	public static final String PART_LINK_FAILED_AS_LINK_MAINFIELD = "part_link_failed_as_link_mainfield";
	// 失败！
	public static final String PART_LINK_FAILED_AS_LINK_MAINFIELD_FAILED = "part_link_failed_as_link_mainfield_failed";

	// 源变量与主变量关联成功！
	public static final String LINK_IECPAHT_WITH_MAINFIELD_SUCCESS = "link_iecpaht_with_mainfield_success";

	// 文件解析失败：iecpath CSV文件不存在！
	public static final String CSV_NONE_EXIST = "csv_none_exist";

	// 文件解析失败：文件不是CSV格式！
	public static final String FILE_IS_NOT_CSV = "file_is_not_csv";

	// 文件解析失败：文件编码格式为UTF-8-BOM格式，请将其转换为UTF-8无BOM格式！
	public static final String CSV_MUST_BE_UTF8_WITHOUT_BOM = "csv_must_be_utf8_without_bom";

	// 文件解析成功：
	public static final String FILE_PARSE_SUCCESS_AS = "file_parse_success_as";
	// 入库成功，
	public static final String FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_SUCCESS = "file_parse_success_as_save_into_db_success";
	// 入库失败！
	public static final String FILE_PARSE_SUCCESS_AS_SAVE_INTO_DB_FAILED = "file_parse_success_as_save_into_db_failed";

}
