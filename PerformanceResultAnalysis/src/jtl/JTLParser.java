package jtl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class JTLParser {

	public static ArrayList<Sampler> readJTLFile(File xmlFile) throws FileNotFoundException{
		//Create the parser instance
		JTLXmlParser parser = new JTLXmlParser();

		//Parse the file
		ArrayList<Sampler> sample = parser.parseXml(new FileInputStream(xmlFile));

		//Verify the result
		//		System.out.println(sample);
		return sample;
	}

	public static Map<String,Double> prepReport(ArrayList<Sampler> sample){
		Map<String, Double> averagByName=sample.stream()
				.collect(
						Collectors.groupingBy(
								Sampler::getlb,
								Collectors.averagingInt(Sampler::gett)
								)
						);


		//prints the elements without ordering
		//averagByName.forEach((key, value) -> System.out.println(key + ":" + value));

		//sorting the collection
		List<String> keys = new ArrayList<String>(averagByName.keySet());
		Collections.sort(keys);

		/*		for ( String s : keys ) {
			System.out.println(s+" : "+ Math.round(averagByName.get(s)));
		}*/
		return averagByName;
	}

	public static Boolean compareResultWithSLA(File resultXML, String SLAFile) throws FileNotFoundException {
		Boolean meetSLA = true;
		ArrayList<Sampler> fileData = readJTLFile(resultXML);
		Map<String, Double> perfReport = prepReport(fileData);
		ArrayList<String> stepsFault=new ArrayList<String>();
		//		System.out.println(perfReport);
		readSLA anchor = new readSLA();
		HashMap<String, Double> expSLA = anchor.readCsv(SLAFile);
		for (String key : expSLA.keySet()) {
			if(expSLA.get(key) < perfReport.get(key)) {
//				System.out.println(key);
				stepsFault.add(key);
			}
		}
		if(stepsFault.size()>0) {
			meetSLA=false;
		}
		System.out.println(stepsFault);
		return meetSLA;
	}

	public static void main(String[] args) throws FileNotFoundException{
		//Locate the file
		File xmlFile = new File("D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\results_2.jtl");
		String SLAFilePath = "D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\ProjSLA.csv";
		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));
	}
}

