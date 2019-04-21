package twitterApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TwitterGUI extends JFrame{
	JTextField text1;
	JTextArea area1 = new JTextArea();
	JButton button2;
	JCheckBox[] ckbox;
	JComboBox tweetCombo;

	String query;
	String query2 = "aaaaa";
	Feed feed = new Feed();
	int i = 0;

	TwitterView view = new TwitterView();

	public static void main(String[] args) {
		TwitterGUI frame = new TwitterGUI();
		frame.setVisible(true);
	}

	TwitterGUI(){
		setSize(350, 470);
		setTitle("searchWord");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		text1 = new JTextField("検索したい単語を入力してください", 20);
		area1 = new JTextArea(14, 22);
		area1.setEditable(false);

		String[] tweetNumber = {"1","5","10","30","50","80","90","100"};
		tweetCombo = new JComboBox(tweetNumber);
		tweetCombo.setPreferredSize(new Dimension(80, 30));

		ckbox = new JCheckBox[7];
		ckbox[0] = new JCheckBox("リツイート除外");
		ckbox[1] = new JCheckBox("リツイート0を含まない");
		ckbox[2] = new JCheckBox("いいね0を含まない");
		ckbox[3] = new JCheckBox("ポジティブのみ");
		ckbox[4] = new JCheckBox("ネガティブのみ");
		ckbox[5] = new JCheckBox("独自ソートをかける                             ");
		ckbox[6] = new JCheckBox("日本語ツイートのみ");

		ckbox[3].addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if (ckbox[3].isSelected()){
							ckbox[1].setSelected(false);
							ckbox[2].setSelected(false);
							ckbox[4].setSelected(false);
						}
					}
				});
		ckbox[4].addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if (ckbox[4].isSelected()){
							ckbox[1].setSelected(false);
							ckbox[2].setSelected(false);
							ckbox[3].setSelected(false);
						}
					}
				});

		JButton button = new JButton("検索");
		button.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						area1.setText("");
						i = 0;
						query = text1.getText();
						feed.setURL("http://www.google.com/complete/search?hl=en&output=toolbar&q=" + query);
						// feed.setEncoding("Shift_JIS");		// UTF-8 以外の場合は指定
						feed.run();
						ArrayList<String> itemList = feed.getItemList();

						area1.append("'" + query + "'" + "の予測検索結果\n");
						for(String item: itemList) {
							i++;
							System.out.println(item);	// toString() が呼び出される
							String a = item + "\n";
							area1.append( i + ", " + a);
						}
					}
				});

		button2 = new JButton("Twitter検索");
		button2.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						JLabel label = new JLabel("検索する単語を選択してください");
						label.setForeground(Color.RED);
						String b = "";
						String c = "";
						String d = "";
						String e = "";
						String f = "";
						String g = "";
						String h = "";
						boolean sort = false;
						if (event.getSource() == button2) {
							query2 = area1.getSelectedText();
							view.area.setText("");
							System.out.println("ok");
							System.out.println(query2);
							if(!query2.equals("")) {
								System.out.println("選択した単語は" + query2);
								view.setVisible(true);
								//view.search(query2);
								if (ckbox[0].isSelected()){
									b = " exclude:retweets";
								}
								if (ckbox[1].isSelected()){
									c = " min_retweets:1";
								}
								if (ckbox[2].isSelected()){
									d = " min_faves:1";
								}
								if (ckbox[3].isSelected()){
									e = " :)";
								}
								if (ckbox[4].isSelected()){
									f = " :(";
								}
								if (ckbox[5].isSelected()){
									sort = true;
								}
								if (ckbox[6].isSelected()){
									g = " lang:ja";
								}
								if (tweetCombo.getSelectedIndex() == -1){
									h = "(not select)";
								}else{
									h = (String)tweetCombo.getSelectedItem();
								}
								view.search(query2, b, c, d, e, f, g, h, sort);
							}else if(query2.equals("")){
								//JOptionPane.showMessageDialog(p, label);
							}
						}
					}
				});

		p.add(text1);
		p.add(button);
		p.add(area1);
		p2.add(ckbox[0]);
		p2.add(ckbox[1]);
		p2.add(ckbox[2]);
		p2.add(ckbox[6]);
		p2.add(ckbox[3]);
		p2.add(ckbox[4]);
		p2.add(ckbox[5]);
		p2.add(new JLabel("検索ツイート数"));
		p2.add(tweetCombo);
		p3.add(button2);

		Container contentPane = getContentPane();
		Container contentPane2 = getContentPane();
		Container contentPane3 = getContentPane();
		contentPane.add(p, BorderLayout.PAGE_START);
		contentPane2.add(p2, BorderLayout.CENTER);
		contentPane3.add(p3, BorderLayout.PAGE_END);
	}

	//	public void actionPerformed(ActionEvent e){
	//		JLabel label = new JLabel("検索する単語を選択してください");
	//		label.setForeground(Color.RED);
	//		if (e.getSource() == button2) {
	//			query2 = area1.getSelectedText();
	//			System.out.println("ok");
	//			System.out.println(query2);
	//			if(!query2.equals("")) {
	//				System.out.println("選択した単語は" + query2);
	//				view.setVisible(true);
	//			}else if(query2.equals("")){
	//				JOptionPane.showMessageDialog(this, label);
	//			}
	//		}
	//	}
}