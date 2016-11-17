package cn.gdin.diary.domain;

import java.io.Serializable;

public class DiaryItem implements Serializable {

	private static final long serialVersionUID = -7060210544600464481L;

	private String id;
	private String date;
	private String week;
	private String title;
	private String content;

	public DiaryItem(String id, String date, String week, String title,
			String content) {
		this.id = id;
		this.date = date;
		this.week = week;
		this.title = title;
		this.content = content;
	}

	public DiaryItem() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
