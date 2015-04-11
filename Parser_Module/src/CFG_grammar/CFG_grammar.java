package CFG_grammar;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class CFG_grammar {
	private static final String path = "resource\\grammar.txt";
	private static HashMap<String,ArrayList<ArrayList<String>>> grammar_table;
	private static HashMap<String,ArrayList<ArrayList<String>>> pos_table;
	private static void Set_grammar_table(String conditional, String[] consequence)
	{
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<ArrayList<String>> temp1 = grammar_table.get(conditional);
		if(temp1 == null)
			temp1 = new ArrayList<ArrayList<String>>();
		
		
		for(int i=0;i<consequence.length;i++)
			temp.add(consequence[i]);
		temp1.add(temp);
		grammar_table.put(conditional,temp1);
	}
	private static void Set_pos_table(String conditional, String[] consequence)
	{
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<ArrayList<String>> temp1 = pos_table.get(consequence[0]);
		if(temp1 == null)
			temp1 = new ArrayList<ArrayList<String>>();
		temp.add(conditional);
		temp1.add(temp);
		
		pos_table.put(consequence[0],temp1);
	}
	public ArrayList<ArrayList<String>> Get_pos_table_index(String key)
	{
		return pos_table.get(key);
	}
	public ArrayList<ArrayList<String>> Get_grammar_table_index(String key)
	{
		return grammar_table.get(key);
	}
	
	public void Print_grammar_table()
	{
		for(String key : grammar_table.keySet())
		{
			ArrayList<ArrayList<String>> temp = grammar_table.get(key);
			System.out.println(key);
			for(int i = 0;i<temp.size();i++)
			{
				System.out.println(temp.get(i));
			}
			
		}
	}
	public void Print_pos_table()
	{
		for(String key : pos_table.keySet())
		{
			ArrayList<ArrayList<String>> temp = pos_table.get(key);
			System.out.println(key);
			for(int i = 0;i<temp.size();i++)
			{
				System.out.println(temp.get(i));
			}
			
		}
	}
	public CFG_grammar()
	{
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			grammar_table = new HashMap<String, ArrayList<ArrayList<String>>>();
			pos_table = new HashMap<String, ArrayList<ArrayList<String>>>();
			String grammar_str;
			while((grammar_str = br.readLine())!=null)
			{
				if(grammar_str.isEmpty())
					break;
				String[] str_list = grammar_str.split(" -> ");
				String[] consequence = str_list[1].split(" ");
				String conditional = str_list[0];
				
				Set_grammar_table(conditional, consequence);
				
				//System.out.println(consequence[0] +" "+ inverse_grammar_table.get(consequence));
			}
			while((grammar_str = br.readLine())!=null)
			{
				String[] str_list = grammar_str.split(" -> ");
				String[] consequence = str_list[1].split(" ");
				String conditional = str_list[0];
				//Set_grammar_table(conditional,consequence);
				Set_pos_table(conditional, consequence);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
