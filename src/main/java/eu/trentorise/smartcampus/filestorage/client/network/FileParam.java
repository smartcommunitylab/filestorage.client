package eu.trentorise.smartcampus.filestorage.client.network;

import java.io.File;

public class FileParam {
	private File file;
	private String paramName;

	public FileParam() {
	}

	public FileParam(File file, String paramName) {
		super();
		this.file = file;
		this.paramName = paramName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
