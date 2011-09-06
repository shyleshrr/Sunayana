package org.gov.sunayana.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RemoveBytes {
	private static int STRIP_LEN = 46;
	private static String fileISO = "bpx.iso";
	private static String fileRAW = "bpx_new.jp2";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		getFile(null);
	}

	public static void getFile(String args) throws FileNotFoundException, IOException {
		// File file = new
		// File("C:/Documents and Settings/admin/Desktop/UID LOGS/" + fileISO);
//		File file = new File("C:/Documents and Settings/admin/Desktop/UID LOGS/images");
		File file = new File("C:/Documents and Settings/admin/Desktop/POSTMORTEM");
		if (!file.isDirectory()) {
			return;
		}
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File fileNew = files[i];

			FileInputStream fis = new FileInputStream(fileNew);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			byte[] dest = null;
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			byte[] bytes = bos.toByteArray();
			dest = new byte[bytes.length - STRIP_LEN];

			System.arraycopy(bytes, STRIP_LEN, dest, 0, (bytes.length - STRIP_LEN));
			writeByteArrayToFile(dest, fileNew.getName()+".jp2");
		}
	}

	public static void writeByteArrayToFile(byte[] baarray, String fileNew) {
		System.out.println("New file " + baarray.length);
//		String strFilePath = "C:/Documents and Settings/admin/Desktop/UID LOGS/imagesRaw/";
		String strFilePath = "C:/Documents and Settings/admin/Desktop/POSTMORTEM/";
		File f;
		try {
			f = new File(strFilePath+fileNew);
			if (!f.exists()) {
				f.createNewFile();
				System.out.println("New file created");
			} else {
				System.out.println("Cant create the file...");
			}
			FileOutputStream fos = new FileOutputStream(f);

			fos.write(baarray);

			System.out.println("Writing completed");

			fos.close();

		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException : " + ex);
		} catch (IOException ioe) {
			System.out.println("IOException : " + ioe);
		}
	}
}
