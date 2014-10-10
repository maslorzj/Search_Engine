/* --
COMP4321 Lab2 Exercise
Student Name:
Student ID:
Section:
Email:
*/
import java.util.Vector;

import org.htmlparser.beans.StringBean;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.StringTokenizer;

import org.htmlparser.beans.LinkBean;

import java.net.URL;


public class Crawler
{
	private String url;
	Crawler(String _url)
	{
		url = _url;
	}
	
	public Vector<String> extractWords() throws ParserException

	{
		// extract words in url and return them
		// use StringTokenizer to tokenize the result from StringBean
		// ADD YOUR CODES HERE
		StringBean sb = new StringBean();
	    sb.setLinks(false);
	    sb.setReplaceNonBreakingSpaces(true);
	    sb.setCollapse(true);
	    sb.setURL (url); // the HTTP is performed here
	    String s = sb.getStrings();
	    
	    // use StringTokenizer to tokenize the result
	    StringTokenizer st = new StringTokenizer(s);
	    Vector<String> words = new Vector<String>();
	    while (st.hasMoreTokens()) 
	    {
	    	words.add(st.nextToken());
	        
	    }
	    return words;
	}
	public Vector<String> extractLinks() throws ParserException

	{
		// extract links in url and return them
		// ADD YOUR CODES HERE
		LinkBean lb = new LinkBean();
		lb.setURL(url);
		URL[] links = lb.getLinks();
		//String links = lb.getLinks();
		Vector<String> link_str = new Vector<String>();
		
		for(int i = 0; i < links.length; i++)
		{
			link_str.add(links[i].toString());
		}
		
		return link_str;
	}
	
	
	/* 
		input: url is the starting url for crawler
		 	   url_number is the number of urls for crawler
	*/
	public static void CrawlerByUrl(String url, int url_number)
	{
		try
		{
			int count = 0;
			// the queue to store url
			String[] queue = new String[url_number];
			queue[0] = new String(url);
			// for stopword
			StopStem stopStem = new StopStem("stopwords.txt");
			// to initialize the database
			database data = new database("comp4321");
			
			// to process each url
			while(count < url_number)
			{
				String str = queue[count];
				int page_id = data.check_pagetable(str);
				// if the page is not in the page table
				if(page_id == -1)
				{
					page_id = data.put_pagetable(str);
					
				}
				// if the page is in the page table already
				else
				{
					
				}
				/*
				// get the words in the page
				Crawler crawler = new Crawler(queue[count]);
				Vector<String> words = crawler.extractWords();
				// remove the stopword
				*/
			}
		}
		catch (Exception e)
	    {
			e.printStackTrace();
	    }
	}
	
	public static void main(String[] args)
	{
		try
		{
			

			URL url = new URL("http://www.xyz.com/documents/files/xyz-china.pdf");

		    System.out.println("URL:- " +url);
		    URLConnection connection = url.openConnection();

		    Crawler crawler = new Crawler("http://www.cs.ust.hk");
		    System.out.println(connection.getHeaderField("Last-Modified"));
			Vector<String> words = crawler.extractWords();		
			
			System.out.println("Words in "+crawler.url+":");

			for(int i = 0; i < words.size(); i++)
				System.out.print(words.get(i)+" ");
			System.out.println("\n\n");
			

	
			Vector<String> links = crawler.extractLinks();
			System.out.println("Links in "+crawler.url+":");
			for(int i = 0; i < links.size(); i++)		
				System.out.println(links.get(i));
			System.out.println("");
			
		}
		catch (ParserException e)
        {
			e.printStackTrace();
        }
		
	}
}
	
