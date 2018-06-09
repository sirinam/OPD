package com.zillious.training.assignment1.entity;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * The entity class
 * 
 * @author Sampath
 *
 */
public class Member implements Serializable {

	//serial version UID for serialization
	private static final long serialVersionUID = -683293720732640413L;
	
	/**
	 * Unique id of a Member in FamilyTree
	 */
	private Integer memberID;
	
	/**
	 * Name of the Member in FamilyTree 
	 */
	private String name;
	
	/**
	 * Address of Member in FamilyTree
	 */
	private String address;
	
	/**
	 * Depth of the Member in FamilyTree
	 */
	private int relationWithRoot;
	
	/**
	 * Children of Member in FamilyTree
	 */
	private ArrayList<Member> children;
	
	
	
	/**
	 * Creates a family tree with root details as given memberId as id of root and name as name of the root of family tree 
	 * 
	 * @param memberID - MemberId
	 * @param name - Name of the Member
	 */
	public Member(int memberID, String name) {
		this.memberID = memberID;
		this.name = name;
		this.children = new ArrayList<Member>();
	}

	/**
	 * @param memberID - MemberId 
	 * @param name -Name of the Member
	 * @param address - Address of the Member
	 */
	public Member(int memberID, String name, String address) {
		super();
		this.memberID = memberID;
		this.name = name;
		this.address = address;
		this.relationWithRoot=0;
		this.children = new ArrayList<Member>();
	}

	/**
	 * Adds the dependency between the 
	 * @param child
	 */
	public void addDependency(Member child) {
		child.relationWithRoot = this.relationWithRoot + 1;
		this.children.add(child);
	}

	/**
	 * @param child
	 */
	public void deleteDependency(Member child) {
		this.children.remove(child);
	}

	/**
	 * @return the memberID
	 */
	public int getMemberID() {
		return memberID;
	}

	/**
	 * @param memberID
	 *            the memberID to set
	 */
	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the relationWithRoot
	 */
	public int getRelationWithRoot() {
		return relationWithRoot;
	}

	/**
	 * @param relationWithRoot
	 *            the relationWithRoot to set
	 */
	public void setRelationWithRoot(int relationWithRoot) {
		this.relationWithRoot = relationWithRoot;
	}

	/**
	 * @return the children
	 */
	public ArrayList<Member> getChildren() {
		return children;
	}

}
