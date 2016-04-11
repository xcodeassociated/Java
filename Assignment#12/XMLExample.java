import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XMLExample {
	public static void main(String[] args) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder docB = docBF.newDocumentBuilder();

		Document doc = docB.newDocument();
		Element root = doc.createElement( "object" );
		Element name = doc.createElement( "className" );
		Text className = doc.createTextNode( "Czlowiek" );
		
		Element state = doc.createElement( "classState" );
		Element imie = doc.createElement( "imie" );
		Element wiek = doc.createElement( "wiek" );
		
		Text imieValue = doc.createTextNode( "Piotr" );
		Text wiekValue = doc.createTextNode( "43" );

		Attr atrI = doc.createAttribute( "type" );
		atrI.setNodeValue( "String");

		imie.appendChild( imieValue );
		imie.setAttributeNode( atrI );
		wiek.appendChild( wiekValue );
		wiek.setAttribute( "type", "Integer" );;
		
		name.appendChild( className );
		
		state.appendChild( imie );
		state.appendChild( wiek );
		
		root.appendChild( name );
		root.appendChild( state );
		doc.appendChild( root );
				
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		StreamResult result =  new StreamResult(System.out);
		
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		t.transform( new DOMSource( doc ), result);
		
	}
}
