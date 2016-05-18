package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Bot;
import model.Speech;
import error.Log;
import error.LogType;

public class ChatWindow extends JFrame implements ActionListener,KeyListener,Runnable{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH=800;
	private static final int HEIGHT=600;
	private String speak;
	private JLabel lb_title;
	private JPanel header;
	private JTextPane tp_chat;
	private JScrollPane js_chat;
	private JPanel south;
	private JButton bn_send ;
	private JTextArea jta_sent_text;
	private JScrollPane js_sent_text;
	private Bot bot;
	private String question;
	private ArrayList<String> wordsToLearn;
	private JButton bn_forget;
	
	public ChatWindow() {
		properties_screen();
		init();
		properties_components();
		addingOrder();
		action();
	}
	
	private void properties_components() {
		lb_title.setFont(new Font("Felix Titling", Font.BOLD, 26));	
		header.setLayout(new FlowLayout(FlowLayout.CENTER));
		tp_chat.setForeground(new Color(153, 255, 0));
		south.setPreferredSize(new Dimension(800,100));
		bn_send.setBounds(683, 12, 97, 60);
		js_sent_text.setBounds(12, 12, 660, 76);
		bn_forget.setBounds(683, 76, 97, 24);
		bn_forget.setVisible(false);
		//botSpeak("This program was created by Hasler Choo");
	}

	private void properties_screen() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(WIDTH,HEIGHT);
		getContentPane().setLayout(new BorderLayout(0, 0));
	}

	private void init()
	{
		bot=new Bot("Cortana");
		wordsToLearn=new ArrayList<String>();
		lb_title = new JLabel("Smart Chat");
		header = new JPanel();
		tp_chat = new JTextPane();
		south = new JPanel();
		bn_send = new JButton("Enviar");
		js_chat = new JScrollPane(tp_chat);
		jta_sent_text = new JTextArea();		
		js_sent_text = new JScrollPane(jta_sent_text);
		south.add(js_sent_text);
		bn_forget = new JButton("Forget");
	}
	
	private void addingOrder()
	{
		header.add(lb_title);
		tp_chat.setEditable(false);
		south.setLayout(null);
		south.add(bn_send);
		south.add(bn_forget);
		getContentPane().add(header, BorderLayout.NORTH);	
		getContentPane().add(js_chat, BorderLayout.CENTER);
		getContentPane().add(south, BorderLayout.SOUTH);	
	}
	
	public static void addtheme()
	{
		try
		{
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
			Log.addLog(LogType.INFO,"tema inicializado");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			Log.addLog(LogType.ERROR,e.getMessage());
		}
	}
	
	private void action()
	{
		bn_send.addActionListener(this);
		bn_forget.addActionListener(this);
		jta_sent_text.addKeyListener(this);
	}
	
	public static void startWindow()
	{
		new ChatWindow().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		
		if (action.getSource()==bn_send) {
			if (bn_send.getText().toString().equalsIgnoreCase("Enviar")) {
				answer();				
			}
			else{
				if (!emptyText()) {
					wordsToLearn.add(jta_sent_text.getText().toString());
					bn_forget.setVisible(false);
				}
				learn();
				wordsToLearn.clear();
				jta_sent_text.setText("");
				bn_forget.setVisible(false);
				
			}
		}
		if (action.getSource() == bn_forget) {
			bot.setLearn(false);
			bn_forget.setVisible(false);
			bn_send.setText("Enviar");
			tp_chat.setText(tp_chat.getText()+"\n"+bot.getName()+": forgeting your question");
			botSpeak("forgeting your question");
			Log.addLog(LogType.ALERT, "was learing new word, but the user cancel");
		}
	}
	
	private void learn()
	{
		String texto=tp_chat.getText().toString();
		String [] newAnswer=new String[wordsToLearn.size()];
		String [] newQuestion={question};
		for (int i = 0; i < newAnswer.length; i++) {
			newAnswer[i]=wordsToLearn.get(i);
		}
		bot.learn(newQuestion);
		bot.learn(newAnswer);
		tp_chat.setText(texto+"\n"+bot.getName()+": Thanks for teaching me. (New word Learn)");
		botSpeak("Thanks for teaching me");
		bot.setLearn(false);
		bn_send.setText("Enviar");
	}
	
	private void answer()
	{
		if (jta_sent_text.getText().trim().equalsIgnoreCase("")) {
			return;
		}
		String question=jta_sent_text.getText().trim().toString();
		String texto=tp_chat.getText().toString()+"\n"+"Hasler Choo: "+question+"\n";
		
		tp_chat.setText(texto);
		
		String answer=bot.answer(question);
		tp_chat.setText(texto+bot.getName()+": "+answer);
		if (bot.isLearn()) {
			this.question=question;
			bn_send.setText("Learn");
			bn_forget.setVisible(true);
		}
		jta_sent_text.setText("");
		botSpeak(answer);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent action) {		
		if (action.getKeyCode()== KeyEvent.VK_ENTER) {
			if (bn_send.getText().toString().equalsIgnoreCase("Enviar")) {
				answer();				
			}
			else
			{
				if (!emptyText()) {
					wordsToLearn.add(jta_sent_text.getText().trim());
					tp_chat.setText(tp_chat.getText()+"\n"+bot.getName()+": Is there an other way to answer that question, if not press Learn Button");
					Log.addLog(LogType.ALERT,"The word: "+jta_sent_text.getText().trim()+", was learn but not saved on memory yet");
					
					botSpeak("Is there an other way to answer that question");
					jta_sent_text.setText("");					
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	private boolean emptyText()
	{
		return jta_sent_text.getText().trim().equalsIgnoreCase("");		
	}
	
	private void botSpeak(String msg)
	{
		speak=msg;
		Thread botSpeak=new Thread(this);
		botSpeak.start();
	}

	@Override
	public void run() {
		Speech.speak(speak);
	}
}
