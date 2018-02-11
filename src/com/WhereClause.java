package com;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WhereClause {
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

		this.finalstring=s1;


	}


}