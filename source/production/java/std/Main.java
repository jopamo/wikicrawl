/*
# Wiki Crawler
# Word Frequency in randomly selected Wikipedia artcles
*/

package std;

import io.github.fastily.jwiki.core.NS;
import io.github.fastily.jwiki.core.Wiki;
import org.wikiclean.WikiClean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) {
    Wiki wiki = new Wiki.Builder().build();
    WikiClean cleaner = new WikiClean.Builder().build();

    // make sure this is 500 for release, set lower for testing
    // 500 is 1000 for some reason
    ArrayList<String> thousandRandom = wiki.getRandomPages(500, NS.MAIN);

    // wikiclean expects xml files. another way?
    String xmlStart = "<text xml:space=\"preserve\">";
    String xmlEnd = "</text>";
    StringBuilder wholeText = new StringBuilder();

    // iterate over each wiki article
    // expanded for reference and clarity
    for (int i = 0; i < thousandRandom.size(); i++) {
      // get page text
      String x = wiki.getPageText(thousandRandom.get(i));

      // add xml tags
      x = xmlStart + x + xmlEnd;

      // remove wiki tags
      x = cleaner.clean(x);

      // remove remaining weblinks
      x = x.replaceAll("https?://.*?\\s+", "");
      x = x.replaceAll("http?://.*?\\s+", "");

      // remove special
      x = x.replaceAll("[^a-zA-Z0-9]", " ").toLowerCase();

      // remove numbers
      x = x.replaceAll("\\d", "");

      // remove single letters besides 'a' and 'i'
      x = x.replaceAll("(?:^| )[b-hj-z |B-HJ-Z](?= |$)", "");

      // remove newlines
      x = x.replaceAll("[\\t\\n\\r]+"," ");

      // remove extra spaces
      x = x.trim().replaceAll(" +", " ");

      // join all articles after cleanup into one string
      wholeText.append(" ").append(x);
    }

    System.out.println("***ARTICLES***");

    // print each article chosen for traversal
    for (int i = 0; i < thousandRandom.size(); i++) {
      System.out.println(thousandRandom.get(i));
    }

    System.out.println();
    System.out.println();

    // split string into an arraylist
    List<String> myList = new ArrayList<String>(Arrays.asList(wholeText.toString().split(" ")));

    // cleanup junk
    myList.removeAll(Collections.singleton(null));
    myList.removeAll(Collections.singleton(""));

    // sort in a set
    TreeSet<String> unique = new TreeSet<String>(myList);

    System.out.println("***WORD COUNTS***");

    // print each word and it's frequency
    for (String key : unique) {
      System.out.println(key + ": " + Collections.frequency(myList, key));
    }
  } //close public static void
}
