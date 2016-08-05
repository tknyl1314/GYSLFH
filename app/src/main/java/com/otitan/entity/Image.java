package com.otitan.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Image implements Serializable {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
