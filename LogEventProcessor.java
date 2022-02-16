package org.processmining.plugins;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class LogEventProcessor {
	
	
	Map<Long,List<String>> getMapOfEvents(XLog log) throws Exception {		
		Map<Long,List<String>> logEventMap=new HashMap<>();		
		for (long i=0;i<log.size();i++) {			
			List<String> eventList=new ArrayList<>();
			XTrace trace = log.get((int) i);
			for (XEvent event : trace) {
				XAttributeMap attrMap = event.getAttributes();
				String event_name = attrMap.get("concept:name").toString();
				eventList.add(event_name);
			}
			logEventMap.put(i, eventList);			
		}		
		return logEventMap;		
	}
	
	  HashMap<String, List<String>> parseInput(XLog log, List<String> inputEventSequence, int ngramCount) throws Exception {			
		XTrace r_sequence = log.get(0);
		List<String> event_trace = new ArrayList<>();
		for (XEvent event : r_sequence) {
			XAttributeMap attrMap = event.getAttributes();
			String event_name = attrMap.get("concept:name").toString();
			event_trace.add(event_name);
		}
		
		String n_value = log.get(1).get(0).getAttributes().get("concept:name").toString();
		HashMap<String,List<String>> input_data = new HashMap<>();
		input_data.put(n_value, event_trace);
		return input_data;
	}
	


	List<List<String>> generateNgramCountSequences(List<String> eventSequence, int ngramCount) {			
		List<List<String>> nGramList = new ArrayList<>();
		int len = eventSequence.size();
		for (int i = 1; i <= ngramCount; i++) {
			List<String> subGramList = eventSequence.subList(len - i, len);
			nGramList.add(subGramList);
		}
		return nGramList;
	}

	Map<String, Long> formNextEventsMap(List<String> nGramSequence,Map<Long,List<String>> eventLongMap) {		 
		 Map<String, Long> eventOccuranceMap = new HashMap<>();		 
		 eventLongMap.values().forEach(eventList->{			 
				int sequenceMatchIndex = Collections.indexOfSubList(eventList, nGramSequence);
				int nextEventIndex = sequenceMatchIndex + nGramSequence.size();				
				if((sequenceMatchIndex >=0) && (nextEventIndex<=eventList.size()-1)) {					
					String nextEvent=eventList.get(nextEventIndex);					
					//Increase the occurrence count for events.
					if(eventOccuranceMap.containsKey(nextEvent)) {
						eventOccuranceMap.put(nextEvent, eventOccuranceMap.get(nextEvent)+1);
					}else {
						eventOccuranceMap.put(nextEvent, (long) 1);
					}
				}			 
		 });		 
		 return eventOccuranceMap;
		 

	}
	Map<String, Double> probabilityCalculator(Map<String, Long> eventOccMap) {	
		Map<String, Double> probabilityMap = new HashMap<>();
		Long totalCount = eventOccMap.values().stream().mapToLong(Long::longValue).sum();
		System.out.println("Total count"+totalCount);
		eventOccMap.entrySet().forEach(eventOcc->{
			probabilityMap.put(eventOcc.getKey(),Math.round((double)eventOcc.getValue() / totalCount * 1000d) / 1000d);
		});
		return probabilityMap;
	}
	 static String[][] getTableData(Map<Long,Map<String,Double>> resultMap) {	
		List<String> unique_events = new ArrayList<>(resultMap.get((long)1).keySet());	
		int row_size = unique_events.size();
		int column_size = resultMap.size()+1;
		String[][] tabledata = new String[row_size][column_size];
		for(int i=0;i<row_size;i++) {	
			String unique_event_name = unique_events.get(i);
			for(int j=0;j<column_size;j++) {
				if(j==0) {
					tabledata[i][0] = unique_event_name;
					continue;
				}
				Map<String, Double> probMap = resultMap.get((long)j);
				if(probMap.containsKey(unique_event_name)) {
					tabledata[i][j] = Double.toString(probMap.get(unique_event_name));
				}
				else {
					tabledata[i][j] = Double.toString((double) 0);
				}
			}
		}
	 return tabledata;
	}
	
	 JFrame constructTable(Map<Long,Map<String,Double>> resultMap,int nGramCount) {		
		String column[] = new String[nGramCount+1];
		column[0] = "EVENT_NAME ↓ | N-GRAM VALUE → ";
		for(int i=1;i<=nGramCount;i++) {
			column[i] = Integer.toString(i);
		}
	    JFrame f=new JFrame();    
	    JTable jt=new JTable(getTableData(resultMap),column);    
	    TableColumnModel columnModel = jt.getColumnModel();
	    columnModel.getColumn(0).setPreferredWidth(400);
	    JTableHeader th = jt.getTableHeader();
	    th.setFont(new Font("Serif", Font.BOLD, 15));
	    JScrollPane sp=new JScrollPane(jt);    
	    f.add(sp);          
	    f.setSize(1000,700);    
	    f.setVisible(true); 
	    return f;
	}
}
