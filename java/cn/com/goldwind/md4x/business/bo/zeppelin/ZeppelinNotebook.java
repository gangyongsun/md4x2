package cn.com.goldwind.md4x.business.bo.zeppelin;

import java.util.List;

public class ZeppelinNotebook {
	private String name;
	private List<ZeppelinParagraph> zeppelinParagraphs;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ZeppelinParagraph> getZeppelinParagraphs() {
		return zeppelinParagraphs;
	}
	public void setZeppelinParagraphs(List<ZeppelinParagraph> zeppelinParagraphs) {
		this.zeppelinParagraphs = zeppelinParagraphs;
	}

}
