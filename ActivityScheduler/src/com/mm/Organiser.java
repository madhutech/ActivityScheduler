package com.mm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 
 * @author madhumikkili
 * 
 */
public class Organiser {

	Map<String, Integer> inPutMap = new HashMap<>();// assuming sinlge-threaded
	private int START_HOUR = 9;
	private int START_MINUTE = 0;
	private int LUNCH_START_TIME = 12;
	private int STAG_MOTIVIATION_START_TIME=17;
	private int SPRINT_DURATION = 15;
	private int PRE_LUNCH_GAP = 0;
	private int POST_LUNCH_GAP= 0;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
	private static  boolean hasLUNCH_BREAK = true;
	
	

	public int getSTART_HOUR() {
		return START_HOUR;
	}
	public void setSTART_HOUR(int sTART_HOUR) {
		START_HOUR = sTART_HOUR;
	}
	public int getSTART_MINUTE() {
		return START_MINUTE;
	}
	public void setSTART_MINUTE(int sTART_MINUTE) {
		START_MINUTE = sTART_MINUTE;
	}
	public int getLUNCH_START_TIME() {
		return LUNCH_START_TIME;
	}
	public void setLUNCH_START_TIME(int lUNCH_START_TIME) {
		LUNCH_START_TIME = lUNCH_START_TIME;
	}
	public int getSTAG_MOTIVIATION_START_TIME() {
		return STAG_MOTIVIATION_START_TIME;
	}
	public void setSTAG_MOTIVIATION_START_TIME(int sTAG_MOTIVIATION_START_TIME) {
		STAG_MOTIVIATION_START_TIME = sTAG_MOTIVIATION_START_TIME;
	}
	
	public int getPRE_LUNCH_GAP() {
		return PRE_LUNCH_GAP;
	}
	public void setPRE_LUNCH_GAP(int pRE_LUNCH_GAP) {
		PRE_LUNCH_GAP = pRE_LUNCH_GAP;
	}
	public int getPOST_LUNCH_GAP() {
		return POST_LUNCH_GAP;
	}
	public void setPOST_LUNCH_GAP(int pOST_LUNCH_GAP) {
		POST_LUNCH_GAP = pOST_LUNCH_GAP;
	}

	/**
	 * reads input file and populates an internal map.
	 * @param file
	 * @throws IllegalArgumentException
	 */
	public Map<String,Integer> populateInput(final File file) throws IllegalArgumentException {
		String str = null;
		try {
			if (isValid(file)) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				try{
				while ((str = br.readLine()) != null) {
					String[] split = str.trim().split(",");
					if (split.length == 2 && split[0] != null
							&& split[1] != null) {
						if (split[1].trim().equalsIgnoreCase("sprint"))
							inPutMap.put(split[0].trim() + " for "+SPRINT_DURATION+"min", SPRINT_DURATION);
						else
							inPutMap.put(split[0].trim() + " for " + split[1].trim(),
									Integer.valueOf(split[1].trim().substring(
											0, 2)));
					} else
						throw new IllegalArgumentException(
								"Invalid input: file format incorrect");
				}
				}
				finally{
					br.close();
					br=null;
				}

			} else
				throw new IllegalArgumentException(
						"Invalid input: file doesn't "
								+ "exist or can't be read.");
//			Just printing map we constructed
//			for (Map.Entry<String, Integer> entry : map.entrySet()) {
//				System.out.println("1 Key:" + entry.getKey() + "-- Value:"
//						+ entry.getValue());
//			}
//			System.out.println("----");
//			System.out.println("input map: "+inPutMap);;
//			System.out.println("----");

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			
		}
		
		return inPutMap;

	}

	private boolean isValid(File f) {
		return (f.isFile() && f.canRead());

	}

	// private static void shuffleMap(Map<String,Integer> m){
	// List<String> keys = new ArrayList<String>(m.keySet());
	// Collections.shuffle(keys);
	// for (String s : keys) {
	// System.out.println("shuffled:"+s+"-"+m.get(s));
	// }
	// }
	
	/**
	 * Returns a schedule as a Map<LocalTime,String>
	 * @param map
	 * @param haslunchbreak
	 * @return
	 */
	public Map<LocalTime,String> schedule2(Map<String, Integer> map,boolean haslunchbreak) {
	
		Map<LocalTime, String> schedule = new TreeMap<>();
		LocalTime time = LocalTime.of(START_HOUR, START_MINUTE);
		if(haslunchbreak) 
			{
//					map.remove("lunchbreak for 60min");
					
					//add activities before lunch
					Map<String,Integer> tmp = findActivitiesNotExceedingGivenHours(map, LUNCH_START_TIME-START_HOUR);
					for(Map.Entry<String, Integer> entry : tmp.entrySet())
					{
						schedule.put(time, entry.getKey());
						time = time.plusMinutes(entry.getValue());//+PRE_LUNCH_GAP);
					}
					
					//lunch
					schedule.put(LocalTime.of(LUNCH_START_TIME, 0), "lunch for 60 min");
					time = LocalTime.of(LUNCH_START_TIME, 0).plusHours(1);

					//add activities after lunch and before speech
					//but don't consider activities already added
					for(Map.Entry<LocalTime, String> entry : schedule.entrySet()){
						map.remove(entry.getValue());
					}
					Map<String,Integer> tmp2 = findActivitiesNotExceedingGivenHours(map, STAG_MOTIVIATION_START_TIME-LUNCH_START_TIME-1);
					for(Map.Entry<String, Integer> entry : tmp2.entrySet())
					{
						schedule.put(time, entry.getKey());
						time = time.plusMinutes(entry.getValue());//+POST_LUNCH_GAP);
					}
					for(Map.Entry<LocalTime, String> entry : schedule.entrySet()){
						map.remove(entry.getValue());
					}
					
					schedule.put(LocalTime.of(STAG_MOTIVIATION_START_TIME,0),"Stag Motivation Presentation");
			}
//		System.out.println("==============");
//		for (Map.Entry<LocalTime, String> entry2 : schedule.entrySet())
//			System.out.println(entry2.getKey().format(formatter) + " : "
//					+ entry2.getValue());
//		System.out.println("==============");
		
		
		return schedule;

	}
	
	private static Map<String,Integer> findActivitiesNotExceedingGivenHours(Map<String,Integer> m,int givenHours){
		Map<String,Integer> mapSpanningGivenHours = new HashMap<>();
		boolean visitedMap = false;
		int tmp = 0;
		ArrayList<Integer> min = new ArrayList<>();
		List<String> activities = new ArrayList<String>(m.keySet());
		Collections.shuffle(activities);
		for(String activity : activities){
			min.add(m.get(activity));
			Collections.sort(min);
			mapSpanningGivenHours.put(activity, m.get(activity));
			tmp = tmp + m.get(activity);
			if(tmp==givenHours*60)
				break;
			while(tmp>(givenHours*60)){
				for(Map.Entry<String, Integer> entry : mapSpanningGivenHours.entrySet()){
					if(min.get(0).equals(entry.getValue())){
						mapSpanningGivenHours.remove(entry.getKey());
						break;
						}
					}
				tmp=tmp - min.get(0);
				min.remove(0);
				Collections.sort(min);
				visitedMap=true;
			}
			if(visitedMap && tmp <=(givenHours*60)) break;

		}
		
		return mapSpanningGivenHours;
	}
	
	/**
	 * just schedules
	 * @param file
	 */
	
	public String scheduler(final File file){
		StringBuilder sbldr = new StringBuilder();
		Map<String,Integer> inputMap = populateInput(file);
		Map<LocalTime,String> schedule = new TreeMap<>();
		int count = 1;
		System.out.println("	SCHEDULE:");
		sbldr.append("\n").append("SCHEDULE:");
		while(inputMap.size()>0){
			System.out.println("		Team/Day "+count);
			sbldr.append("\n").append("	Team/Day - "+count);
			schedule=schedule2(inputMap,hasLUNCH_BREAK);
			//System.out.println("OUTPUT:"+schedule);
			System.out.println("		==============");
			sbldr.append("\n").append("	==============");
			for (Map.Entry<LocalTime, String> entry2 : schedule.entrySet()){
				System.out.println("		"+entry2.getKey().format(formatter) + " : "
						+ entry2.getValue());
				sbldr.append("\n").append("	"+entry2.getKey().format(formatter) + " : "
						+ entry2.getValue());
		}
			System.out.println("		==============");
			sbldr.append("\n").append("	==============");

//			System.out.println("----");
//			System.out.println("input map: "+inPutMap);;
//			System.out.println("----");
			count++;
		}
		
		return sbldr.toString();
	}

}
