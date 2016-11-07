package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class Refactor {
	
	public static void baseProcess(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			List<String> fileContent = new ArrayList<String>();
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.replace("augusta", "soy");
				line = line.replace("Augusta", "Soy");
				line = line.replace("AUGUSTA", "SOY");
				fileContent.add(line);
			}
			br.close();
			fr.close();
			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String tmp : fileContent) {
				bw.write(tmp + "\n");
			}
			bw.close();
			fw.close();
			System.out.println(FilenameUtils.getName(file.getName()));
		} catch (Exception e) {
			
		}
	}
	
	public static void mainProcess(File file) {
		if (file.isDirectory()) {
			for (File cFile : file.listFiles()) {
				mainProcess(cFile);
			}
		} else if (file.isFile()) {
			baseProcess(file);
		}
	}
	
	public static void mainProcessPom(File file) {
		if (file.isDirectory()) {
			for (File cFile : file.listFiles()) {
				mainProcess(cFile);
			}
		} else if (file.isFile() && file.getName().equalsIgnoreCase("pom.xml") && !file.getAbsolutePath().contains("classes") 
				&& !file.getAbsolutePath().contains("bin")) {
			baseProcess(file);
		}
	}
	
	public static void refactor() {
		List<String> paths = Arrays.asList("../soy-site/views");
		for (String path : paths) {
			mainProcess(new File(path));
		}
	}
	public static void main (String[] args) {
		refactor();
	}
}
