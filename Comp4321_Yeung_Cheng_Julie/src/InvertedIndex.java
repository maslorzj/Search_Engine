/* --
COMP336 Lab1 Exercise
Student Name:
Student ID:
Section:
Email:
*/

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;

class Posting implements Serializable
{
	public String doc;
	public int freq;
	Posting(String doc, int freq)
	{
		this.doc = doc;
		this.freq = freq;
	}
}

public class InvertedIndex
{
	private RecordManager recman;
	private HTree hashtable;

	InvertedIndex(String recordmanager, String objectname) throws IOException
	{
		recman = RecordManagerFactory.createRecordManager(recordmanager);
		long recid = recman.getNamedObject(objectname);
			
		if (recid != 0)
			hashtable = HTree.load(recman, recid);
		else
		{
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject( "ht1", hashtable.getRecid() );
		}
	}


	public void finalize() throws IOException
	{
		recman.commit();
		recman.close();				
	} 

	public void addEntry(String word, int x, int y) throws IOException
	{
		// Add a "docX Y" entry for the key "word" into hashtable
		// ADD YOUR CODES HERE
		
		// to check the key's value is null or not
		String value = (String)hashtable.get(word);
		// if the value is null, don't need type the space
		if(value == null)
		{
			hashtable.put(word, "doc"+x+" "+y);
		}
		// else, need type one space
		else
		{
			// get the previous value
			String prevalue = (String)hashtable.get(word);
			// then store the new value
			hashtable.put(word, prevalue + " doc"+x+" "+y);
		}
	}
	public void delEntry(String word) throws IOException
	{
		// Delete the word and its list from the hashtable
		// ADD YOUR CODES HERE
		hashtable.remove(word);
	
	} 
	public void printAll() throws IOException
	{
		// Print all the data in the hashtable
		// ADD YOUR CODES HERE
		
		// create iterate
		FastIterator iterate = hashtable.keys();
		String key;
		while((key = (String)iterate.next()) != null)
		{
			// print the key and value
			System.out.println(key + " = " + hashtable.get(key));
		}
	}	
	/*
	public static void main(String[] args)
	{
		try
		{
			InvertedIndex index = new InvertedIndex("lab1","ht1");
	
			index.addEntry("cat", 2, 6);
			index.addEntry("dog", 1, 33);
			System.out.println("First print");
			index.printAll();
			
			index.addEntry("cat", 8, 3);
			index.addEntry("dog", 6, 73);
			index.addEntry("dog", 8, 83);
			index.addEntry("dog", 10, 5);
			index.addEntry("cat", 11, 106);
			System.out.println("Second print");
			index.printAll();
			
			index.delEntry("dog");
			System.out.println("Third print");
			index.printAll();
			index.finalize();
		}
		catch(IOException ex)
		{
			System.err.println(ex.toString());
		}

	}
	*/
}