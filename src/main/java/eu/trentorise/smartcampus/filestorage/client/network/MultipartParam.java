package eu.trentorise.smartcampus.filestorage.client.network;

public class MultipartParam {
	private String paramName;

	public MultipartParam(String paramName) {
		super();
		this.paramName = paramName;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
