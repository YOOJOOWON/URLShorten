import java.util.Hashtable;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.net.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class URLShortner {
	
	public static final int MAXSIZE = 195112;
	public static final int KEYLENGTH = 8;
	
    Hashtable<String, RBTree[]> table;
    
    
	
	public URLShortner() {
		table = new Hashtable<String, RBTree[]>();
		load();
		 
	}
	

	
	String LongToShort(String longName){
		
		String st = longName;		
		URL aURL = null;
		
		//URL���� �Ǻ�
		try {
			aURL = new URL(st);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return "It's not URL";
		}
		
		//���� ���� ���ֱ� ����
		st = aURL.toString();
				
		
		// KEY1
		// ������ �̸��� �����Ͽ� ������ ������ �ܾ table�� key�� Ȱ��
		// ������ �̸��� ������ �� �����ϰ� ����� �������� ������ ���� ��ǥ�� ��
		String host = aURL.getHost();
		
		if (host.equals("localhost")){
			return "Cannot shorten itself";
		}
		
		host = (host.startsWith("www.")) ? host.substring(4) : host;
		String key1 = host.replaceAll("[aeiouAEIOU]", "");
		
		// ���� ���� �� ���̰� ������ ���
		// ���� 1���� ��� : ���� ���� �ݺ�
		// ���� 0���� ��� : �Ǿ� ������ 2�� �ݺ�
		if (key1.length() == 1){
			key1 = key1 + key1;
		}
		else if (key1.length() == 0){
			key1 = aURL.getHost().substring(0, 1) + aURL.getHost().substring(0, 1);			
		}
		
		key1 = key1.substring(0,2);
		
		
		// KEY1�� treeSet�� �ҷ��ͺ���
		// treeSet �̹� ���� ��쿡�� tree�� �״�� ����, ���� ��쿡�� ���� ���� table�� �����Ѵ�
		RBTree[] treeSet = table.get(key1);
		
		if (treeSet == null){
			treeSet = new RBTree[195112];
			table.put(key1, treeSet);
		}
		

		// KEY2
		// ������ �̸��� CRC32�� ��ȯ�� �� base58�� ��ȯ�Ͽ� ���� 3���ڸ� ���´�
		// �ش� key�� treeSet �迭�� �ּҰ����� ���δ�. 111 ~ zzz ���� �ִ� 195112��
		String key2 = Base58.IntToBase58((getCRC32Value(st)),3);
		
		int key2_int = Base58.IntFromBase58(key2);
		RBTree tree = treeSet[key2_int];
		
		// ���������� KEY2�� tree�� �ҷ�����, ���� null�� ��� ���� ���� ����
		if (tree == null){
			tree = new RBTree();
			treeSet[key2_int] = tree;
		}
		
		
		
		// KEY3
		// key3�� rbtree���� pair�� ���� val��
		// 123 ~ zyz ������ value�� ���� node�� ���� �� �ִ�
		int key3_int = tree.findName(st);
		
		String key;
		String key3;
		
		// ������ ����. �߰��� ��쿡�� �̹� ��ϵ� key�� �ִ� ��
		if (key3_int >= 0){
			key3 = Base58.IntToBase58(key3_int, 3);
		}
		else{
			key3_int = tree.findEmpty();
			if (key3_int < 0){	return "data is full";	}
			tree.insert(tree.root, key3_int, st);
			key3 = Base58.IntToBase58(key3_int, 3);
		}
		

		key = "http://localhost/" + key1 + key2 + key3;
		return key;		
	}
	
	String ShortToLong(String shortName){

		URL aURL = null;
		
		//URL���� �Ǻ�
		try {
			aURL = new URL(shortName);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return "It's not URL";
		}
		if (!aURL.getHost().equals("localhost")){
			return "Wrong input (not localhost)";
		}

		String st = aURL.getPath();
		st = st.substring(1);
		
		if (st.length() < KEYLENGTH){
			return "Key is too Short";
		}
		else if (st.length() > KEYLENGTH){
			return "Key is too Long";
		}
		
		String key1 = st.substring(0, 2);
		String answer;
		
		RBTree[] treeSet = table.get(key1);
		if (treeSet == null){
			return "Not Found";
		}
		
		String key2 = st.substring(2, 5);
		int key2_int = Base58.IntFromBase58(key2);
		RBTree tree = treeSet[key2_int];
		
		if (tree == null){
			return "Not Found";
		}
		
		String key3 = st.substring(5, 8);
		
		
		int key3_int = Base58.IntFromBase58(key3);
		RBTree.Node node = tree.search(tree.root, key3_int);
		if (node.val != -1){
			answer = node.name;				
		}
		else {
			return "Not Found";
		}
				
		return answer;		
	}
	

	public static long getCRC32Value(String filename) {  
		Checksum crc = new CRC32();
	    byte buffer[] = filename.getBytes();
	        
	    crc.update(buffer, 0, buffer.length);
	
	
	    long checksumValue = crc.getValue();
	
	    return checksumValue;
	}	
	
	
	//xml�� �̿��� ���̺� �ε�
	void load(){
		try {
			File fXmlFile = new File("saved.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
	        NodeList descNodes = doc.getElementsByTagName("List");
	        
	        for(int i=0; i<descNodes.getLength();i++){
	        	 
	            for(Node node1 = descNodes.item(i).getFirstChild(); node1!=null; node1=node1.getNextSibling()){ //ù��° �ڽ��� �������� ���������� ���� ������ ����
	 
	                if(node1.getNodeName().equals("key1")){
	                	NamedNodeMap node1Attrs = node1.getAttributes();
	                    String key1 = node1Attrs.getNamedItem("val").getNodeValue();
	                    
	            		RBTree[] treeSet = table.get(key1);	            		
	            		if (treeSet == null){
	            			treeSet = new RBTree[195112];
	            			table.put(key1, treeSet);
	            		}
	            		
	                    NodeList childNodes
	                    = node1.getChildNodes();
	            		
	                    for(int j=0; j<childNodes.getLength(); j++){
	                        Node node2 = childNodes.item(j);
		                	NamedNodeMap node2Attrs = node2.getAttributes();
		                    String key2 = node2Attrs.getNamedItem("val").getNodeValue();
		                    int key2_int = Integer.parseInt(key2);

		            		RBTree tree = treeSet[key2_int];
		            		
		            		// ���������� KEY2�� tree�� �ҷ�����, ���� null�� ��� ���� ���� ����
		            		if (tree == null){
		            			tree = new RBTree();
		            			treeSet[key2_int] = tree;
		            		}
		            		
		                    NodeList childNodes2
		                    = node2.getChildNodes();

		                    for(int k=0; k<childNodes2.getLength(); k++){

		                        Node node3 = childNodes2.item(k);
			                	NamedNodeMap node3Attrs = node3.getAttributes();
			                    String key3 = node3Attrs.getNamedItem("val").getNodeValue();
			                    String name = node3Attrs.getNamedItem("name").getNodeValue();
			                    int key3_int = Integer.parseInt(key3);
			                    tree.insert(tree.root, key3_int, name);
			                    System.out.println("success : " + name + " " + i + " " + j + " " + k);
		                    }      
	                    }
	                	
	                }
	            } 
	        }        
		}   
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(){
      try {
    	  Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

          
          Element rootElement = doc.createElement("List");
          doc.appendChild(rootElement);

          Set<String> keys = table.keySet();
          for(String key: keys){
        	  RBTree[] treeSet = table.get(key);
        	  if (treeSet == null) continue;        	  

              Element key1 = doc.createElement("key1");
              Attr attr1 = doc.createAttribute("val");
              attr1.setValue(key);
              key1.setAttributeNode(attr1);
              
        	  
              for (int i = 0; i < MAXSIZE; i++){
            	  RBTree tree = treeSet[i];
            	  if (tree == null) continue;
            	  
                  Element key2 = doc.createElement("key2");
                  Attr attr2 = doc.createAttribute("val");
                  attr2.setValue(String.valueOf(i));
                  key2.setAttributeNode(attr2);
            	  
                  
            	  tree.addToList(tree.root, 0);
            	  for (int j = 0; j < tree.listForOutput.size(); j++){
            		  RBTree.Node node = tree.listForOutput.get(j);
                      Element key3 = doc.createElement("key3");
                      Attr attr3 = doc.createAttribute("val");
                      attr3.setValue(String.valueOf(node.val));
                      Attr attr4 = doc.createAttribute("name");
                      attr4.setValue(node.name);
                      
                      key3.setAttributeNode(attr3);
                      key3.setAttributeNode(attr4);
                      
                      key2.appendChild(key3);
            	  }
            	  tree.listForOutput.clear();       
            	  
            	  key1.appendChild(key2);
              }
              rootElement.appendChild(key1);
          }
          DOMSource xmlDOM = new DOMSource(doc); StreamResult xmlFile = new StreamResult(new File("saved.xml")); 
          TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);

       } catch (Exception e) {
          e.printStackTrace();
       }
		
	}	
	
	
	
}
