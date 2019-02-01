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

		//sorting the collection
		List<String> keys = new ArrayList<String>(averagByName.keySet());
		Collections.sort(keys);

		/*for ( String s : keys ) {
			System.out.println(s+" : "+ Math.round(averagByNameE.get(s)));
		}*/
		return averagByName;
	}

	public static Map<String,Double> prepReportError(ArrayList<Sampler> sample){
		Map<String, Double> averagByName=sample.stream()
				.collect(
						Collectors.groupingBy(
								Sampler::getlb,
								Collectors.averagingInt(Sampler::gets)
								)
						);

		//sorting the collection
		List<String> keys = new ArrayList<String>(averagByName.keySet());
		Collections.sort(keys);

		/*for ( String s : keys ) {
			System.out.println(s+" : "+ Math.round(averagByName.get(s)));
		}*/
		return averagByName;
	}

	public static Boolean compareResultWithSLA(File resultXML, String SLAFile) throws FileNotFoundException {
		Boolean meetSLA = true;

		//		Read Performance report JTL
		ArrayList<Sampler> fileData = readJTLFile(resultXML);

		//		Create report on test step name and Average time
		Map<String, Double> perfReport = prepReport(fileData);
		ArrayList<String> stepsFault=new ArrayList<String>();
		//				System.out.println(perfReport);
		//		Read SLA file of the project
		readSLA anchor = new readSLA();
		HashMap<String, Double> expSLA = anchor.readCsv(SLAFile);
		//		Identify the steps having Average time greater than SLA
		for (String key : expSLA.keySet()) {
			if(expSLA.get(key) < perfReport.get(key)) {
				//				System.out.println(key);
				stepsFault.add(key);
			}
		}

		//		If Size of identified steps is greater than 0 then raise the flag
		if(stepsFault.size()>0) {
			meetSLA=false;
			System.out.println("SLA Barding Steps : " + stepsFault);
		}

		//		If SLA is met then try to find that at each step error percentage is >= 20%
		Map<String, Double> perfReportError = prepReportError(fileData);
		ArrayList<String> stepsFaultError=new ArrayList<String>();
		if(meetSLA) {
			for (String key : perfReportError.keySet()) {
				if(perfReportError.get(key) >= 20) {
					stepsFaultError.add(key);
				}
			}	
		}
		//		If Size of identified steps is greater than 0 then raise the flag
		if(stepsFaultError.size()>0) {
			meetSLA=false;
			System.out.println("Error Steps : " + stepsFaultError);
		}
		return meetSLA;
	}

	public static void main(String[] args) throws FileNotFoundException{
		//Locate the file
		File xmlFile = new File(args[0]);
		String SLAFilePath = args[1];
		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));

		//		For unit Testing.
		//		File xmlFile = new File("D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\results_2 - SLAError.jtl");
		//		String SLAFilePath = "D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\ProjSLA.csv";
		//		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));
		//		System.out.println("=======================================");
		//		xmlFile = new File("D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\results_2 - ExeError.jtl");
		//		SLAFilePath = "D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\ProjSLA.csv";
		//		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));
		//		System.out.println("=======================================");
		//		xmlFile = new File("D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\results_2 - ExeError60.jtl");
		//		SLAFilePath = "D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\ProjSLA.csv";
		//		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));
		//		System.out.println("=======================================");
		//		xmlFile = new File("D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\results_2.jtl");
		//		SLAFilePath = "D:\\apache-jmeter-4.0\\JTLProj\\AnalysingJTL\\src\\jtl\\ProjSLA.csv";
		//		System.out.println(compareResultWithSLA(xmlFile, SLAFilePath));
	}
}

