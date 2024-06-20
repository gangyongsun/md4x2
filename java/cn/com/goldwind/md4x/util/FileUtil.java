package cn.com.goldwind.md4x.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作公共类
 * 
 * @author alvin
 *
 */
public class FileUtil {
	private final static Class<? extends Object> SELF = FileUtil.class;

	/**
	 * 保存文件
	 * 
	 * @param fileUploadPath
	 * @param file
	 * @return
	 */
	public static boolean saveFile(String fileUploadPath, MultipartFile file) {
		boolean flag = false;
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件保存路径
				String filePath = fileUploadPath + file.getOriginalFilename();
				// 转存文件
				file.transferTo(new File(filePath));
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				LoggerUtils.error(SELF, "保存文件失败！", e);
			}
		}
		return flag;
	}

	/**
	 * 多文件压缩zip后下载
	 * 
	 * @param fileNames
	 * @param filePath
	 * @param response
	 */
	public static void download(String[] fileNames, String filePath, HttpServletResponse response) {
		// 设置最终输出zip文件的文件名
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String zipFileName = formatter.format(new Date()) + ".zip";
		String strZipPath = filePath + "/" + zipFileName;

		ZipOutputStream zipStream = null;
		FileInputStream zipSource = null;
		BufferedInputStream bufferStream = null;
		File zipFile = new File(strZipPath);
		try {
			// 构造最终压缩包的输出流
			zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
			for (int i = 0; i < fileNames.length; i++) {
				// 解码获取真实路径与文件名
				String realFileName = URLDecoder.decode(fileNames[i], "UTF-8");
				File file = new File(filePath + fileNames[i]);
				// TODO:未对文件不存在时进行操作，后期优化。
				if (file.exists()) {
					zipSource = new FileInputStream(file);// 将需要压缩的文件格式化为输入流
					/**
					 * 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样这里的name就是文件名, 文件名和之前的重复就会导致文件被覆盖
					 */
					ZipEntry zipEntry = new ZipEntry(realFileName);// 在压缩目录中文件的名字
					zipStream.putNextEntry(zipEntry);// 定位该压缩条目位置，开始写入文件到压缩包中
					bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
					int read = 0;
					byte[] buf = new byte[1024 * 10];
					while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
						zipStream.write(buf, 0, read);
					}
				}
			}
			// 判断系统压缩文件是否存在：true-把该压缩文件通过流输出给客户端后删除该压缩文件 false-未处理
			if (zipFile.exists()) {
				download(zipFileName, filePath, response);
				zipFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.error(SELF, "保存文件失败！", e);
		} finally {
			IoUtil.close(bufferStream, zipStream, zipSource);
		}
	}

	/**
	 * 单文件下载文件
	 * 
	 * @param fileName
	 * @param filePath
	 * @param response
	 * @throws Exception
	 */
	public static void download(String fileName, String filePath, HttpServletResponse response) throws Exception {
		if (null != fileName) {
			File file = new File(filePath, fileName);
			LoggerUtils.debug(SELF, "file路径:" + file);
			if (file.exists()) {
				// 设置强制下载不打开,也有设置成response.setContentType("application/octet-stream
				// charset=utf-8");
				response.setContentType("application/force-download charset=utf-8");
				// 设置文件名
				response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso8859-1"));
				response.setContentLength((int) file.length());
				byte[] buffer = new byte[1024];

				try (FileInputStream fis = new FileInputStream(file);
						BufferedInputStream bis = new BufferedInputStream(fis);
						OutputStream os = response.getOutputStream();
				) {
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
					LoggerUtils.error(SELF, "下载文件失败！", e);
				}
			} else {
				LoggerUtils.debug(SELF, "文件不存在:" + file.getAbsolutePath());
			}
		} else {
			LoggerUtils.debug(SELF, "下载的文件名为空");
		}
	}

	/**
	 * @Author yaleiwang
	 * @Description zip file 下载
	 * @Date 2020-9-24 20:15
	 * @param file zip file
	 * @param response response
	 * @param request request
	 * @return void
	 **/
	public static void downloadV2(File file, HttpServletResponse response, HttpServletRequest request) throws Exception {
		if (!StringUtils.isNullOrEmpty(file.getName())) {
			LoggerUtils.debug(SELF, "file路径:" + file);
			if (file.exists()) {
				String fileName = file.getName();
				fileName = URLEncoder.encode(fileName,"UTF-8");
				//清除缓存
				response.reset();
				response.setContentType(request.getServletContext().getMimeType(fileName));
				// 设置文件名
				response.setHeader("Content-Disposition", "attachment;filename="+fileName);
				response.setContentLength((int) file.length());

				FileInputStream fis = null;
				BufferedInputStream bis = null;
				OutputStream out = null;
				try{
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					out = new BufferedOutputStream(response.getOutputStream());
					byte[] buffer = new byte[512];

					int i;
					while ((i = bis.read(buffer)) != -1) {
						out.write(buffer, 0, i);
					}
					out.flush();
				}catch (Exception e){
					e.printStackTrace();
					LoggerUtils.error(SELF, "下载文件失败！", e);
				}finally {
					IoUtil.close(bis, fis,out);
				}
			} else {
				LoggerUtils.debug(SELF, "文件不存在:" + file.getAbsolutePath());
			}
		} else {
			LoggerUtils.debug(SELF, "下载的文件名为空");
		}
	}

	/**
	 * 判断文件夹是否以斜杠结尾，没有加入斜杠
	 * 
	 * @param filePath
	 * @return
	 */
	public static String setPathEndWithSlash(String filePath) {
		if (!isDirectoryEndWithSlash(filePath)) {
			filePath = filePath + "/";
		}
		return filePath;
	}

	/**
	 * 判断目录是否以斜杠结尾
	 * 
	 * @param path 目录名
	 * @return
	 */
	public static boolean isDirectoryEndWithSlash(String path) {
		return path.endsWith("/") || path.endsWith("\\");
	}

}