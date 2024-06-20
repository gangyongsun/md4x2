package cn.com.goldwind.md4x.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class CsvUtil {

	/**
	 * 解析CSV文件并转成bean
	 * 
	 * @param csvFile CSV文件(必须为UTF-8编码，没有BOM)
	 * @param clazz   类
	 * @param <T>     泛型
	 * @return 泛型bean集合
	 */
	public static <T> List<T> getCsvData(MultipartFile csvFile, Class<T> clazz) {
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(csvFile.getInputStream(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(clazz);

		CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in).withSeparator(',').withQuoteChar('\'').withMappingStrategy(strategy).build();
		return csvToBean.parse();
	}

	/**
	 * 解析CSV文件成String数组
	 * 
	 * @param in          CSV File InputStream
	 * @param charsetName file 的编码方式 直接设置GBK就可以啦
	 */
	public static List<String[]> getCsvData(InputStream in, String charsetName) {
		List<String[]> list = new ArrayList<String[]>();
		int i = 0;
		try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(in, charsetName))).build()) {
			Iterator<String[]> iterator = csvReader.iterator();
			while (iterator.hasNext()) {
				String[] next = iterator.next();
				// 去除第一行的表头，从第二行开始
				if (i >= 1) {
					list.add(next);
				}
				i++;
			}
		} catch (Exception e) {
			System.out.println("CSV文件读取异常");
		}
		return list;
	}

}