package CKY_Parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import grammar.grammar;
//차트의 요소
class Chart_element
{
	ArrayList<String> element;
	public void Print_chart_element()
	{
		for(int i = 0;i<element.size();i++)
			System.out.println(element.get(i));
	}
	public Chart_element() {
		// TODO Auto-generated constructor stub
		element = new ArrayList<String>();
	}
}
public class cky_parser {
	
	//input string splited by white space 
	String[] input_tokens;
	Chart_element cky_chart[][];
	FileWriter fw;
	BufferedWriter bw;
	//스스로 확장
	private void Self_expansion(String token, grammar grm, int x, int y,String aso) throws IOException
	{
		if(!grm.Is_there_grammar(token))
			return ;
		
		ArrayList<String> temp = grm.Index_Inverse_grammar_table(token);
		if(temp == null)
			return ;
		for(int i = 0;i<temp.size();i++)
		{
			String str;
			bw.write(temp.get(i) + " -> "+token+"\n");
			//System.out.println(temp.get(i) + " -> "+token);
			
			str = "(" + temp.get(i) + " "+ aso + ")"; 
			
			cky_chart[x][y].element.add(str);
			Self_expansion(temp.get(i), grm, x, y,str);
		}
	}
	private String Get_grammar_from_parse_tree(String parse_tree)
	{
		int first = 0,end = 0;
		int i;
		for(i = 0;i<parse_tree.length();i++)
		{
			if(parse_tree.charAt(i)!='(')
			{
				first = i;
				break;
			}
		}
		for(;i<parse_tree.length();i++)
		{
			if(parse_tree.charAt(i) == ' ')
			{	
				end = i;
				break;
			}
		}
		return parse_tree.substring(first, end);
	}
	//2개 문법 확장 함수
	private void Double_expansion(int x,int y,int k,grammar grm) throws IOException
	{
		int i = x - k,j = x  - k,l;
		
		ArrayList<String> temp1 = cky_chart[i][y].element;
		ArrayList<String> temp2 = cky_chart[x][j].element;
		
		for(i = 0;i<temp1.size();i++)
		{
			String g1 = Get_grammar_from_parse_tree(temp1.get(i));
			for(j = 0;j<temp2.size();j++)
			{
				String g2 = Get_grammar_from_parse_tree(temp2.get(j));
				if(grm.Is_there_grammar(g1, g2))
				{
					ArrayList<String> grammar_list = grm.Index_Inverse_grammar_table(g1, g2);
					for(l = 0;l<grammar_list.size();l++)
					{
						String g3 = grammar_list.get(l);
						String str;
						bw.write(g3 +" -> " + g1 + " " + g2+"\n");
						
						
						str = "("+g3 + " " +temp1.get(i)+temp2.get(j)+")";
						cky_chart[x][y].element.add(str);
						Self_expansion(g3, grm, x, y, str);
					}
				}
			}
		}
	}
	//파싱
	private void parse(grammar grm) throws IOException
	{
		int i,j,k;
		int input_tokens_length = input_tokens.length + 1;
		
		for(i = 1;i<input_tokens_length;i++)
		{
			//형태소 분석 결과 적용 및 초기화
			String token = input_tokens[i-1];
			
			Self_expansion(token, grm, i, i-1,token);
			
			for(j = i - 2;j>=0;j--)
			{
				//2개의 문법으로 확장 Np -> N V
				for(k = 1; k < i - j;k++)
				{
					Double_expansion(i, j, k,grm);
				}
				
			}
			
		}
	}
	
	//파서 초기화
	public cky_parser(String str,grammar grm) throws IOException {
		// TODO Auto-generated constructor stub
		input_tokens = str.split(" ");
		cky_chart = new Chart_element[input_tokens.length+1][input_tokens.length];
		for(int i = 0;i<input_tokens.length+1;i++)
			for(int j = 0;j<input_tokens.length;j++)
			{
				cky_chart[i][j] = new Chart_element();
			}
		fw = new FileWriter("output.txt");
		bw = new BufferedWriter(fw);
		System.out.println("ready for parsing");
		
		this.parse(grm);
		
		System.out.println("parse compelete!!");
		try {
			for(int k = 0;k<cky_chart[input_tokens.length][0].element.size();k++)
				bw.write(cky_chart[input_tokens.length][0].element.get(k)+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bw.close();
		fw.close();
	}

	//파싱 결과 출력!!!
	public void Print_cky_parser()
	{
		cky_chart[5][0].Print_chart_element();
		return ;
	}

}
