package eu.trentorise.smartcampus.filestorage.client.network;

public class ByteArrayParam extends MultipartParam {

	private String filename;
	private String contentType;
	private byte[] content;

	public ByteArrayParam(String paramName) {
		super(paramName);
	}

	public ByteArrayParam(String paramName, String filename,
			String contentType, byte[] content) {
		super(paramName);
		this.filename = filename;
		this.contentType = contentType;
		this.content = content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
