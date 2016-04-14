package com.tabayon.arng;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

public class Generator {
	public static final int FEMALE = 1;
	public static final int MALE = 2;
	static PrintWriter writer;

	public static void main(String[] args) throws IOException {
		int numberOfRandomNamesNeeded = 1000;
		int numberOfNameSegments = 3;
		int neededGender = FEMALE;
		SecureRandom random = new SecureRandom();
		ArrayList<String> maleNames = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> femaleNames = null;
		loadNames("male.names", maleNames);
		//loadNames("dbids.txt", ids); for males
		loadNames("fem-ids.txt", ids); //for others
		if (neededGender == FEMALE) {
			femaleNames = new ArrayList<String>();
			loadNames("female.names", femaleNames);
		}
		for (int i = 0; i < numberOfRandomNamesNeeded; i++) {
			int ms = maleNames.size();
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < numberOfNameSegments; j++) {
				if (neededGender == FEMALE && j == 0) {
					sb.append(" ").append(femaleNames.get(Math.abs(random.nextInt() % femaleNames.size())));
				}
				sb.append(" ").append(maleNames.get(Math.abs(random.nextInt() % ms)));

			}
			String stmnt = "update persons set full_name = '" + sb.toString().trim() + "' where id = " + ids.get(i) + ";";
			outputToFile(stmnt);
		}

		if (writer != null)
			writer.close();
	}

	private static void outputToFile(String line) {
		try {
			if (writer == null)
				writer = new PrintWriter("names-fem-stmnt.txt", "UTF-8");
			writer.println(line);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private static void loadNames(String fileName, ArrayList<String> placeHolder) throws IOException {
		Scanner in = new Scanner(new FileReader(fileName));
		while (in.hasNext()) {
			placeHolder.add(in.nextLine());
		}
		in.close();
	}
}
