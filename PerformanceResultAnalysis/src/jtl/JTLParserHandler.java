package jtl;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class JTLParserHandler extends DefaultHandler
{
	//This is the list which shall be populated while parsing the XML. 
    private ArrayList<Sampler> sampleList = new ArrayList<Sampler>();
    
    //As we read any XML element we will push that in this stack
    private Stack<String> elementStack = new Stack<String>();
    
    //As we complete one user block in XML, we will push the User instance in userList 
    private Stack<Sampler> objectStack = new Stack<Sampler>();

    public void startDocument() throws SAXException
    {
//        System.out.println("start of the document   : ");
    }

    public void endDocument() throws SAXException
    {
//        System.out.println("end of the document document     : ");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
    	//Push it in element stack
        this.elementStack.push(qName);

        //If this is start of 'user' element then prepare a new User instance and push it in object stack
        if ("sample".equals(qName))
        {
            //New User instance
        	Sampler sample = new Sampler();
//            System.out.println(attributes.getLength());
            //Set all required attributes in any XML element here itself
            if(attributes != null && attributes.getLength() >= 1)
            {
            	sample.setlb(attributes.getValue("lb"));
            	sample.sett(Integer.parseInt(attributes.getValue("t")));
            	if(attributes.getValue("s") != null && attributes.getValue("s").contains("true")) {
            		sample.sets(0);
            	} else {
            		sample.sets(100);
            	}
            }
            this.objectStack.push(sample);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException
    {	
    	//Remove last added </user> element
        this.elementStack.pop();

        //User instance has been constructed so pop it from object stack and push in userList
        if ("sample".equals(qName))
        {
            Sampler object = this.objectStack.pop();
            this.sampleList.add(object);
        }
    }

    /**
     * This will be called everytime parser encounter a value node
     * */
    /*public void characters(char[] ch, int start, int length) throws SAXException
    {
        String value = new String(ch, start, length).trim();

        if (value.length() == 0)
        {
            return; // ignore white space
        }
        
        //handle the value based on to which element it belongs
        if ("sample".equals(currentElement()))
        {
            Sampler user = (Sampler) this.objectStack.peek();
            user.setlb(value);
        }
    }*/
    
    /**
     * Utility method for getting the current element in processing
     * */
    /*private String currentElement()
    {
        return this.elementStack.peek();
    }*/
    
    //Accessor for userList object
    public ArrayList<Sampler> getSample()
    {
    	return sampleList;
    }
}
