package twitterApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

public class Feed implements Runnable {
	private URL url;
	private String encoding;
	private Document document;
	//private ArrayList<Item> itemList;
	public ArrayList<String> itemList2;

	public Feed() {
		url = null;
		encoding = "utf-8";
		document = null;
		//itemList = new ArrayList<Item>();
	}
	public void setURL(String url) {
		try {
			this.url = new URL(url);
		}
		catch(MalformedURLException e) {
			System.err.println("間違ったURL: " + url);
		}
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/** twitterApp.Feed の内容を Item オブジェクトのリストとして返す */
	public ArrayList<String> getItemList() {
		return itemList2;
	}
	/** URLで指示されたフィードを取得し DOM tree を構築、itemList を生成
	 * @return */
	public void run() {
		InputStream inputStream = null;
		try {
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)");
			connection.connect();
			inputStream = connection.getInputStream();
		}
		catch (IOException e) {
			System.err.println("接続エラー: " + e);
			System.exit(1);
		}

		try {
			// DOM実装(implementation)の用意 (Load and Save用)
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementationLS implementation = (DOMImplementationLS)registry.getDOMImplementation("XML 2.0");
			// 読み込み対象の用意
			LSInput input = implementation.createLSInput();
			input.setByteStream(inputStream);
			input.setEncoding(encoding);
			// 構文解析器(parser)の用意
			LSParser parser = implementation.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
			parser.getDomConfig().setParameter("namespaces", false);
			// DOMの構築
			document = parser.parse(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// root要素 (rdf:RDF または rss または feed ) を findItems に渡す
			itemList2 = findItems(document.getDocumentElement());
			//System.out.println(itemList2);
		}
		catch (DOMException e) {
			System.err.println("DOMエラー:" + e);
		}
	}
	/** node の下位(子孫)から item 要素を探し Item オブジェクトを得る(深さ優先探索)
	 * @return */
	private ArrayList<String> findItems(Node node) {
		// node の子ノードについて繰り返す
		ArrayList<String> itemList = new ArrayList<String> ();
		for(Node current = node.getFirstChild();
				current != null;
				current = current.getNextSibling()) {
			// 着目している子ノード current は要素か
			if(current.getNodeType() == Node.ELEMENT_NODE) {
				// 要素なら要素名をチェック
				String nodeName = current.getNodeName();
				if(nodeName.equals("item") || nodeName.equals("CompleteSuggestion")) { // item または entry 要素を発見
					// item 要素または CompleteSuggestion 要素から Item オブジェクトを生成しリストに追加


					NamedNodeMap attrs = current.getFirstChild().getAttributes();
					Node attr = attrs.getNamedItem("data");
					//System.out.println(attr.getNodeValue());
					itemList.add(attr.getNodeValue());
				}
				//System.out.println(itemList);
				//else
				// item 要素または entry 要素でなければ、さらにその要素の子ノードから探す
				// (channel, items など)
				//findItems(current); // 再帰呼び出し
			}
		}
		return itemList;
	}
}
