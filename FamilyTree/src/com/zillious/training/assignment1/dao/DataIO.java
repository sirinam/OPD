package com.zillious.training.assignment1.dao;

import com.zillious.training.assignment1.util.FamilyTree;

/**
 * @author Sampath
 *
 */
public interface DataIO {
	
	FamilyTree getData();
	void saveObject(FamilyTree familyTree);

}
