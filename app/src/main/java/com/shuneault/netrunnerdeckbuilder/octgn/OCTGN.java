package com.shuneault.netrunnerdeckbuilder.octgn;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.text.Html;

import com.shuneault.netrunnerdeckbuilder.game.Card;
import com.shuneault.netrunnerdeckbuilder.game.Deck;
import com.shuneault.netrunnerdeckbuilder.helper.AppManager;

public class OCTGN {
	
	// OCTGN Specific values
	private static final String OCTGN_GAME_ID = "0f38e453-26df-4c04-9d67-6d43de939c77";
	private static final String OCTGN_CARD_ID_PREFIX = "bc0f047c-01b1-427f-a439-d451eda";
	
	private static final String KEY_QTY = "qty";
	private static final String KEY_ID = "id";
	private static final String XPATH_IDENTITY_ID = "/deck/section[@name='Identity']/card/@id";
	private static final String XPATH_DECK_NAME = "/deck/section[@name='Identity']/card";
	private static final String XPATH_DECK_NOTES = "/deck/notes";
	private static final String XPATH_CARD_LIST = "/deck/section[@name='R&D / Stack']/card";
	
	public static Deck toDeck(String xmlString) {
		try {
			// Build the XML document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(xmlString)));
			doc.getDocumentElement().normalize();
			// Get the Identity ID
			String strIdentityID = XPathFactory.newInstance().newXPath().evaluate(XPATH_IDENTITY_ID, doc);
			// Get the deck name
			String strDeckName = XPathFactory.newInstance().newXPath().evaluate(XPATH_DECK_NAME, doc);
			// Get the deck notes
			String deckNotes = XPathFactory.newInstance().newXPath().evaluate(XPATH_DECK_NOTES, doc);
			// Get the card list
			NodeList nodeCards = (NodeList) XPathFactory.newInstance().newXPath().evaluate(XPATH_CARD_LIST, doc, XPathConstants.NODESET);
			
			// Build the new deck
			Deck deck = new Deck(strDeckName, getCardCodeFromUUID(strIdentityID));
			deck.setNotes(Html.fromHtml( deckNotes ).toString());
			for (int i = 0; i < nodeCards.getLength(); i++) {
				Node node = nodeCards.item(i);
				Card card = AppManager.getInstance().getCard(getCardCodeFromUUID(node.getAttributes().getNamedItem(KEY_ID).getTextContent()));
				int count = Integer.parseInt( node.getAttributes().getNamedItem(KEY_QTY).getTextContent() );
				deck.setCardCount(card, count);
			}
			deck.clearCardsToAddAndRemove();
			
			// Return the deck
			return deck;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String fromDeck(Deck deck) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			// Root
			Element eleRoot = doc.createElement("deck");
			eleRoot.setAttribute("game", OCTGN_GAME_ID);
			doc.appendChild(eleRoot);
			// Section - Identity
			Element eleSectionIdentity = doc.createElement("section");
			eleSectionIdentity.setAttribute("name", "Identity");
			eleRoot.appendChild(eleSectionIdentity);
			// Card - Identity
			Element eleCardIdentity = doc.createElement("card");
			eleCardIdentity.setAttribute("qty", "1");
			eleCardIdentity.setAttribute("id", OCTGN_CARD_ID_PREFIX + deck.getIdentity().getCode());
			eleCardIdentity.setTextContent(deck.getIdentity().getTitle());
			eleSectionIdentity.appendChild(eleCardIdentity);
			// Section - cards
			Element eleSectionCards = doc.createElement("section");
			eleSectionCards.setAttribute("name", "R&D / Stack");
			eleRoot.appendChild(eleSectionCards);
			// Cards
			for (Card card : deck.getCards()) {
				Element eleCard = doc.createElement("card");
				eleCard.setAttribute("qty", String.valueOf(deck.getCardCount(card)));
				eleCard.setAttribute("id", OCTGN_CARD_ID_PREFIX + card.getCode());
				eleCard.setTextContent(card.getTitle());
				eleSectionCards.appendChild(eleCard);
			}
			// Notes
			Element eleNotes = doc.createElement("notes");
			eleNotes.setTextContent(deck.getNotes());
			eleRoot.appendChild(eleNotes);
			
			// Return the XML text
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static String getCardCodeFromUUID(String uuid) {
		return uuid.substring(uuid.length()-5);
	}
}