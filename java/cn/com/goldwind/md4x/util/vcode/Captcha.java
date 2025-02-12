package cn.com.goldwind.md4x.util.vcode;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;

/**
 * 验证码抽象类,暂时不支持中文
 * 
 * @author alvin
 *
 */
public abstract class Captcha extends Randoms {
	protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 28); // 字体
	protected int len = 5; // 验证码随机字符长度
	protected int width = 150; // 验证码显示跨度
	protected int height = 40; // 验证码显示高度
	private String chars = null; // 随机字符串

	/**
	 * 生成随机字符数组
	 * 
	 * @return 字符数组
	 */
	protected char[] alphas() {
		char[] cs = new char[len];
		for (int i = 0; i < len; i++) {
			cs[i] = alpha();
		}
		chars = new String(cs);
		return cs;
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @return Color 随机颜色
	 */
	protected Color color(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + num(bc - fc);
		int g = fc + num(bc - fc);
		int b = fc + num(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 验证码输出,由子类实现
	 * 
	 * @param os 输出流
	 */
	public abstract void out(OutputStream os);

	/**
	 * 获取随机字符串
	 * 
	 * @return string
	 */
	public String text() {
		return chars;
	}
}