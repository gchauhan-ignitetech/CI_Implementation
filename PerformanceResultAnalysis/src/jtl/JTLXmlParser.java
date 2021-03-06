package jtl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class JTLXmlParser {

	public ArrayList<Sampler> parseXml(InputStream in)
	{
		//Create a empty link of users initially
		ArrayList<Sampler> users = new ArrayList<Sampler>();
		try 
		{
			//Create default handler instance
			JTLParserHandler handler = new JTLParserHandler();
			
			//Create parser from factory
			XMLReader parser = XMLReaderFactory.createXMLReader();
			
			//Register handler with parser
			parser.setContentHandler(handler);

			//Create an input source from the XML input stream
			InputSource source = new InputSource(in);
			
			//parse the document
			parser.parse(source);
			
			//populate the parsed users list in above created empty list; You can return from here also.
			users = handler.getSample();

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return users;
	}
}
