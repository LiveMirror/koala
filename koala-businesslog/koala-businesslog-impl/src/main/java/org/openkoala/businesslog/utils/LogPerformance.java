package org.openkoala.businesslog.utils;

import java.util.ArrayList;
import java.util.List;

public class LogPerformance {

	public static List<Long> runtimes = new ArrayList<Long>();
	
	public static void addRunTime(long runtime) {
		runtimes.add(runtime);
	}
	
	public static void clearData() {
		runtimes.clear();
	}
	
	public static String outputAnalyseResult() {
		if (runtimes.isEmpty()) return "No Data!"; 
		List<Long> theRuntimes = new ArrayList<Long>(runtimes);
		theRuntimes.remove(0);
		if (theRuntimes.isEmpty()) return "No Data!"; 
		
		long maxTime = caculateMaxTime(theRuntimes);
		long minTime = caculateMinTime(theRuntimes);
		long averageTime = caculateAverageTime(theRuntimes);
		
		System.out.println("Max Run Time : " + maxTime + "ms");
		System.out.println("Min Run Time : " + minTime + "ms");
		System.out.println("Average Run Time : " + averageTime + "ms");
		
		return "Max Run Time : " + maxTime + "ms" + "<br>"
				+ "Min Run Time : " + minTime + "ms" + "<br>"
				+ "Average Run Time : " + averageTime + "ms";
	}
	
	private static long caculateAverageTime(List<Long> theRuntimes) {
		long totalRunTime = 0L;
		for (Long time : theRuntimes) {
			totalRunTime = totalRunTime + time;
		}
		return (int)(totalRunTime/theRuntimes.size());
	}
	
	private static long caculateMaxTime(List<Long> theRuntimes) {
		long maxTime = 0;
		for (Long time : theRuntimes) {
			if (time > maxTime) {
				maxTime = time;
			}
		}
		return maxTime;
	}
	
	private static long caculateMinTime(List<Long> theRuntimes) {
		long minTime = 1000000;
		for (Long time : theRuntimes) {
			if (time < minTime) {
				minTime = time;
			}
		}
		return minTime;
	}
	
}
