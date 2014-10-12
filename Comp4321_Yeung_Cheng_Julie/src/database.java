package project;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;

import java.util.Vector;
import java.util.regex.PatternSyntaxException;
import java.io.IOException;
import java.io.Serializable;

import java.lang.*;
public class database {
	// the database's name
	private RecordManager recman;
	// the table of url - pageID
	private HTree page_table;
	private int page_table_size = 0;
	private HTree prop_table;

	
	// constructor of database
	// input is the database's name
	database(String recordmanager) throws IOException
	{
		recman = RecordManagerFactory.createRecordManager(recordmanager);
		
		// to read the page table
		long recid = recman.getNamedObject("page_table");	
		if (recid != 0)
			page_table = HTree.load(recman, recid);
		else
		{
			page_table = HTree.createInstance(recman);
			recman.setNamedObject("page_table", page_table.getRecid());
		}
		FastIterator iterate = page_table.keys();
		String key;
		while((key = (String)iterate.next()) != null)
		{
			page_table_size++;
		}
		
		// to read the property table
		recid = recman.getNamedObject("prop_table");	
		if (recid != 0)
			prop_table = HTree.load(recman, recid);
		else
		{
			prop_table = HTree.createInstance(recman);
			recman.setNamedObject("porp_table", page_table.getRecid());
		}
	
		
	}
	
	// to check the url is in the page table or not, if it is in, return the id
	// if not, return -1
	public int check_pagetable(String url) throws IOException
	{
		Integer x = (Integer)page_table.get(url);
		if(x == null)
			return -1;
		else
			return x;
		
	}

	// to put the url into page table
	public int put_pagetable(String url) throws IOException
	{
		page_table.put(url, page_table_size++);
		return page_table_size-1;
	}
	
	public void read_pagetable() throws IOException
	{
		FastIterator iterate = page_table.keys();
		String key;
		while((key = (String)iterate.next()) != null)
		{
			// print the key and value
			System.out.println(key + " : " + page_table.get(key));
		}
	}
	
	public void finalize() throws IOException
	{
		recman.commit();
		recman.close();				
	} 
	
	// according to page ID, return its date, it there's no page ID return null
	public String get_proptable(int pageID) throws IOException
	{	
		try{
			String date_prop_table; 
			String result;
			result = (String)prop_table.get(pageID);
			String cmp = "\n";
			if( result != null){
				String string_getprop[] = result.split( cmp );
				date_prop_table = string_getprop[0];
				return date_prop_table;
			}
		}
		catch(PatternSyntaxException e){
			e.printStackTrace();
		}
		return null;
	}
	
	// put the property of one new page ID
	// result looks like this = (date+"\n"+title+"\n"+size)
	public void put_proptable(int pageID, String title, String date, int size) throws IOException
	{	
		// check if there's pageID in proptable
		//String string_proptable = String.join("\n", date, title, size);
		String string_proptable = date+"\n"+title+"\n"+size;
		String check = (String)prop_table.get(pageID);
		if( check == null){
			prop_table.put(pageID, string_proptable);	
		}
		else{
			prop_table.remove(pageID);
			prop_table.put(pageID, string_proptable);
		}			
	}
}
