package org.processmining.plugins;



import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;

public class NgramPlugin {
	 
	@Plugin
	        (name = "Ngram(Task2)-plugin", 
			parameterLabels = {"eventLogFile"}, 
			returnLabels = {"predective analysis" }, 
			returnTypes = {JFrame.class }, 
			userAccessible = true, 
			help ="Plugin renders the probibilty table of predeted events W.R.T running events inputs provided")
	
	@UITopiaVariant
	         (affiliation = "University of Koblenz and Landau", 
	          author = "Manojkumar Krishnakumar", 
	          email = "manojkumar@uni-koblenz")
	
	public static JFrame helloWorld(PluginContext context,XLog log) throws Exception {
				
		
				//Hard coded input for the sequence of events.
				List<String> inputSequence = new ArrayList<String>();
				inputSequence.add("finish editing");
				inputSequence.add("create");
				inputSequence.add("initialize");
				inputSequence.add("performed");
				
				//Hard code ngram count
				int ngramCount = 4;
				
				LogEventProcessor eventProcessor = new LogEventProcessor();
				
				// Parse the event log
				Map<Long,List<String>> eventLongMap = eventProcessor.getMapOfEvents(log);
				
				// Produce possible ngram sequences
				List<List<String>> nGramList = eventProcessor.generateNgramCountSequences(inputSequence, ngramCount);
				
				//System.out.println("NGram list count:"+nGramList.size());
			
				//Using Tree map to get sorted map.
				Map<Long,Map<String,Double>> resultMap = new TreeMap<>();
				nGramList.forEach(nGram->{
					Long nValue = (long) nGram.size();
					Map<String, Long> eventOccMap = eventProcessor.formNextEventsMap(nGram, eventLongMap);
					System.out.println("Event count map:"+eventOccMap);
					Map<String, Double> probMap = eventProcessor.probabilityCalculator(eventOccMap);
					System.out.println("Prob map:"+probMap);
					
					resultMap.put(nValue, probMap);
				});
				
				JFrame jframe = eventProcessor.constructTable(resultMap,ngramCount);
				
				return jframe;
	}
}