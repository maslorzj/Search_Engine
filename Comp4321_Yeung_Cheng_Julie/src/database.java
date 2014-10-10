import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;

import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;

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
}
