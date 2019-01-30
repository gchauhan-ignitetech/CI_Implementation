package jtl;

import java.io.*;
import java.util.*;


public class readSLA {

	public HashMap<String, Double> readCsv(String filePath) {
		String csvFile = filePath;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		HashMap<String, Double> expSLA = new HashMap<String, Double>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] rowData = line.split(cvsSplitBy);

//				System.out.println(rowData[0] + " : " + rowData[1]);
				expSLA.put(rowData[0].toString(), Double.parseDouble(rowData[1]));
			}
//			System.out.println(expSLA);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return expSLA;
	}
}
