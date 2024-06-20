package cn.com.goldwind.md4x.business.bo.zeppelin;

public class ZeppelinParagraphConfig {
	private boolean title = true;//是否显示paragraph title 
	private float colWidth = 12.0f;
	private boolean editorHide = false; //false:显示脚本并run  true:不显示脚本
	private boolean enabled = true;//true: paragraph 可执行    false:不可执行
	private int index;//paragraph 的序号
	
	public boolean isTitle() {
		return title;
	}
	public void setTitle(boolean title) {
		this.title = title;
	}
	public float getColWidth() {
		return colWidth;
	}
	public void setColWidth(float colWidth) {
		this.colWidth = colWidth;
	}
	public boolean isEditorHide() {
		return editorHide;
	}
	public void setEditorHide(boolean editorHide) {
		this.editorHide = editorHide;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

}
