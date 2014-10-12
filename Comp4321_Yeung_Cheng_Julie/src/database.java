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
	private HTree parent_table;
	private int parent_table_size = 0;
	private HTree child_table;
	private int child_table_size = 0;

	
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
		
		// to read the child table
		recid = recman.getNamedObject("child_table");
		if (recid!=0)
			child_table = HTree.load(recman,recid);
		else
		{
			child_table = HTree.createInstance(recman);
			recman.setNamedObject("child_table", child_table.getRecid());
		}
		
		FastIterator iterate_child = child_table.keys();
		String key_child;
		while((key_child = (String)iterate_child.next()) != null)
		{
			child_table_size++;
		}
		
		// to read the parent table
		recid = recman.getNamedObject("parent_table");
		if (recid!=0)
			parent_table = HTree.load(recman,recid);
		else
		{
			parent_table = HTree.createInstance(recman);
			recman.setNamedObject("parent_table", parent_table.getRecid());
		}
		
		FastIterator iterate_parent = parent_table.keys();
		String key_parent;
		while((key_parent = (String)iterate_parent.next()) != null)
		{
			parent_table_size++;
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
	
	// according to page ID, return its date
	public String get_proptable(int pageID)
	{
		return "";
	}
	
	// put the property of one new page ID
	public void put_proptable(int pageID, String title, String Date, int size)
	{
	}
	
	// update the property of one new page ID
	public void update_proptable(int pageID, String title, String Date, int size)
	{
	}
	
	
	// get the children related to a page ID
	public int[] get_children(int pageID) throws IOException
	{
		//if the pageID is indeed referenced in child_table, we recover the child(ren)
		if(child_table.get(pageID)!=null){
		String children = child_table.get(pageID).toString();
		//since we recover the data as a String, we will decompose each child ID as an int which we will then store in an array of ints
		String childrenID[] = children.split(",");

		int[] children_ID = new int[childrenID.length];
		for (int i=0 ; i<childrenID.length ; i++){
			String s = childrenID[i];
			//here we convert the string id to an int id
			children_ID[i] = Integer.parseInt(s);
		}	
		return children_ID;
		}
		//if no child was found, we return an empty array
		else return null;
	}
	
	
	// get the parents related to a page ID
	public int[] get_parents(int pageID) throws IOException
	{
		//if the pageID is indeed referenced in parent_table, we recover the parent(s)
		if(parent_table.get(pageID)!=null){
		String parents = parent_table.get(pageID).toString();
		//since we recover the data as a String, we will decompose each parent ID as an int which we will then store in an array of ints
		String parentsID[] = parents.split(",");

		int[] parents_ID = new int[parentsID.length];
		for (int i=0 ; i<parentsID.length ; i++){
			String s = parentsID[i];
			//here we convert the string id to an int id
			parents_ID[i] = Integer.parseInt(s);
		}	
		return parents_ID;
		}
		//if no parent were found, we return an empty array
		else return null;
	}
	
	
	// check if a pageID is not already referenced in the parent_table
	public Boolean parent_exists_not(int pageID) throws IOException
	{
		if (get_parents(pageID)==null)
			return true;
			else{
				return false;
			}
	}
	
	
	// check if a pageID is not already referenced in the child_table
	public Boolean child_exists_not(int pageID) throws IOException
	{
		if (get_children(pageID)==null)
		return true;
		else{
			return false;
		}
	}
		
		
	// add a child of a page ID
	public void add_child(int pageID, int childID) throws IOException
	{
		//if no child is already associated to the page ID, we add the child ID associated to the page ID
		if (child_exists_not(pageID))
			child_table.put(pageID,childID);
		else
		{
			// otherwise we add the child ID to the list of other children
			String child_list = child_table.get(pageID).toString();
			String child_ID = "" + childID;
			// each child ID is separated by a comma: "," 
			child_list = child_list+","+child_ID+"";
			child_table.put(pageID,child_list);
		}
	}
	
	// add a parent of a page ID
	public void add_parent(int pageID, int parentID) throws IOException
	{
		//if no parent is already associated to the page ID, we add the parent ID associated to the page ID
		if (parent_exists_not(pageID))
			parent_table.put(pageID,parentID);
		else
		{
			// otherwise we add the parent ID to the list of other parents
			String parent_list = parent_table.get(pageID).toString();
			String parent_ID = "" + parentID;
			// each parent ID is separated by a comma: "," 
			parent_list = parent_list+","+parent_ID+"";
			parent_table.put(pageID,parent_list);
		}
	}
	
	
	
	// delete children of a page ID
	public void delete_children(int pageID)
	{
		
	}
	
	// delete parents of a page ID
	public void delete_parents(int pageID)
	{
		
	}
	
}
