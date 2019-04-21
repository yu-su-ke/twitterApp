package twitterApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterView extends JFrame{
	JTextArea area = new JTextArea();
	ArrayList<Twe> list = new ArrayList<Twe>();
	ArrayList<TweNormal> list2 = new ArrayList<TweNormal>();
	int j = 1;
	int k = 1;

	TwitterView(){
		setBounds(350, 0, 700, 750);
		addWindowListener(new MyWindowAdapter());
		setTitle("tweetView");
		setLayout(new BorderLayout());

		JPanel p = new JPanel();
		area = new JTextArea(41, 55);
		JScrollPane scrollpane = new JScrollPane(area);
		area.setEditable(false);

		p.add(scrollpane);

		Container contentPane = getContentPane();
		contentPane.add(p, BorderLayout.CENTER);
	}

	public void search(String a, String b, String c, String d, String e, String f, String g, String h, boolean sort) {
		//search(検索語、リツイート除外、リツイート0、いいね0、検索ツイート数、ソート)
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			System.out.println();
			Query query2 = new Query();
			query2.setCount(Integer.parseInt(h));
			System.out.println();
			System.out.println();
			System.out.println("'" + a + "'" + "の検索結果");

			//twitter検索部
			query2.setQuery(a + b + c + d + e + f + g);
			System.out.println(a + b + c + d + e + f + g);
			QueryResult result;
			//do {
			result = twitter.search(query2);
			System.out.println(result);
			List<Status> tweets = result.getTweets();
			for (Status tweet : tweets) {
				double point = tweet.getFavoriteCount() * 0.8 + tweet.getRetweetCount() * 1.2;
				String changeP = String.format("%.2f", point);

				if(sort == true) {
					list.add(new Twe(tweet, point));
				}else if(sort == false) {
					list2.add(new TweNormal(tweet));
				}
			}
			Collections.sort(list, new TweComparator());
			Collections.reverse(list);
			area.append("Twitter検索ワード\r\n");
			area.append(a + b + c + d + e + f + g);
			area.append("\r\n\r\n");
			if(sort == true) {
				for (Twe twe : list) {
					String changeP = String.format("%.2f", twe.getPoint());
					String strText = twe.getTwe().getText();
					strText = strText.replaceAll("\r\n"," ");
					strText = strText.replaceAll("\r"," ");
					strText = strText.replaceAll("\n"," ");

					area.append(""+ "\r\n" + String.valueOf(j) + "位: ");
					k++;
					j++;
					area.append("@" + twe.getTwe().getUser().getScreenName() + " - \r\n"
							+ strText + "\r\n\"" + "いいね数：" + twe.getTwe().getFavoriteCount() + "  RT数：" + twe.getTwe().getRetweetCount() + "  総合Point：" + changeP + "\r\n");
				}
			}else if(sort == false) {
				for (TweNormal twe : list2) {
					String strText = twe.getTwe().getText();
					strText = strText.replaceAll("\r\n"," ");
					strText = strText.replaceAll("\r"," ");
					strText = strText.replaceAll("\n"," ");

					area.append(""+ "\r\n" + String.valueOf(j) + ": ");
					j++;
					area.append("@" + twe.getTwe().getUser().getScreenName() + " - \r\n"
							+ strText + "\r\n\"" + "いいね数：" + twe.getTwe().getFavoriteCount() + "  RT数：" + twe.getTwe().getRetweetCount() + "\r\n");
				}
			}
			list.clear();
			list2.clear();
			j = 1;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}



		//area.append(a);
		System.out.println(a);
	}

	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}

class Twe{
	private Status twe;
	private double point;

	public Twe(Status twe, double point) {
		this.twe = twe;
		this.point = point;
	}
	public Status getTwe() {
		return twe;
	}
	public double getPoint() {
		return point;
	}
}

class TweNormal{
	private Status twe;

	public TweNormal(Status twe) {
		this.twe = twe;
	}
	public Status getTwe() {
		return twe;
	}
}

class TweComparator implements Comparator<Twe> {
	public int compare(Twe twe1, Twe twe2) {
		return Double.compare(twe1.getPoint(), twe2.getPoint());
	}
}
