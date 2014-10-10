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
	
	public static void main (String[] args)
	{
		try
		{
			Crawler crawler = new Crawler("http://www.cs.ust.hk");

			
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
	
