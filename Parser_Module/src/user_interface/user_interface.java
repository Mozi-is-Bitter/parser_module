package user_interface;
import grammar.grammar;
import CFG_grammar.CFG_grammar;
import CKY_Parser.cky_parser;
import Earley_Tomita.Earley_Tomita;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class user_interface {
	private static final String input_path = "input.txt";
	private static ArrayList<String> input_strings = new ArrayList<String>();
	public static void read_input_file() {
		try {
			FileReader fr = new FileReader(input_path);
			BufferedReader br = new BufferedReader(fr);
			String str;
			while((str = br.readLine())!= null)
			{
				input_strings.add(str);
				//System.out.println(str);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CFG_grammar grm = new CFG_grammar();
		//grm.Print_grammar_table();
		//grm.Print_pos_table();
		read_input_file();
		for(int i = 0;i<input_strings.size();i++)
		{
			Earley_Tomita parser = new Earley_Tomita(input_strings.get(i),grm);
		}
	}

}
