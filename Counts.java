/**
 * CSE11 Java Accelerated
 * Final Exam Part 3 Task 1
 *
 * @author Natalie Young
 * @since 2021-12-06
 */

import tester.*;
import java.util.*;

interface Query<T>
{
	boolean matches(T t);
}

class LongerStringQuery implements Query<String>
{
	int length;
	
	LongerStringQuery(int length)
	{
		this.length = length;
	}
	
	public boolean matches(String s)
	{
		return s.length() > this.length;
	}
}

class EqualStringQuery implements Query<String>
{
	String s;

	EqualStringQuery(String s)
	{
		this.s = s;
	}

	public boolean matches(String other)
	{
		return s.equals(other);
	}
}

class CountExamples	//implements Query<T>
{
	/**
	 * Returns List<Integer> of same size as list queries; each
	 * index in returned list corresponds to the count of values
	 * that matches return true for query at that index in queries
	 */
	// static generic method counts
	static <T> List<Integer> counts(List<Query<T>> queries, List<T> values)
	{
		List<Integer> matchedCounts = new ArrayList<>();

		for (Query query : queries)	// or counter loop?
		{
			int matchCount = 0;

			for (T value : values)
			{
				if (query.matches(value))
				{
					matchCount += 1;
				}
			}

			matchedCounts.add(matchCount);
		}

		return matchedCounts;
	}

	void testCounts(Tester t)
	{
		Query<String> query1 = new LongerStringQuery(2);
		Query<String> query2 = new EqualStringQuery("banana");
		Query<String> query3 = new LongerStringQuery(3);
		List<Query<String>> queries = Arrays.asList(query1, query2, query3);
		List<String> docs = Arrays.asList("banana", "pea", "apple", "tomato", "pear");
		// All 5 values longer than 2
		// 1 value equal to "banana"
		// 4 values longer than 3
		t.checkExpect(counts(queries, docs), Arrays.asList(5, 1, 4));
	}
}
