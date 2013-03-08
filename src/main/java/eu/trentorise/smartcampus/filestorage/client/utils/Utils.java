package eu.trentorise.smartcampus.filestorage.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static byte[] read(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[512];

		int byteReaded = 0;
		while ((byteReaded = is.read(buffer, 0, buffer.length)) != -1) {
			bos.write(buffer, 0, byteReaded);
		}
		return bos.toByteArray();
	}
}
