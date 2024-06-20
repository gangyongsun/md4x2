package cn.com.goldwind.md4x.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * 
 * @Title: IKAnalyzerUtil.java
 * @Package cn.com.goldwind.md4x.util
 * @description 分词公共类
 * @author 孙永刚
 * @date Sep 8, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public class IKAnalyzerUtil {
	/**
	 * IK分词
	 * 
	 * @param filterContent
	 * @return
	 */
	public static List<String> iKSegmenterToList(String filterContent) {
		List<String> result = new ArrayList<String>();
		if (StringUtils.isNotBlank(filterContent)) {
			StringReader stringReader = new StringReader(filterContent);
			// 关闭智能分词 (对分词的精度影响较大)
			IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
			Lexeme lex;
			try {
				while ((lex = ikSegmenter.next()) != null) {
					String lexemeText = getNumspecifiedResult(lex.getLexemeText(), 2);
					if (null != lexemeText) {
						result.add(lexemeText);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * 处理字符串，如果字符串大于num个字符，则返回吗，否则返回null
	 * 
	 * @param target 目标字符串
	 * @param num    字符限制
	 * @return
	 */
	private static String getNumspecifiedResult(String target, int num) {
		char[] c = target.toCharArray();
		int count = 0;
		for (int i = 0; i < c.length; i++) {
			String len = Integer.toBinaryString(c[i]);
			if (len.length() > 8) {
				count++;
			}
		}
		if (count >= num) {
			return target;
		} else {
			return null;
		}
	}

}
