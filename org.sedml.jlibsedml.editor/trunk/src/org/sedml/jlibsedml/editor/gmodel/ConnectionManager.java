package org.sedml.jlibsedml.editor.gmodel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages possible cross-reference  & aggregation connections
 * @author radams
 *
 */
public class ConnectionManager {
	static Map<Class, List<Class>> src2TargRefRelations = new HashMap<Class, List<Class>>();
	static {
		src2TargRefRelations.put(GTask.class, Arrays.asList(new Class[]{GSimulation.class,GModel.class}));
		src2TargRefRelations.put(GVariable.class, Arrays.asList(new Class[]{GTask.class}));
		src2TargRefRelations.put(GCurve.class,Arrays.asList(new Class[]{GDataGenerator.class}));
		src2TargRefRelations.put(GDataset.class,Arrays.asList(new Class[]{GDataGenerator.class}));
		

	}
	
	static Map<Class, List<Class>> aggregateRelations = new HashMap<Class, List<Class>>();
	static {
		aggregateRelations.put(GModel.class, Arrays.asList(new Class[]{GChange.class}));
		aggregateRelations.put(GDataGenerator.class, Arrays.asList(new Class[]{GVariable.class, GParameter.class}));
		aggregateRelations.put(GChange.class, Arrays.asList(new Class[]{GVariable.class, GParameter.class}));
		aggregateRelations.put(GReport.class, Arrays.asList(new Class[]{GDataset.class}));
		aggregateRelations.put(GPlot2D.class,Arrays.asList(new Class[]{GCurve.class}));

	}
	/**
	 * Boolean test for whether reference type is possible between src and targ
	 * @param src
	 * @param targ
	 * @return
	 */
	public boolean canConnectReferenceRelations(GElement src,
			GElement targ) {
		return canConnect(src, targ, src2TargRefRelations,false,false);

	}
	
	/**
	 * Boolean test for whether aggregation is possible between src and targ
	 * @param src
	 * @param targ
	 * @return
	 */
	public boolean canConnectAggregateRelations(GElement src,
			GElement targ) {
		return canConnect(src, targ, aggregateRelations,false,false);

	}
	
	/**
	 * Boolean test for whether any connection type is possible between src and targ
	 * @param src
	 * @param targ
	 * @return
	 */
	public boolean canConnect(GElement src,
			GElement targ, boolean isSrcReconnect, boolean isTargetReconnect){
		return canConnect(src, targ, aggregateRelations,isSrcReconnect,isTargetReconnect) || canConnect(src, targ,src2TargRefRelations,
																							isSrcReconnect,isTargetReconnect);
	}
	
	boolean canConnect(GElement src,
			GElement targ, Map<Class, List<Class>> mappings, boolean isSrcReconnect, boolean isTargetReconnect){
		if(!isSrcReconnect){
			if(src.isDataGenerator() && (targ.isVariable()|| targ.isParameter())){
				for (Connection existing: targ.getTargetConnections()){
					if (existing.getSource().isDataGenerator()){
						return false;
					}
				}
			}else if(src.isPlot2D() && targ.isCurve()){
				for (Connection existing: targ.getTargetConnections()){
					if (existing.getSource().isPlot2D()){
						return false;
					}
				}
			}else if(src.isReport() && targ.isDataset()){
				for (Connection existing: targ.getTargetConnections()){
					if (existing.getSource().isReport()){
						return false;
					}
				}
			}else if(src.isVariable() && targ.isTask()&&!isTargetReconnect){
				for (Connection existing: src.getSrcConnections()){
					if (existing.getTarget().isTask()){
						return false;
					}
				}
			}else if(src.isDataset() && targ.isDataGenerator()&&!isTargetReconnect){
				for (Connection existing: src.getSrcConnections()){
					if (existing.getTarget().isDataGenerator()){
						return false;
					}
				}
			}else if(src.isTask() && (targ.isModel()|| targ.isSimulation()) && !isTargetReconnect){
				for (Connection existing: src.getSrcConnections()){
					if (existing.getTarget().isSimulation() && targ.isSimulation() ||
							existing.getTarget().isModel() && targ.isModel()){
						return false;
					}
				}
			}
		}else {
			if(src.isVariable() && targ.isTask()){
				for (Connection existing: src.getSrcConnections()){
					if (existing.getTarget().isTask()){
						return false;
					}
				}
			}else if(src.isDataset() && targ.isDataGenerator()){
				for (Connection existing: src.getSrcConnections()){
					if (existing.getTarget().isDataGenerator()){
						return false;
					}
				}
			}
		}
		Class<? extends GElement> srcClass = src.getClass();
		for (Class<? extends GElement> c : mappings.keySet()) {
			if (c.isAssignableFrom(srcClass)) {
				srcClass = c;
			}
		}
		if (mappings.get(srcClass) != null){
			List<Class> possTargets = mappings.get(srcClass);
			for (Class possTarget: possTargets){
				if(possTarget.equals(
						targ.getClass())){
					return true;
				}
			}
		}
	
		
				
		
		return false;

		
	}

}
