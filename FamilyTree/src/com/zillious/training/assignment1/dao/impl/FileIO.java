package com.zillious.training.assignment1.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.zillious.training.assignment1.dao.DataIO;
import com.zillious.training.assignment1.util.FamilyTree;

/**
 * @author Sampath
 *
 */
public class FileIO implements DataIO {

	private String filePath;// = "D:/tree.ser";
	private static Logger s_logger = Logger.getLogger(FileIO.class);

	public FileIO(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public FamilyTree getData() {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			objectInputStream = new ObjectInputStream(fileInputStream);
			FamilyTree familytTree = (FamilyTree) objectInputStream
					.readObject();
			s_logger.info("Retrieved Tree from previously saved file!!");

			return familytTree;

		} catch (FileNotFoundException fNfe) {
			s_logger.error("File not found exception", fNfe);
		} catch (SecurityException se) {
			s_logger.error("security exception during reading serialized file",
					se);
		} catch (Exception e) {

		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (Exception e) {
				s_logger.error("Error in closing stream", e);
			}
		}
		return null;
	}

	@Override
	public void saveObject(FamilyTree familyTree) {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {

			fos = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(familyTree);
		} catch (Exception e) {
			s_logger.error("Error in writing the object", e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				s_logger.error("Error in closing of stream", e);
			}
		}
	}

}
