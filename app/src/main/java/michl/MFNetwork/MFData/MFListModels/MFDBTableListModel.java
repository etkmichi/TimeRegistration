/*
package MFNetwork.MFData.MFListModels;

import java.util.Vector;

import javax.swing.DefaultListModel;

import MFNetwork.MFDatabase.MFDatabase.MFDBData;
import MFNetwork.MFDatabase.MFDatabase.MFDBTable;

*/
/*TODO implement with sorting vector or array. Implement add functions for MFContact objects*//*

public class MFDBTableListModel extends DefaultListModel<String>  {
	*/
/**
	 * 
	 *//*

	private static final long serialVersionUID = 1L;
	
	
	*/
/**
	 * TODO to sort the gui list, the vecIndexMap has to change
	 *//*

	protected Vector<Integer> vecIndexMap=new Vector<Integer>();
	protected MFDBTable table;
	
	
	public MFDBTableListModel(MFDBTable table){
		this.table=table;
	}
	
	*/
/**
	 * Loads all data from the table. Avoid using this method, if specific content should be shown.
	 * *//*

	public void loadFromTable() {
		//TODO
	}
	
	@Override
	public void addElement(String contactName) {
		super.addElement(contactName);
		vecIndexMap.addElement(-1);
	}
	
	public void addElement(MFDBData data) {
		super.addElement(data.getComposedName());
		vecIndexMap.addElement(data.getTableIndex());
	}
	
	public void addElement(String contactName,MFDBData data) {
		super.addElement(contactName);
		vecIndexMap.addElement(data.getTableIndex());
	}
	
	*/
/**
	 * This function gets the data of a specific table with the selected index of the list model.
	 * @param GUIIndex
	 * @return null if index is out of bounds or table entry is null. If a valid table entry is available it will
	 * be returned.
	 *//*

	public MFDBData getData(int GUIIndex) {
		if(GUIIndex>=vecIndexMap.size() || GUIIndex<0)
			return null;
		int tableIndex = vecIndexMap.get(GUIIndex);
		if(tableIndex>=0 && tableIndex<table.size())
			return table.get(tableIndex);
		return null;
	}
}
*/
