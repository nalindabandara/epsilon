package com.epsilon.LeakHawk.test;
import java.applet.Applet;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import java.lang.Integer;
import java.util.regex.Pattern;
import java.awt.Color;


public class Text extends Applet
{
	String newln = new String(new char[] {10});
	public boolean DEBUGGING = true;
	
	TextArea t = new TextArea("The Office for Mathematics, Science, and Technology Education (MSTE) is a unit within the Department of Curriculum and Instruction in the College of Education at the University of Illinois at Urbana-Champaign. The goal of MSTE is to serve as a model-builder for innovative, standards-based, technology-intensive mathematics and science instruction at the K-16 levels. The Office serves as a campus-wide catalyst for integrative teaching and learning in mathematics, science, and technology education.",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
	TextArea r = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
	Button b = new Button("Analyze");


	public void init() {
		setLayout(null);
		this.setBackground(Color.white);
		this.add(t);
		this.add(r);
		this.add(b);
		t.setBounds(0,0,500,200);
		r.setBounds(0,200,500,200);
		b.setBounds(0,400,80,40);
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				r.setText("");
				parse();
				r.setCaretPosition(0);
			}
		});
	}

	public void parse()
	{
		int dots = 0,commas = 0,vowels = 0,consonants = 0,dot_and_space=0,ws=0,spaces=0;
		char store;
		char empt = 0;
		String dummy = t.getText();
		dummy = dummy.toLowerCase();
		boolean last_dot=false;
		boolean space_after = false;	
		
		Vector letters = new Vector(0,1);
		
		for(int c = 0;c < 26;c++)
			letters.add (new Integer(0));
		
		String[] sent0;
		String[][] sent1;
		String[][][] sent2;
		sent0 = split (dummy,'.');
		
		sent1 = new String[sent0.length][];
		for(int c = 0;c < sent0.length;c++)
			sent1[c] = split (sent0[c],'!');
				
		sent2 = new String[sent0.length][sent1.length][];
		for(int j = 0;j < sent0.length;j++)
			for(int k = 0;k < sent1[j].length;k++)
				sent2[j][k] = split (sent1[j][k],(char)63);
				
		int num = 0;
		for(int j = 0;j < sent2.length;j++)
			for(int k = 0;k < sent2[j].length;k++)
				if (sent2[j][k] != null)
					for(int l = 0;l < sent2[j][k].length;l++)
						if (haveSpaces(sent2[j][k][l]))
							num++;

		dot_and_space = num;		
		
		for(int c = 0;c < dummy.length ();c++)
		{
			store = dummy.charAt (c);
			if (store == ',')
			{
				commas++;
				space_after = true;
			}
			if (store > 96 && store < 123)
			{
				letters.setElementAt (new Integer(((Integer)letters.elementAt (store-97)).intValue () + 1),store-97);
				if (store == 'a' || store == 'e' || store == 'i' || store == 'o' || store == 'u')
					vowels++;
				else
					consonants++;
			}
			if (store == '.')
				dots++;
			if (store == ' ')
				spaces++;
		}

		for(char c = 0;c < 39;c++)
			dummy = dummy.replace (c,' ');
		for(char c = 40;c < 45;c++)
			dummy = dummy.replace (c,' ');
		for(char c = 46;c < 48;c++)
			dummy = dummy.replace (c,' ');
		for(char c = 58;c < 65;c++)
			dummy = dummy.replace (c,' ');
		for(char c = 91;c < 97;c++)
			dummy = dummy.replace (c,' ');
		for(char c = 123;c < 256;c++)
			dummy = dummy.replace (c,' ');

		String[] ar = dummy.split(" ");
		ar = sort(ar);
		
		Vector words = new Vector(0,1);
		Vector frequencies = new Vector(0,1);
		int loc = -1;
		for (int c = 0;c < ar.length;c++)
		{
			if (loc != -1)
			{
				if (!((String)(words.elementAt (loc))).equals (ar[c]))
				{
					words.addElement (ar[c]);
					frequencies.add (new Integer(1));
					loc++;
				}
				else
				{
					frequencies.set (loc,new Integer(((Integer)frequencies.elementAt (loc)).intValue () + 1));
				}
			}
			else
			{
				words.addElement (ar[c]);
				frequencies.add (new Integer(1));
				loc = 0;
			}
		}
		
		String s;
		
		for(int c = 0;c < frequencies.capacity ();c++)
		{
			if (((String)words.elementAt (c)).length () != 0)
				ws = ((Integer)(frequencies.elementAt (c))).intValue () + ws;
		}
		
		r.append ("# of periods: " + dots);
		r.append (newln + "# of potential sentences: " + dot_and_space);
		r.append (newln + "# of commas: " + commas);
		r.append (newln + "# of vowels: " + vowels);
		r.append (newln + "# of consonants: " + consonants);
		r.append (newln + "# of letters: " + (consonants + vowels));
		r.append (newln + "# of letters with spaces: " + (consonants + vowels + spaces));
		r.append (newln + "# of words: " + ws);
		r.append (newln + "# of letters per word: " + (double)(consonants + vowels) / (double)ws);
		r.append (newln + "# of words per potential sentence: " + (double)ws / (double)dot_and_space);

		r.append (newln + "--------------------");

		for(int c = 0;c < 26;c++)
			r.append (newln + (char)(c + 97) + " " + ((Integer)letters.elementAt (c)));

		r.append (newln + "--------------------");
		
		for(int c = 0;c < words.capacity ();c++)
		{
			s = (String)words.elementAt (c);
			if (s.length () != 0)
				r.append(newln + s + " " + (Integer)frequencies.elementAt (c));
		}
	}
	
	public String[] split(String s, char ch)
	{
		Vector a = new Vector(0,1);
		String dummy = "";
		for(int c = 0;c < s.length();c++)
			if (s.charAt(c) == ch)
			{
				if (dummy.length() > 0)
					a.add(dummy);
				dummy = "";
			}
			else
				dummy = dummy + s.charAt(c);
		a.add(dummy);
		String[] ret = new String[a.capacity()];
		for(int c = 0;c < a.capacity();c++)
			ret[c] = (String)(a.elementAt(c));
		return ret;
	}
	
	public boolean haveSpaces(String s)
	{
		if (s.indexOf(" ",1) == -1)
			return false;
		return true;
	}
	
	public String[] sort(String[] ar)
	{
		String temp;
		int j=0,k=0;
		for(j = 0;j < ar.length - 1;j++)
			for(k = j + 1;k < ar.length;k++)
				if (ar[j].compareTo(ar[k]) > 0)
				{
					temp = ar[j];
					ar[j] = ar[k];
					ar[k] = temp;
				}
		return ar;
	}

	public void paint(Graphics g) {
	}
public void Debug(String s)
{
if (DEBUGGING)
	System.out.println(s);
}
public void Debug(int s)
{
if (DEBUGGING)
	System.out.println("" + s);
}
public void Debug(double s)
{
if (DEBUGGING)
	System.out.println("" + s);
}
public void Debug()
{
if (DEBUGGING)
	System.out.println("");
}
}