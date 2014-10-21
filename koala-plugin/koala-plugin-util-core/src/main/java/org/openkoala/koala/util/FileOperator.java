package org.openkoala.koala.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOperator {

	/**
	 * 删除文件的某块连续行
	 * @param file
	 * @param startLine
	 * @param endLine
	 */
	public static void removeLinesFromFile(String filePath, int startLine, int endLine) {
		File file = new File(filePath);
		
		try {
			if (!file.isFile()) {
				System.out.println(file + " is not an existing file");
				return;
			}

			File tempFile = new File(file.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String lineString = null;
			int lineNumber = 0;
			while ((lineString = br.readLine()) != null) {
				++ lineNumber;
				if (lineNumber >= startLine && lineNumber <= endLine) {
					continue;
				}
				pw.println(lineString);
				pw.flush();
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!file.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(file))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
