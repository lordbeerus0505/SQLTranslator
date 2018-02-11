package com;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WhereClause {
	HashMap <String,int[]> dist=new HashMap<String, int[]>();
	ArrayList<String> list=new ArrayList<String>();
	int distance[]=new int[20];//Assuming a maximum of 20 words

	public String nesting(String sentence,String key) throws IOException {
		//Key is holding the value of clause X in WHERE X IN()
		String result="";
		boolean context=false;
		Select s=new Select(sentence);
		result=s.finalstring;
		Count s1=new Count(sentence,context);
		result+=" "+s1.finalstring;//+"h2";
		context=s1.context;
		/*Minmax s3=new Minmax(cleaned2,columnnameslist);
		res+=" "+s3.finalstring;//+"h3";*/
		//context=s3.context;
		Sum s4=new Sum(sentence,context);
		result+=" "+s4.finalstring;//+"h4";
		context=s4.context;
           /* From s5=new From(sentence);
            res+=" "+s5.finalstring;
//System.out.println(cleaned2+"cleaned");

            OrderByClause s6=new OrderByClause(cleaned2,columnnameslist);
            res+=" "+s6.finalstring;*/
		Average s5=new Average(sentence,context);
		result+=" "+s5.finalstring;
		From s6=new From(sentence);
		result+=" "+s6.finalstring;
		/*OrderByClause s7=new OrderByClause(cleaned2,columnnameslist);
		res+=" "+s7.finalstring;*/
		WhereClause s8=new WhereClause(sentence);
		result+=" "+s8.finalstring;
		return key+" IN "+"("+result+")";


	}

	public void Distance(String s)
	{	int i=0;int k=0,loc,j=0;//K holds index of distance array.I holds the index of each word
		for(String t:s.split(" "))
		{k=0;j=0;//resetting both to nil
			if(t.contains("c_"))
			{
				if(!list.contains(t))//Isnt already present
				{list.add(t.toLowerCase());loc=i;//Storing location of the column_name
					System.out.println("column @"+loc);
					for(String u:s.split(" "))
					{
						if(!u.contains("c_"))
						{
							distance[k++]=Math.abs(loc-j);
						}
						j++;
					}
					dist.put(t,distance);
				}//Have found distance of all from that point
				int count=0;
				for(int x:distance)
				{
					System.out.println(count +":" +x);
					count++;

				}

			}i++;

		}


	}

	public String finalstring;
	//colname->[condition]
	//condition->[data]

    /*public WhereClause(Map<String,String[]> map,Map<String,String> data,Map<String,String> conditions) throws  IOException{
        String[] arr = {"mumbai","bangalore"};
        if(map==null){
            this.finalstring="";
            return;
        }
        map.put("city", arr);

        //System.out.print((map)+"The mapis\n\n");
        data.put("mumbai", "is ");//make sure of the spacings-means greater than 100
        //data.put("bangalore", "is ");
        //System.out.print((data)+"The mapis\n\n");
        conditions.put("city","or");
        //work on condition which are the relations which are there in sentence
        //TODO:After the basic proto type

        String s = "";


        s = stmntgenerator(map, data,conditions);
        String value = "" + conditions(s);

        System.out.println("WHERE " + value);
        this.finalstring="WHERE " + value;

    }*/



	public static String process(String s) {
		/*
			This converts statement to correct SQL syntax
		 */
		String res = "";//result
		String s1="";
		for(String t:s.split(" "))
		{
			//After every and/or/not the column name should be specified
			if(t.contains("c_"))
				s1=t;//Assume only a single column with multiple conditions specified

		}

		for(String t:s.split(" "))
		{
			if(t.equals("AND")||t.equals("OR")||t.equals("NOT"))
				res=res+" "+t+" "+s1+" ";
			else res=res+" "+t;
		}
		//System.out.println(res);
		/*String condition=conditions.get(colname);
		System.out.println(condition);
		int i = 0;
		int length = arr.length;
		for (String data : arr) {
			i++;
			if (k.containsKey(data)) {
				String temp = k.get(data);
				res = res + " " + colname + " " + temp + data + " ";
			}
			else {
				res = res + " " + colname + " equals " + data + " ";
			}
			if (length != i) {
				if(condition==null){
					res+=" AND ";
					continue;
				}
				res += condition;
			}
		}*/
		return res;
	}

	public static String stmntgenerator(String s) throws IOException {
		//m contains column name->data
		String res = "";
		List<String> list = new ArrayList<>();
		List<String> l = Files.readAllLines(Paths.get("Where.txt"));
		/*Only if such a word exists shall we generate a where clause*/
		for(String s2:s.split(" "))
		list.add(s2.toLowerCase());
		int count=0,count_where=0;//Count_where checks if where clause exists
		for(String t:list)
			{	count++;
				if(l.containsAll(Collections.singleton(t))) {
					count_where=1;
					break;
				}

			}

			if(count_where==0)
				return "";
		System.out.println("count=" +count);
		System.out.println(list);

		//Now deleting all the words before 'where' as they will be handled by other classes.
		//TODO:priority setting yet unclear...


		for(int i=0;i<count;i++)
			list.remove(0);

		System.out.println(list+"\n\n");

		/*Iterator<String> iter1 = m.keySet().iterator();
		while (iter1.hasNext()) {
			listofcolumns.add(iter1.next());
		}
		String[] t;
		Iterator<String> iter = m.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String arrayname = iter.next();
			String[] array = m.get(arrayname);
			//this is implicit joining using conjunctions
			System.out.println(arrayname);
			if (i == 0)
			{
				res = process(data, array, arrayname,conditions);
				i++;
			} else
				res = res + " and " + process(data, array, arrayname,conditions);
			//else {
			// TODO: this part should handle different relations between different columns
			// TODO:this part can be handled later
			//
			// }


		}

		System.out.println(res);*/
		StringBuilder sb = new StringBuilder();
		for (String s2 : list)
		{
			sb.append(s2);
			sb.append(" ");
		}

		String s2=sb.toString();
		System.out.println(s2);
		res =conditions(s2);
		res="WHERE "+res;
		res=process(res);
		return res;
	}

	public static String conditions(String cond) throws IOException {
		//todo:Identify the <,>,=,not,etc operations and apply accordingly
    	/* cond hold the condition that has to be altered in the where clause.
    	Converting it to a character symbol*/

		String parts[] = cond.split(" ");//Maximum of 3 tokens eg. greater than equal
		//System.out.println(parts);
		/*Checking if split correctly
		for (String c : parts) {
			System.out.println(c);
		}*/
		//System.out.println("Parts have been printed");

		boolean flag=false;
		String value = "";
		int l = parts.length;
		List<String> great = Files.readAllLines(Paths.get("Greater.txt"));
		List<String> less = Files.readAllLines(Paths.get("Lesser.txt"));
		List<String> equal = Files.readAllLines(Paths.get("Equals.txt"));
		List<String> not = Files.readAllLines(Paths.get("Not.txt"));
		int index=-1;
		Scanner sc=new Scanner(cond.trim());
		for (String inp : parts) {index++;
			if(flag==true)
			{
				flag=false;
				continue;
			}
			if (inp.equalsIgnoreCase("than") || inp.equalsIgnoreCase("to")) {
				//skip all meaningless word in the relation part
				continue;
			} else if (not.containsAll(Collections.singleton(inp))) {
				//if inp is not then ! Similarly to all other cases
				value += '!' + " = "+" ";
			} else if (great.containsAll(Collections.singleton(inp))) {
				value += '>' + " ";
			} else if (less.containsAll(Collections.singleton(inp))) {
				value += '<' + " ";
			} else if (equal.containsAll(Collections.singleton(inp))) {//If a number then do this
				if(sc.hasNextInt())
				value += '=' + " ";
				else {
					value += "=" + " '" + parts[index+1] + "' ";
					flag=true;
				}
			} else if (inp.equalsIgnoreCase("and")) {
				value += "AND" + " ";
			}
			else  if(inp.equalsIgnoreCase("or")){
				value+= " OR ";
			}
			else {

				value += inp + " ";
			}

		}
		System.out.println(value+" is value");

		//Return the symbol that was converted.
		return value;
	}//Testing purposes only


	public WhereClause(String sentence) throws IOException {
		String s=sentence;//"select all customers where salary greater than 4500";
		String s1=stmntgenerator(s);
		System.out.println(s1);
		System.out.println("\n\n\nThe distances are as follows \n");
		Distance(s);
		this.finalstring=s1;




	}


}