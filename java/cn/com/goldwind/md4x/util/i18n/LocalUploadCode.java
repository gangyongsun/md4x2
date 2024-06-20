package cn.com.goldwind.md4x.util.i18n;

public class LocalUploadCode {
	// 文件上传失败：文件不存在！
	public static final String FILE_UPLOAD_FAILED_ON_FILE_NONE_EXIST = "file_upload_failed_on_file_none_exist";
	
	// 文件上传成功！
	public static final String FILE_UPLOAD_SUCCESS = "file_upload_success";
	// 文件上传失败！
	public static final String FILE_UPLOAD_FAILED = "file_upload_failed";
	// 文件上传成功，s3key信息存入DB失败！
	public static final String FILE_UPLOAD_SUCCESS_WHILE_S3KEY_SAVE_FAILED = "file_upload_success_while_s3key_save_failed";
	// 本地上传文件列表查询成功！
	public static final String FILE_LIST_SUCCESS = "file_list_success";
	// 本地上传文件列表查询失败！
	public static final String FILE_LIST_FAILED = "file_list_failed";
	// S3文件删除失败：文件名不能为空
	public static final String FILE_DELETE_FAILED_FILENAME_EMPTY = "file_delete_failed_filename_empty";
	// S3文件删除成功！
	public static final String FILE_DELETE_SUCCESS = "file_delete_success";
	// S3文件删除失败！
	public static final String FILE_DELETE_FAILED = "file_delete_failed";
	// S3文件删除成功，但数据库记录删除失败！
	public static final String FILE_DELETE_SUCCESS_WHILE_DB_UPDATE_FAILED = "file_delete_success_while_db_update_failed";

	// S3文件重命名失败：源文件名不能为空！
	public static final String FILE_RENAME_FAILED_SOURCENAME_EMPTY = "file_rename_failed_sourcename_empty";
	// S3文件重命名失败：目标文件名不能为空！
	public static final String FILE_RENAME_FAILED_TRAGETNAME_EMPTY = "file_rename_failed_tragetname_empty";
	// S3文件重命名成功！
	public static final String FILE_RENAME_SUCCESS = "file_rename_success";
	// S3文件重命名失败！
	public static final String FILE_RENAME_FAILED = "file_rename_failed";
	// S3文件重命名成功，但DB里对应的条目更新失败！
	public static final String FILE_RENAME_SUCCESS_WHILE_DB_UPDATE_FAILED = "file_rename_success_while_db_update_failed";

}
