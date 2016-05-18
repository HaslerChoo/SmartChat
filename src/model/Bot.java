package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import error.Log;
import error.LogType;


public class Bot implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String fileName;
	private final String path="resource/";
	private ArrayList<String[]> memory;
	private boolean learn;

	public Bot(String name)
	{
		learn=false;
		this.name=name;
		fileName=name+"_memory";
		readMemory();
	}
	
	@SuppressWarnings("unchecked")
	private void readMemory(){
		File file=new File(path+fileName);
		try {
			FileInputStream fileStream=new FileInputStream(file);
			ObjectInputStream stream=new ObjectInputStream(fileStream);
			memory= (ArrayList<String[]>) stream.readObject();
			stream.close();
			Log.addLog(LogType.INFO, "memory recovery");
		} catch (IOException |ClassNotFoundException e ) {
			Log.addLog(LogType.ERROR,e.getMessage());
			memory=new ArrayList<String[]>();
		}
	}
	
	private void updateMemory(){
		File file=new File(path+fileName);
		try {
			FileOutputStream fileStream=new FileOutputStream(file);
			ObjectOutputStream stream=new ObjectOutputStream(fileStream);
			stream.writeObject(memory);
			stream.flush();
			stream.close();
			Log.addLog(LogType.INFO, "memory update");
		} catch (IOException e) {
			Log.addLog(LogType.ERROR, e.getMessage());
		}
	}
	
	public void learn(String[] learn){
		memory.add(learn);
		Log.addLog(LogType.INFO, "new word learn it");
		updateMemory();
	}
	
	public String answer(String question)
	{
		for (int i = 0; i < memory.size(); i++) {
			if (i%2!=0) {
				continue;
			}
			if (know(question,memory.get(i))) {
				int answer=(int)(Math.random()*memory.get(i+1).length);
				return memory.get(i+1)[answer];
			}
		}
		learn=true;
		return "i dont know how to answer that, please teach me";
	}
	
	private boolean know(String in,String[] str){
		for (String question:str) {
			if (question.equalsIgnoreCase(in)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public boolean isLearn() {
		return learn;
	}

	public void setLearn(boolean learn) {
		this.learn = learn;
	}
}
