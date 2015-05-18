package ssd;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SSD {
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;

	public static void main(String[] args) throws Exception {
     	if (args.length != 3) {
            System.err.println("Usage: java SSD <input.xml> <move.xml> <output.xml>");
            System.exit(1);
        }
	
		String inputPath = args[0];
		String movePath = args[1];
		String outputPath = args[2];
    
       initialize();
       transform(inputPath, movePath, outputPath);
    }
	
	private static void initialize() throws Exception {    
       documentBuilderFactory = DocumentBuilderFactory.newInstance();
       documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
	
	/**
     * Use this method to encapsulate the main logic for this example. 
     * 
     * First read in the jeopardy document. 
     * Second create a JeopardyMoveHandler and an XMLReader (SAX parser)
     * Third parse the moveDocument = funktional
     * Last get the Document from the JeopardyMoveHandler and use a
     *    Transformer to print the document to the output path.
     * 
     * @param inputPath Path to the xml file to get read in.
     * @param movePath Path to the move file
     * @param outputPath Path to the xml file to print statistics.
     */
    private static void transform(String inputPath, String movePath, String outputPath) throws Exception {
     //   Document jeopardyDoc = documentBuilder.parse(inputPath);
        JeopardyMoveHandler jm = new JeopardyMoveHandler(documentBuilder.parse(inputPath));
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(jm);
        parser.parse(movePath);

        /*Write to xml file*/
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(jm.getDocument());
        StreamResult result = new StreamResult(new File(outputPath));
        transformer.transform(source, result);
    }

    /**
     * Prints an error message and exits with return code 1
     * 
     * @param message The error message to be printed
     */
    public static void exit(String message) {
    	System.err.println(message);
    	System.exit(1);
    }
    

}
