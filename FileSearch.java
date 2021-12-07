/**
 * CSE 11 Final Exam
 * Part 3 Task 2
 *
 * @author Natalie Young
 * @since 2021-12-06
 */

//import tester.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;	// file.Files, file.Path, file.Paths
import java.util.*;
import java.io.IOException;

/*
interface Query<T>
{
	boolean matches(T t);
}
*/
class ContainStringQuery implements Query<List<String>>
{
	public String searchTerm;

	ContainStringQuery(String searchTerm)
	{
		this.searchTerm = searchTerm;
	}

	public boolean matches(List<String> strings)
	{
		for (String str : strings)
		{
			if (str.contains(searchTerm))
			{
				return true;
			}
		}

		return false;
	}
}

class FileSearch
{
	public static void main(String[] args)
	{
		Path filesPath = Paths.get(args[0]);
		List<String> fileList = getLines(filesPath);
		List<Query<List<String>>> queryList = new ArrayList<>();
//		List<String> queryMatches = new ArrayList<>();
		List<List<String>> allContainedQueries = new ArrayList<>();
		List<List<String>> allContainingFiles = new ArrayList<>();
		boolean match = false
			;
		// creates new query object for each search term, adds
		// to list of queries
		for (int i = 1; i < args.length; i++)
		{
			Query<List<String>> query = new ContainStringQuery(args[i]);

			queryList.add(query);
		}
	
		// goes through one file at a time, checks each for search
		// term
		for (String file : fileList)
		{
			// holder list
			List<String> containedQueries = new ArrayList<>();

			Path docPath = Paths.get(file);
			List<String> docText = getLines(docPath);

			// checks each query against this particular file
			for (Query<List<String>> query : queryList)
			{
				int qMatchCount = 0;

				if (query.matches(docText))
				{
					qMatchCount = 1;
					
					containedQueries.add(((ContainStringQuery) query).searchTerm);
					match = true;
				//	int qIndex = queryList.indexOf(query);
				}
			}

			// how many times query showed up
			allContainedQueries.add(containedQueries);
		}

		// goes through each query and then each file to look for
		// occurrences
		for (Query<List<String>> query : queryList)
		{
			// holder list
			List<String> containingFiles = new ArrayList<>();
			String qString = ((ContainStringQuery) query).searchTerm;
		//	System.out.println("qString: " + qString);

			for (String file : fileList)
			{
			//	int fMatchCount = 0;

				Path docPath = Paths.get(file);
			//	List<String> docText = getLines(docPath);
				String fileText = "";
				try {
					fileText = Files.readString(docPath);
				} catch (IOException e)
				{
					System.out.println("containingFile failed");
				}
		//		System.out.println("file: " + file);
			//	for (String line : docText)
			//	{
					// checks each query against this particular file
					if (fileText.contains(qString))
					{
				//		fMatchCount = 1;
						
						containingFiles.add(file);
						match = true;
//					System.out.println("file " + file + " contains query" + qString);	
						//int qIndex = queryList.indexOf(query);
					}
			//	}
			}

			allContainingFiles.add(containingFiles);
		//	allContainingFiles.add(containingFiles);
		//			containingFiles.add(file);
		}		

		int resultCount = 0;
		int indexOfLargest = 0;
		List<Integer> searchTermCount = new ArrayList<>();

		for (int i = 0; i < queryList.size(); i++)
		{
			searchTermCount.add(0);
		}


		for (int i = 0; i < allContainedQueries.size(); i++)
		{
			for (int j = 0; j < allContainedQueries.get(i).size(); j++)
			{
				String foundTerm = allContainedQueries.get(i).get(j);

				for (Query<List<String>> query : queryList)
				{
					if (foundTerm.equals(((ContainStringQuery) query).searchTerm))
					{
						int termIndex = queryList.indexOf(query);
						int count = searchTermCount.get(termIndex);
						
						count += 1;

						searchTermCount.set(termIndex, count);
						//int queryList.indexOf(query) = 1;
					}
				}
			}
			
			if (resultCount < allContainedQueries.get(i).size())
			{
				resultCount = allContainedQueries.get(i).size();
				indexOfLargest = i;
			}

		}

		int mostRelevant = 0;
		int relevantIndex = 0;
		for (Integer count : searchTermCount)
		{
			if (count > mostRelevant)
			{
				mostRelevant = count;
				relevantIndex = searchTermCount.indexOf(count);
			}
		}

		if (match)
		{
			System.out.println("Most relevant search term: "
					+ ((ContainStringQuery) queryList.get(relevantIndex)).searchTerm);
			System.out.println("Most relevant document: "
					+ fileList.get(indexOfLargest));

			System.out.println("");

			for (int i = 0; i < queryList.size(); i++)
			{
				String searchTerm = ((ContainStringQuery) queryList.get(i)).searchTerm;
				int count = searchTermCount.get(i);
				List<String> list = allContainingFiles.get(i);
				String files = String.join(", ", list);

				System.out.println(searchTerm + ": " + count + " [" + files + "]");
			}

			System.out.println("");

			for (int i = 0; i < fileList.size(); i++)
			{
				List<String> list = allContainedQueries.get(i);
				//Arrays.asList("test1", "test2", "test3");
				// set to list that contains number of times
				// search term showed up

				String file = fileList.get(i);

				int termCount = list.size();
				String searchTerms = String.join(", ", list);

				System.out.println(file + ": " + termCount + " [" + searchTerms + "]");
			}
		}
		else
		{
			System.out.println("No matches found");
		}
		// print out search term that matched most documents
		// print out document that had most search terms matched
		//
		// print out how many documents a term was found in, with
		// list of matches in brackets
		//
		// print out how many terms found in document with list of
		// matches in brackets
		//
		// substrings still count as match
		//
		// term can only match each document once; multiple matches
		// should only add 1 to count
		//
		//

	}

	/**
	 * Takes the path of document, outputs lines into list,
	 * returns list
	 *
	 * @param path
	 * @return lines
	 */
	static List<String> getLines(Path path)
	{
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	/*		for (String line : lines)
			{
				System.out.println(line);
			}
	*/
		}
		catch (IOException e) {
			System.out.println("readAllLines failed");
		}

		return lines;
	}
}


