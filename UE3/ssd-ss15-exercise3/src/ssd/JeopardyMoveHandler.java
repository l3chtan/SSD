
package ssd;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.Exception;
import java.lang.Override;
import java.lang.System;

/**
 * TODO: Implement this content handler.
 */
public class JeopardyMoveHandler extends DefaultHandler {
	/**
	 * Use this xPath variable to create xPath expression that can be
	 * evaluated over the XML document.
	 */
	private static XPath xPath = XPathFactory.newInstance().newXPath();
	
	/**
	 * Store and manipulate the Jeopardy XML document here.
	 */
	private Document jeopardyDoc;
	
	/**
	 * This variable stores the text content of XML Elements.
	 */
	private String eleText;

	//***TODO***
	//Insert local variables here

	private Node game = null, asked = null;
    private String player, session;
	
    public JeopardyMoveHandler(Document doc) {
    	jeopardyDoc = doc;
    }
    
    @Override
    /**
     * SAX calls this method to pass in character data
     */
  	public void characters(char[] text, int start, int length)
  			    throws SAXException {
  		eleText = new String(text, start, length);
/*
		System.out.println("Characters: " + eleText);
*/

	}

    /**
     * 
     * Return the current stored Jeopardy document
     * 
     * @return XML Document
     */
	public Document getDocument()/*
		System.out.println("Characters: " + eleText);
*/
 {
		return jeopardyDoc;
	}
    
    //***TODO***
	//Specify additional methods to parse the move document and modify the jeopardyDoc

	@Override
	public void startDocument(){


		System.out.println("Beginning of document");
	}

	@Override
	public void endDocument(){
		System.out.println("End of document");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		NodeList nl = null;
		if (qName.equals("move")) {
			session = attributes.getValue("session");
			nl = getNodeList("//game[@session=\""+session+"\"]");

			game = nl.item(0);


		}/* else if(qName.equals("question")){jeopardy/games/game
			System.out.println(session);
			nl = getNodeList("jeopardy/games/game/asked[@question=\""+eleText+"\"]");
		//	asked = getNode("asked","question",attributes);
			System.out.println(nl.getLength());
		}*/
	}

	@Override
	public void endElement(String uri, String localName, String qName){
		Element elem = null;
		NodeList lst = null;
		if(qName.equals("player")){
		//	lst = getNodeList("jeopardy/games/game[@session=\""+session+"\"]/player");

			if(getNodeList("//game[@session=\""+session+"\"]/player").getLength() < 2) {
				player = eleText;
				elem = newElem("player", "ref");
				//	game.appendChild(elem);
				game.insertBefore(elem, game.getChildNodes().item(0));
			}
		} else if(qName.equals("question")){
			String path = "//game[@session=\""+session+"\"]/asked[@question=\""+eleText+"\"]";
			lst = getNodeList(path);
			if(lst.getLength() < 1) {
				System.out.println(lst.getLength());

				elem = newElem("asked", "question");
				elem.setAttribute("question", eleText);
				game.appendChild(elem);
				lst = getNodeList(path);
			}
				asked = lst.item(0);
				System.out.println("foo");


		} else if(qName.equals("answer")){
			elem = newElem("givenanswer", "player");
			elem.setAttribute("player", player);
			elem.appendChild(jeopardyDoc.createTextNode(eleText));
			asked.appendChild(elem);
		}
	}

	private Element newElem(String node, String attr){
		Element elem = jeopardyDoc.createElement(node);
		elem.setAttribute(attr, eleText);
		return elem;
	}

	private NodeList getNodeList(String path){
		NodeList gameList = null;
		try {
			XPathExpression xpathExpr = xPath.compile(path);
			gameList = (NodeList)xpathExpr.evaluate(jeopardyDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.getMessage();
		}
		return gameList;
	}

/*	private Node getNode(String node, String attr, Attributes attributes){
		Node temp = null;
		for (int i = 0; i < jeopardyDoc.getElementsByTagName(node).getLength(); i++) {
			temp = jeopardyDoc.getElementsByTagName(node).item(i);
			if (temp.getAttributes().getNamedItem(attr).getNodeValue()
					.equals(attributes.getValue(attr))) {
				System.out.println("jeopardy: " + temp.getAttributes().getNamedItem(attr).getNodeValue());
				System.out.println("move: " + attributes.getValue(attr));
				return temp;
			}
		}
		return temp;
	}*/
}

