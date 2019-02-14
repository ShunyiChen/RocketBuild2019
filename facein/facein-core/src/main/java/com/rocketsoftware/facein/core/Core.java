package com.rocketsoftware.facein.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

/**
 * Hello world!
 *
 */
@Component
public class Core 
{
	
	private Process mProcess;

	/**
	 * 
	 * @param img1
	 * @param img2
	 */
	public String compareFaces(String img1Path, String img2Path) {
		Process process;
		try {
//			process = Runtime.getRuntime().exec("python script_python.py 1 2");
//			process = Runtime.getRuntime().exec("python face_match_demo.py --img1=images/daniel-radcliffe_2.jpg --img2=images/daniel-radcliffe_4.jpg");
			process = Runtime.getRuntime().exec("python face_match_demo.py --img1="+img1Path+" --img2="+img2Path);
			mProcess = process;
		} catch (Exception e) {
			System.out.println("Exception Raised" + e.toString());
		}
		InputStream stdout = mProcess.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println("python stdout: " + line);
				if(line.equals("Result = same person")) {
					return "Same Person";
				}
			}
		} catch (IOException e) {
			System.out.println("Exception in reading output" + e.toString());
		}
		return "Not Same Person";
	}
}
