package com.zillious.training.assignment1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zillious.training.assignment1.entity.Member;

/**
 * The utility class
 * 
 * @author Sampath
 *
 */
public class FamilyTree implements Serializable {

	// serial version UID for serialization
	private static final long serialVersionUID = 1662955191204333398L;

	private static Logger s_logger = Logger.getLogger(FamilyTree.class);

	// root Member of FamilyTree
	/**
	 * root Member in the FamilyTree
	 */
	private Member root;

	// HashMap for printing children of every Node
	private HashMap<Member, Set<Integer>> nodeChildren;

	/**
	 * Makes new family tree object with root member initialized
	 * 
	 * @param id
	 *            - MemberId of root
	 * @param name
	 *            - Name of the root
	 * @param address
	 *            - Address of the root
	 */
	public FamilyTree(int id, String name, String address) {
		root = new Member(id, name, address);
		nodeChildren = new HashMap<Member, Set<Integer>>();
		nodeChildren.put(root, new HashSet<Integer>());
	}

	/**
	 * It adds new member to the family tree
	 * 
	 * @param id
	 *            - id of the new member
	 * @param name
	 *            - name of the member
	 * @param address
	 *            - address of the member
	 * @param parent
	 *            - parent of the new child
	 */
	public void addMember(int id, String name, String address, Member parent) {
		Member child = new Member(id, name, address);
		parent.addDependency(child);
		ArrayList<Member> children = parent.getChildren();
		Set<Integer> childrenID = new HashSet<Integer>();
		for (Member m : children)
			childrenID.add(m.getMemberID());
		nodeChildren.put(parent, childrenID);
		nodeChildren.put(child, new HashSet<Integer>());
	}

	/**
	 * It retrieves a member from the family tree of given id
	 * 
	 * @param id
	 * @return
	 */
	public Member getMember(int id) {
		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member.getMemberID() == id)
				return member;
			queue.addAll(member.getChildren());
		}

		return null;
	}

	/**
	 * It prints breadth first traversal of family tree.
	 */
	public void breadthFirstTraversal() {
		if (root == null) {
			System.out.println("Tree is Empty");
			return;
		}
		ArrayList<Member> queue = new ArrayList<>();
		queue.add(root);
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			System.out.println(member.getMemberID() + " : "
					+ nodeChildren.get(member));
			// queue.addAll(member.getChildren());
			Iterator<Member> it = member.getChildren().iterator();
			while (it.hasNext()) {
				Member child = it.next();
				if (!queue.contains(child)) {
					queue.add(child);
				}
			}
			System.out.println();
		}

	}

	/*
	 * Searching for given member in family tree using breadth first traversal
	 * and returning children of the member, if member is present else returning
	 * null
	 */
	/**
	 * Returns ArrayList of Member, which contains immediate children of the
	 * given Member id
	 * 
	 * @param id
	 *            - Member id
	 * @return ArrayList of Member, which contains immediate children of the
	 *         given Member id
	 */
	public ArrayList<Member> getImmediateChildren(int id) {

		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member.getMemberID() == id)
				return member.getChildren();
			queue.addAll(member.getChildren());
		}
		return null;
	}

	/*
	 * Searching for given member in family tree using breadth first traversal
	 * and returning descendants of the member, if member is present else
	 * returning null
	 */
	/**
	 * Returns Set of Member, which contains all the descendants of given Member
	 * id
	 * 
	 * @param id
	 *            - Member id
	 * @return Set of Member, which contains all the descendants of given Member
	 *         id
	 */
	public Set<Member> getDescendants(int id) {
		HashSet<Member> descendants = new HashSet<Member>();
		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);
		boolean flag = false;
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member.getMemberID() == id) {
				queue.clear();
				flag = true;
				queue.add(member);
				break;
			}
			queue.addAll(member.getChildren());
		}
		if (flag) {
			while (queue.size() > 0) {
				Member member = queue.remove(0);
				descendants.addAll(member.getChildren());
				queue.addAll(member.getChildren());
			}
			return descendants;
		} else
			return null;
	}

	/**
	 * 
	 * @param parentID
	 *            -
	 * @param childID
	 */
	/**
	 * Deletes the dependency between the given parent Member and child Member
	 * in the family tree
	 * 
	 * @param parentID
	 *            - parentId of the Member
	 * @param childID
	 *            - childId of the Member
	 * @throws NullPointerException
	 *             - if either of child, parent or both are not present in
	 *             family tree
	 * @throws RuntimeException
	 *             - if there is no direct parent child dependency between the
	 *             members
	 */
	public void deleteDependency(int parentID, int childID)
			throws NullPointerException, RuntimeException {

		Member parentNode = null, childNode = null;

		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member.getMemberID() == parentID) {
				parentNode = member;
			}
			if (member.getMemberID() == childID) {
				childNode = member;
			}
			queue.addAll(member.getChildren());
		}

		if (parentNode == null || childNode == null) {
			throw new NullPointerException();
		}
		if (parentNode.getChildren().contains(childNode)) {
			parentNode.deleteDependency(childNode);
			ArrayList<Member> children = parentNode.getChildren();
			Set<Integer> childrenID = new HashSet<Integer>();
			for (Member m : children)
				childrenID.add(m.getMemberID());
			nodeChildren.put(parentNode, childrenID);
		} else {
			throw new RuntimeException(
					"There is no direct dependancy between parent and child!");
		}

	}

	/**
	 * Deletes the given member in the family tree
	 * 
	 * @param id
	 *            - Member id
	 */
	public void deleteMember(int id) {
		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);

		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member == root && member.getMemberID() == id)
				root = null;
			else {
				ArrayList<Member> children = member.getChildren();
				for (int i = 0; i < children.size(); i++) {
					Member child = children.get(i);
					if (child.getMemberID() == id) {
						member.deleteDependency(child);
						ArrayList<Member> memChildren = member.getChildren();
						Set<Integer> childrenID = new HashSet<Integer>();
						for (Member m : memChildren)
							childrenID.add(m.getMemberID());
						nodeChildren.put(member, childrenID);
					}
				}
			}
			queue.addAll(member.getChildren());
		}
	}

	/**
	 * Returns HashSet of Member, which contains immediate parents of given
	 * Member in family tree
	 * 
	 * @param id
	 *            - Member id
	 * @return - HashSet of Member, which contains immediate parents of given
	 *         Member in family tree
	 */
	public HashSet<Member> getImmediateParents(int id) {
		ArrayList<Member> queue = new ArrayList<Member>();
		queue.add(root);
		HashSet<Member> parents = new HashSet<Member>();
		while (queue.size() > 0) {
			Member member = queue.remove(0);
			if (member == root && member.getMemberID() == id) {
				System.out.println("Given id is root");
				return parents;
			} else {
				ArrayList<Member> children = member.getChildren();
				for (int i = 0; i < children.size(); i++) {
					Member child = children.get(i);
					if (child.getMemberID() == id)
						parents.add(member);
				}
			}
			queue.addAll(member.getChildren());
		}
		return parents;
	}

	/**
	 * Returns HashSet of Member, which contains all the ancestors of given
	 * Member in the family tree
	 * 
	 * @param id
	 * @return - HashSet of Member, which contains all the ancestors of given
	 *         Member in the family tree
	 */
	public HashSet<Member> getAncestors(int id) {
		HashSet<Member> ancestors = new HashSet<Member>();
		ArrayList<Member> immediateParents = new ArrayList<Member>();
		HashSet<Member> hs = getImmediateParents(id);
		ancestors.addAll(hs);
		immediateParents.addAll(hs);
		while (immediateParents.size() > 0) {
			Member member = immediateParents.remove(0);
			hs = getImmediateParents(member.getMemberID());
			ancestors.addAll(hs);
			immediateParents.addAll(hs);
		}
		return ancestors;
	}

	/**
	 * Adds the dependency between the given parent and child in the family tree
	 * 
	 * @param pid
	 *            - parent MemberId
	 * @param cid
	 *            - child MemberId
	 * @throws NullPointerException
	 *             - if the given parent or child not present in the family tree
	 */
	public void addDependency(int pid, int cid) throws NullPointerException {
		Member parent = this.getMember(pid);
		Member child = this.getMember(cid);
		if (parent.getRelationWithRoot() == child.getRelationWithRoot() - 1) {
			parent.addDependency(child);
			Set<Integer> childrenID = nodeChildren.get(parent);
			childrenID.add((Integer) cid);
			nodeChildren.put(parent, childrenID);
		} else {
			s_logger.info("Dependancy cannot be added with member id "
					+ parent.getMemberID());
			System.out.println("Dependency cannot be added with member id "
					+ parent.getMemberID());
		}

	}

	/**
	 * Edits the given member in the family tree
	 * 
	 * @param id
	 *            - Member id
	 * @param name
	 *            - new name that need to be given to member
	 * @param address
	 *            - new address that need to be given to member
	 * @throws NullPointerException
	 *             - if the given parent or child not present in the family tree
	 */
	public void editMember(int id, String name, String address)
			throws NullPointerException {
		Member member = getMember(id);
		member.setAddress(address);
		member.setName(name);
	}

	// This will create a root node and adds to the family tree
	/**
	 * Makes the new root in the empty family tree and returns the family tree
	 * 
	 * @return - FamilyTree.
	 */
	public static FamilyTree makeRoot() {
		FamilyTree fTree = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("Enter id of the root");
			int id = Integer.parseInt(br.readLine());
			System.out.println("Enter name of the root");
			String name = br.readLine();
			System.out.println("Enter address of the root");
			String address = br.readLine();
			fTree = new FamilyTree(id, name, address);

		} catch (IOException e) {
			s_logger.error("IOException, Invalid Input", e);
			System.out.println("Invalid Input");
		} catch (NumberFormatException e) {
			s_logger.error("NumberFormatException", e);
			System.out.println("Invalid Integer Input");
		}
		return fTree;
	}

	public Member getRoot() {
		return root;
	}


}
