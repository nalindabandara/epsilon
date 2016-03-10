package com.epsilon.LeakHawk.test;

import java.util.regex.*;

	public class RegEx{
		
		public static void main(String[] args)
		  {
		    String txt="ad453@min@pa@nd.ora.lk";

		    String re1="((?:[a-z][a-z]+))";	// Word 1
		    String re2="(@)";	// Any Single Character 1
		    String re3="((?:[a-z][a-z]+))";	// Word 2
		    String re4="(\\.)";	// Any Single Character 2
		    String re5="((?:[a-z][a-z]+))";	// Word 3

		    Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		    Matcher m = p.matcher(txt);
		    if (m.find())
		    {
		        String word1=m.group(1);
		        String c1=m.group(2);
		        String word2=m.group(3);
		        String c2=m.group(4);
		        String word3=m.group(5);
		        System.out.print("("+word1.toString()+")"+"("+c1.toString()+")"+"("+word2.toString()+")"+"("+c2.toString()+")"+"("+word3.toString()+")"+"\n");
		    }
		  }

}
