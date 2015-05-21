
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
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		NodeList nl = null;

		if (qName.equals("move")) {
			session = attributes.getValue("session");
			nl = getNodeList("//game[@session=\"" + session + "\"]");

			if(nl.getLength() > 0) {
				game = nl.item(0);

			} else {
				if(getNodeList("//games").getLength() < 1){
					addNode("jeopardy", newElem("games", "", ""));
				}

				game = newElem("game", "session", session);
				addNode("games",game);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName){
		Element elem = null;
		NodeList lst = null;

		if(qName.equals("player")){

			if(!checkNode("//users/user[@username=\""+ eleText +"\"]")){
				SSD.exit("There is no user called "+ eleText);
			}

			/*add a new player node to the game, if there are less than two players in the game*/
			if(getNodeList("//game[@session=\""+session+"\"]/player").getLength() < 2) {
				player = eleText;
				elem = newElem("player", "ref", eleText);
				game.insertBefore(elem, game.getChildNodes().item(0));

			} else {
				SSD.exit("Only two players can participate in the same game");
			}

			/*check if there is already an "asked" element with the same question number.
			 *If there is, add the question to that node, create a new asked element otherwise*/
		} else if(qName.equals("question")){

			if(!checkNode("//question[@id=\""+ eleText +"\"]")){
				SSD.exit("Question number "+ eleText +" is not a valid question");
			}

			String path = "//game[@session=\""+session+"\"]/asked[@question=\""+ eleText +"\"]";
			lst = getNodeList(path);
			if(lst.getLength() < 1) {

				elem = newElem("asked", "question", eleText);
				game.appendChild(elem);
				lst = getNodeList(path);
			}
				asked = lst.item(0);

			/*add an answer of the player to the list of given answers*/
		} else if(qName.equals("answer")){
			elem = newElem("givenanswer", "player", player);
			elem.appendChild(jeopardyDoc.createTextNode(eleText));
			asked.appendChild(elem);
		}
	}


	/*** Helper methods ***/

	private Element newElem(String node, String attr, String attrVal){
		Element elem = jeopardyDoc.createElement(node);
		if(!attr.isEmpty()) {
			elem.setAttribute(attr, attrVal);
		}
		return elem;
	}

	/*get a list of nodes specifid in "path"*/
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

	/*appends a node to the document;
	 *ATTENTION: it will append the specified node to the first node found with the given tag name*/
	private void addNode(String tag, Node node){
		jeopardyDoc.getElementsByTagName(tag).item(0).appendChild(node);
	}

	private boolean checkNode(String path){
		if(getNodeList(path).getLength() < 1)
			return false;
		return true;
	}
}

