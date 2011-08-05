package org.sedml.jlibsedml.editor.configdialogs;

public class ExecutorServiceFactory {
	private static ExecutorServiceFactory fac;
	public static  ExecutorServiceFactory getInstance(){
		if(fac == null){
			fac = new ExecutorServiceFactory();
		}
		return fac;
	}
	private ICommandExecutor executor;
	private ExecutorServiceFactory(){}
	
	/**
	 * 
	 * @return A non-null {@link ICommandExecutor}
	 * @throws IllegalStateException if this has not previously been set.
	 */
	public ICommandExecutor getCommandExecutor(){
		if(executor==null){
			throw new IllegalStateException();
		}
		return executor;
	}
	
	/**
	 * Must be set at start-up
	 * @param exe A non-null {@link ICommandExecutor}
	 */
	public void setCommandExecutor(ICommandExecutor exe) {
		if(exe ==null){
			throw new IllegalArgumentException();
		}
		this.executor=exe;
	}

}
