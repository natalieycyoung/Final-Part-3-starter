/**
 * CSE 11 Final Exam
 * Part 3 Task 2
 *
 * @author Natalie Young
 * @since 2021-12-06
 */

import tester.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;	// file.Files, file.Path, file.Paths
import java.util.*;
import java.io.IOException;

interface Query<T>
{
	boolean matches(T t);
}

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
		List<Query<List<String>>> queries = new ArrayList<>();
		List<String> queryMatches = new ArrayList<>();
		List<List<String>> allContainedQueries = new ArrayList<>();
		
		for (int i = 1; i < args.length; i++)
		{
			Query<List<String>> query = new ContainStringQuery(args[i]);

			queries.add(query);
		}
		
		for (String file : fileList)
		{
			List<String> containedQueries = new ArrayList<>();

			Path docPath = Paths.get(file);
			List<String> docText = getLines(docPath);

			for (Query<List<String>> query : queries)
			{
				int qMatchCount = 0;

				if (query.matches(docText))
				{
					qMatchCount = 1;
//					queryMatches.add(file);
//System.out.println("search term ("
//		+ ((ContainStringQuery) query).searchTerm + ") found in " + file);
					containedQueries.add(((ContainStringQuery) query).searchTerm);
					//.add(containedQueries.size());
				}
//System.out.println("query: " + query + "\t\tmatchCount: " + qMatchCount);
			}

			allContainedQueries.add(containedQueries);
			// how many times query showed up
		}
/*
		String relevantSearch = all;
		String relevantDoc;
*/

		int resultCount = 0;
		int indexOfLargest = 0;

		for (int i = 0; i < allContainedQueries.size(); i++)
		{

			if (resultCount < allContainedQueries.get(i).size())
			{
				resultCount = allContainedQueries.get(i).size();
				indexOfLargest = i;
			}
		}

		System.out.println("Most relevant search term: ");
		System.out.println("Most relevant document: " + fileList.get(indexOfLargest));

		System.out.println("\n");

		for (int i = 0; i < queries.size(); i++)
		{
			String searchTerm = ((ContainStringQuery) queries.get(i)).searchTerm;
			int count = 0;
			String files = "";

			System.out.println(searchTerm + ": " + count + " [" + files + "]");
		}

		System.out.println("\n");

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


