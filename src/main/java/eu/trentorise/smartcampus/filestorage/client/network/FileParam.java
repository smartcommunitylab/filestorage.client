package eu.trentorise.smartcampus.filestorage.client.network;

import java.io.File;

public class FileParam extends MultipartParam {
	private File file;

	public FileParam(String paramName) {
		super(paramName);
	}

	public FileParam(String paramName, File file) {
		super(paramName);
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
