package Earley_Tomita;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import CFG_grammar.CFG_grammar;


class Chart{
	int start;
	int end;
	String cond;
	ArrayList<String> foundList;
	ArrayList<String> findingList;
	int dotted_point;
	int status;
	public void PrintChart()
	{
		System.out.println("["+start+","+end+","+cond+","+foundList+","+findingList+"]");
	}
	Chart(int start,int end, String cond,ArrayList<String> foundList,ArrayList<String> findingList)
	{
		this.start = start;
		this.end = end;
		this.cond = cond;
		this.foundList = foundList;
		this.findingList = findingList;
		this.status = 0;
	}
	private boolean Strcmp(String a, String b)
	{
		boolean result = (a == b);
		return result;
	}
	private <E> boolean ArrayListCmp(ArrayList<E> a, ArrayList<E>b)
	{
		if(a.size() == b.size())
		{
			for(int i = a.size()-1; i >= 0; i--)
			{
				if(a.get(i)!=b.get(i))
					return false;
			}
		}
		else 
			return false;
		return true;
	}
	public boolean Chartcmp(Chart a)
	{
		if(this.start != a.start)
			return false;
		else if(this.end != a.end)
			return false;
		else if(!Strcmp(this.cond, a.cond))
			return false;
		else if(!ArrayListCmp(this.foundList, a.foundList))
			return false;
		else if(!ArrayListCmp(this.findingList, a.findingList))
			return false;
		else if(this.status != a.status)
			return false;
		else
			return true;
	}
	
}
class Parsing_Tree_elem{
	String cond;//tree의 condition
	ArrayList<String> conseq;//tree의 consequence
	boolean status;//tree의 자식 node가 모두 만들어 졌는가?
	Parsing_Tree_elem[] child;
	Parsing_Tree_elem parent;
	public Parsing_Tree_elem(Chart elem, Parsing_Tree_elem par) {
		// TODO Auto-generated constructor stub
		status = false;
		cond = elem.cond;
		conseq = elem.foundList;
		child = new Parsing_Tree_elem[elem.foundList.size()];
		parent = par;
	}
	public boolean Status()
	{
		for(int i = 0;i<child.length;i++)
			if(child[i]==null)
				return true;
		return false;
	}
	
}
class Parsing_Tree{
	private Parsing_Tree_elem root;

	public Parsing_Tree_elem Set_Tree(Chart elem,Parsing_Tree_elem cur)
	{
		if(cur==null)//현재 트리의 노드가 꽉 찬 경우
		{
			return null;
		}
		if(!cur.Status())
		{
			cur = cur.parent;
			return Set_Tree(elem, cur);
		}
		for(int i = 0;i<cur.conseq.size();i++)
		{
			if(elem.cond.equals(cur.conseq.get(i)))
			{
				cur.child[i] = new Parsing_Tree_elem(elem, cur);
				
				cur = cur.child[i];
				return cur;
			}
		}
		cur = cur.parent;
		return Set_Tree(elem, cur);
		
	}
	public Parsing_Tree(Chart elem) {
		// TODO Auto-generated constructor stub
		this.root = new Parsing_Tree_elem(elem,null);
	}
	public Parsing_Tree_elem Get_root_node()
	{
		return this.root;
	}
	public String Print_Sub_Tree(Parsing_Tree_elem cur_node)
	{
		if(cur_node == null)
			return "";
		String str = new String();
		str = "(";
		str += cur_node.cond;
		for(int i = 0;i<cur_node.child.length;i++)
		{
			if(cur_node.child[i]==null)
			{
				str += " "+cur_node.conseq.get(i);
			}
			str += Print_Sub_Tree(cur_node.child[i]);
		}
		str += ")";
		return str;
	}
}
public class Earley_Tomita {
	private Queue<Chart> completeChart;
	private Queue<Chart> pendingChart;
	private String[] input_token;
	private Stack<Chart> completed;
	private Parsing_Tree pst;
	private static final int Active = 1;
	private static final int Complete = 0;
	private static final int pending = 2;
	private int dot;
	private Chart Dequeue(Queue<Chart> queue)
	{
		return queue.remove();
	}
	private void EnqueuependingChart(Chart temp)
	{
		temp.status = pending;
		pendingChart.add(temp);
	}
	private void EnqueuecompleteChart(Chart temp)
	{
		temp.status = Complete;
		completeChart.add(temp);
	}
	private void Print_dotted()
	{
		String temp=new String();
		for(int i = 0;i<input_token.length;i++)
		{
			if(i == dot)
			{
				temp += "+";
			}
			temp += input_token[i]+" ";
		}
		if(dot == input_token.length)
			temp += "+";
		System.out.println(temp);
	}
	private void EnqueueActiveChart(Chart temp)
	{
		temp.status = Active;
		for(Chart activeEdge : completeChart)
		{
			if(temp.Chartcmp(activeEdge))
				return ;
		}
		
		
		completeChart.add(temp);
	}
	//이전에 찾은 적이 없을 경우 condition에서 파생되는 모든 문법을 pending chart에 넣는다.
	private void Predictor(Chart e1,CFG_grammar grm)
	{
		String g1 = e1.findingList.get(0);
		//e1.PrintChart();
		if(grm.Get_grammar_table_index(g1)!=null)
			for(ArrayList<String> grammarList : grm.Get_grammar_table_index(g1))
			{
				Chart newChart = new Chart(e1.end, e1.end, g1, new ArrayList<String>(), grammarList);
				//newChart.PrintChart();
				EnqueuependingChart(newChart);
			}
		EnqueueActiveChart(e1);
	}
	//complete 형태가 들어왔을 경우 이를 상위의 버젼과 합치는 작업.
	private void Completor(Chart e1)
	{
		for(Chart t : completeChart)
		{
			if(t.status == Active)
			{
				if((t.end == e1.start)&&Strcmp(e1.cond, t.findingList.get(0)))
				{
					
					ArrayList<String> newFoundList = ListCopy(t.foundList);
					ArrayList<String> newFindingList = ListCopy(t.findingList);
					newFoundList.add(newFindingList.get(0));
					newFindingList.remove(0);
					Chart newPendingEdge = new Chart(t.start, e1.end, t.cond,newFoundList, newFindingList);
					EnqueuependingChart(newPendingEdge);
				}
			}
		}
		e1.PrintChart();
		completed.push(e1);
		EnqueuecompleteChart(e1);
	}
	private boolean Strcmp(String a, String b)
	{
		boolean result = (a.equals(b));
		return result;
	}
	private ArrayList<String> ListCopy(ArrayList<String> a)
	{
		ArrayList<String> b = new ArrayList<String>();
		for(String str : a)
			b.add(str);
		return b;
	}
	private boolean ScanningPendingChart(Chart e1)
	{
		for(Chart tmp : pendingChart)
		{
			if((e1.end == tmp.start)&&(Strcmp(tmp.cond,e1.findingList.get(0))))
				return true;
		}
		return false;
	}
	private void process2(Chart e1, Chart e2)
	{
		ArrayList<String> newFoundList = ListCopy(e1.foundList);
		ArrayList<String> newFindingList = ListCopy(e1.findingList);
		newFoundList.add(newFindingList.get(0));
		newFindingList.remove(0);
		//e2.PrintChart();
		completed.push(e2);
		//e1.PrintChart();
		Chart newChart = new Chart(e1.start,e2.end, e1.cond, newFoundList, newFindingList);
		EnqueuependingChart(newChart);
	}
	
	private boolean ScanningCompleteChart(Chart e1)
	{
		for(Chart tmp : completeChart)
		{
			if((e1.end == tmp.start)&&(Strcmp(tmp.cond,e1.findingList.get(0))))
			{
				if((tmp.status == Complete))
				{
					process2(e1, tmp);
				}
			}
		}
		return false;
	}
	public void MakeParsingTree()
	{
		System.out.println("\n\ncompleted\n\n");
		for(Chart tmp : completed)
			tmp.PrintChart();
		Chart root = completed.pop();
		pst = new Parsing_Tree(root);
		Parsing_Tree_elem elem = pst.Get_root_node();
		while(completed.size() > 0)
		{
			Chart edge = completed.pop();
			Parsing_Tree_elem tmp = pst.Set_Tree(edge,elem);
			if(tmp != null)
				elem = tmp;
		}
		
	}
	//파싱 알고리즘이 실질적으로 수행.
	private void ET_parsing(String[] input_token,CFG_grammar grm)
	{
		while(pendingChart.size()>0)
		{
			Chart e1 = Dequeue(pendingChart);
			if(dot != e1.end)
			{
				dot = e1.end;
				Print_dotted();
			}
			e1.PrintChart();
			if(e1.findingList.size()==0)
			{
				Completor(e1);
			}
			else
			{
				if(ScanningCompleteChart(e1)||ScanningPendingChart(e1))
				{
					EnqueueActiveChart(e1);
				}
				else
				{
					Predictor(e1, grm);
				}
				
			}
		}
	}
	public Earley_Tomita(String str, CFG_grammar grm)
	{
		//각 QUEUE 초기화
		completeChart = new LinkedList<Chart>();
		pendingChart = new LinkedList<Chart>();
		completed = new Stack<Chart>();
		//active edge 초기화
		//active_edges = new ArrayList<Chart>();
		//tokenizing
		String[] input_token = str.split(" ");
		dot = -1;
		this.input_token = input_token;
		//초기화(Pending Chart)
		System.out.println("initialize root of parsing tree");
		for(ArrayList<String> temp : grm.Get_grammar_table_index("S"))
		{
			Chart init_chart = new Chart(0, 0, "S",new ArrayList<String>(),temp);
			EnqueuependingChart(init_chart);
			
			init_chart.PrintChart();
		}
		//초기화(Complete Chart)
		System.out.println("initialize Pos");
		for(int i = 0;i<input_token.length;i++)
		{
			for(ArrayList<String> temp : grm.Get_pos_table_index(input_token[i]))
			{
				String cond = temp.get(0);
				ArrayList<String> token = new ArrayList<String>();
				token.add(input_token[i]);
				Chart init_chart = new Chart(i,i+1,cond, token,new ArrayList<String>());
				
				EnqueuecompleteChart(init_chart);
				init_chart.PrintChart();
			}
		}
		System.out.println("\nparsing!!!\n");
		ET_parsing(input_token,grm);
		//PrintCompleteChart();
		MakeParsingTree();
		System.out.println(pst.Print_Sub_Tree(pst.Get_root_node()));
	}
}
