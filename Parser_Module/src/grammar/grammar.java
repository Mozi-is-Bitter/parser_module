package grammar;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class grammar {
	private static final String path = "resource\\grammar.txt";
	private static HashMap<String,ArrayList<String>> Inverse_grammar_table = new HashMap<String, ArrayList<String>>();
	
	public boolean Is_there_grammar(String g1)
	{
		String g12 = g1 + " ";
		return this.Inverse_grammar_table.containsKey(g12);
	}
	public boolean Is_there_grammar(String g1, String g2)
	{
		String g12 = g1 + " " + g2 + " ";
		return this.Inverse_grammar_table.containsKey(g12);
	}
	//역 문법 테이블 출력
	public void Print_Inverse_grammr_table()
	{
		for(String key : Inverse_grammar_table.keySet())
		{
			System.out.println(key);
			System.out.println(Inverse_grammar_table.get(key));
		}
	}
	public ArrayList<String> Index_Inverse_grammar_table(String g1)
	{
		String g12 = g1 + " ";
		return Inverse_grammar_table.get(g12);
	}
	public ArrayList<String> Index_Inverse_grammar_table(String g1,String g2)
	{
		String g12 = g1 + " " + g2 + " ";
		return Inverse_grammar_table.get(g12);
	}
	//역 문법 테이블에 문법 추가
	public String Get_grammar(String token, int index)
	{
		String grammar = Inverse_grammar_table.get(token).get(index);
		
		return grammar +" -> "+token;
	}
	
	private static void Set_Inverse_grammr_table(String conditional, String[] consequence)
	{
		String temp = new String();
		
		for(int i = 0;i<consequence.length;i++)
		{	
			temp += consequence[i];
			temp += " ";
		}
		ArrayList<String> temp_list = Inverse_grammar_table.get(temp);
		
		if(temp_list == null)
			temp_list = new ArrayList<String>();
		temp_list.add(conditional);
		Inverse_grammar_table.put(temp, temp_list);
	}
	
	//문법 읽는 모듈
	private static void read_grammar()
	{
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String grammar_str;
			while((grammar_str = br.readLine())!=null)
			{
				String[] str_list = grammar_str.split(" -> ");
				String[] consequence = str_list[1].split(" ");
				String conditional = str_list[0];
				
				Set_Inverse_grammr_table(conditional, consequence);
				
				//System.out.println(consequence[0] +" "+ inverse_grammar_table.get(consequence));
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//문법 객체!!! 생성
	public grammar()
	{
		System.out.println("read grammar file!!");

		read_grammar();
		System.out.println("loading complete!!");
	}
	
}
