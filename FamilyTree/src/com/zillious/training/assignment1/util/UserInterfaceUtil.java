package com.zillious.training.assignment1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zillious.training.assignment1.dao.DataIO;
import com.zillious.training.assignment1.dao.impl.FileIO;
import com.zillious.training.assignment1.entity.Member;

public class UserInterfaceUtil {

	private String filePath= "D:/familyTree.ser";
	
	private static Logger s_logger = Logger.getLogger(UserInterfaceUtil.class);
	
	public void printMenu() {
		System.out.println("-------------options-----------------");
		System.out.println("0. Save into file and Exit");
		System.out
				.println("1. Get Immediate Parents of a particular member");
		System.out
				.println("2. Get Immediate Children of a particular member");
		System.out.println("3. Get Ancestors of a particular member");
		System.out.println("4. Get Descendants of a particular member");
		System.out
				.println("5. Delete Dependency between two members of a tree");
		System.out.println("6. Delete member of a tree");
		System.out
				.println("7. Add Dependency between two members of a tree");
		System.out.println("8. Add new member to the tree");
		System.out.println("9. Edit the member of the tree");
		System.out.println("10. Get Details of the member");
		System.out.println("11. Print Tree");
	}
	public void perform(int option,FamilyTree familyTree)
	{
		DataIO dao=null;
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		try {
			//option = Integer.parseInt(br.readLine());
			int id;
			switch (option) {
			case 0:
				dao = new FileIO(filePath);
				dao.saveObject(familyTree);
				System.exit(0);
				break;
			case 1:
				System.out
						.println("Enter id of member whose immediate parents are needed!");
				id = Integer.parseInt(br.readLine());
				printImmediateParents(familyTree, id);
				break;
			case 2:
				System.out
						.println("Enter id of member whose immediate children are needed!");
				id = Integer.parseInt(br.readLine());
				printImmediateChildren(familyTree, id);
				break;
			case 3:
				System.out
						.println("Enter id of member whose ancestors are needed!");
				id = Integer.parseInt(br.readLine());
				printAncestors(familyTree, id);
				break;
			case 4:
				System.out
						.println("Enter id of member whose descendants are needed!");
				id = Integer.parseInt(br.readLine());
				PrintDescendants(familyTree, id);
				break;
			case 5:
				System.out
						.println("Enter id of parent member and child member whose dependency "
								+ "should be remove");
				System.out.println("Enter parent id:");
				int pid = Integer.parseInt(br.readLine());
				System.out.println("Enter child id:");
				int cid = Integer.parseInt(br.readLine());
				deleteDependency(familyTree, pid, cid);
				break;
			case 6:
				System.out
						.println("Enter id of the member which should be removed from the tree");
				id = Integer.parseInt(br.readLine());
				deleteMember(familyTree, id);
				break;
			case 7:
				System.out
						.println("Enter id of parent member and child member whose dependency "
								+ "should be added");
				System.out.println("Enter parent id:");
				pid = Integer.parseInt(br.readLine());
				System.out.println("Enter child id:");
				cid = Integer.parseInt(br.readLine());
				addDependency(familyTree, pid, cid);
				break;
			case 8:
				if (familyTree.getRoot() != null) {
					System.out
							.println("Enter id of a new member that should be added to the tree "
									+ "and the parent id to which it should be connected");
					System.out.println("Enter new child id:");
					cid = Integer.parseInt(br.readLine());
					addMember(familyTree, cid);
				} else {
					familyTree = FamilyTree.makeRoot();
				}
				break;
			case 9:
				System.out
						.println("Enter id of the member that need to be edited");
				id = Integer.parseInt(br.readLine());
				editMember(familyTree, id);
				break;
			case 10:
				System.out
						.println("Enter id of the member that need to be retrieved");
				id = Integer.parseInt(br.readLine());
				getMember(familyTree, id);
				break;
			case 11:
				System.out
						.println("--------------current tree--------------");
				familyTree.breadthFirstTraversal();
				break;
//			case 12:
//				dao = new FileIO(filePath);
//				dao.saveObject(familyTree);
//				dao = DatabaseIO.getInstance();
//				dao.saveObject(familyTree);
//				System.exit(0);
//				break;
//			case 13:
//				dao = DatabaseIO.getInstance();
//				dao.saveObject(familyTree);
//				System.exit(0);
//				break;
			default:
				System.out.println("Please Enter correct Menu item number");
				break;
			}

		} catch (IOException e) {

			s_logger.error("IOException", e);
			System.out.println("Invalid Input");
		} catch (NumberFormatException e) {
			s_logger.error("NumberFormatException", e);
			System.out.println("Invalid Integer Input");
		} catch (NullPointerException e) {
			s_logger.error("Null Exception", e);
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}
	}
	
	private  void getMember(FamilyTree familyTree, int id) {
		try {
			Member member = familyTree.getMember(id);
			System.out.println("Name of the member : " + member.getName());
			System.out.println("Address : " + member.getAddress());
		} catch (NullPointerException e) {
			s_logger.error(
					"Null Exception due to not presence of id in the tree ", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}

	}

	private  void editMember(FamilyTree familyTree, int id) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("Enter name of the member");
			String name = br.readLine();
			System.out.println("Enter address of the member");
			String address = br.readLine();
			familyTree.editMember(id, name, address);
			System.out.println("Edited Succesfully!!");

		} catch (IOException e) {
			s_logger.error("IOException", e);
			System.out.println("Invalid Input");
		} catch (NullPointerException e) {
			s_logger.error(
					"Null Exception due to not presence of id in the tree ", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}
	}

	private  void addMember(FamilyTree familyTree, int cid) {
		Member member = familyTree.getMember(cid);
		if (member != null) {
			System.out.println("Already this id id present in family tree");
			s_logger.info("Already this id id present in family tree");
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.println("Enter name of the member");
			String name = br.readLine();
			System.out.println("Enter address of the member");
			String address = br.readLine();
			System.out
					.println("Enter parent id's comma separated to which this child should be added:");
			String[] parents = br.readLine().split(",");
			boolean added = false;
			for (int i = 0; i < parents.length; i++) {
				int parentId = Integer.parseInt(parents[i]);
				Member parent = familyTree.getMember(parentId);
				if (parent != null) {
					if (!added) {
						familyTree.addMember(cid, name, address, parent);
						added = true;
					} else {
						familyTree.addDependency(parentId, cid);
					}
				}
			}
			System.out.println("Added Suucesfully!!");

		} catch (IOException e) {
			s_logger.error("IOException", e);
			System.out.println("Invalid Input");
		} catch (NullPointerException e) {
			s_logger.error(
					"Null Exception due to not presence of id in the tree ", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}

	}

	private  void addDependency(FamilyTree familyTree, int pid, int cid) {

		try {
			familyTree.addDependency(pid, cid);
			System.out.println("Added Successfully!!");
		} catch (NullPointerException e) {
			s_logger.error(
					"Null Exception due to not presence of id in the tree ", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}

	}

	private  void deleteMember(FamilyTree familyTree, int id) {
		try {
			familyTree.deleteMember(id);
			System.out.println("Deleted succesfully");
		} catch (NullPointerException e) {
			s_logger.error("given node is not present in tree", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}
	}

	private  void deleteDependency(FamilyTree familyTree, int pid, int cid) {
		try {
			familyTree.deleteDependency(pid, cid);
			System.out.println("Deleted succesfully");
		} catch (NullPointerException e) {
			s_logger.error(
					"Either of given members or both are not present in tree",
					e);
			System.out
					.println("Either of given members or both are not present in tree!");
		} catch (Exception e) {
			s_logger.error(e);
			System.out.println(e.getLocalizedMessage());
		}
	}

	private  void PrintDescendants(FamilyTree familyTree, int id) {
		try {
			Set<Member> set = familyTree.getDescendants(id);
			for (Member m : set)
				System.out.print(m.getMemberID() + " ");
			System.out.println();
		} catch (NullPointerException e) {
			s_logger.error("given node is not present in tree", e);
			System.out.println("Given Id node is not present in the tree!");
		}
	}

	private  void printAncestors(FamilyTree familyTree, int id) {
		try {
			Set<Member> set = familyTree.getAncestors(id);
			if (familyTree.getMember(id) == null) {
				System.out.println("Given Id is not present in tree");
			}
			for (Member m : set)
				System.out.print(m.getMemberID() + " ");
			System.out.println();
		} catch (NullPointerException e) {
			s_logger.error("given node is not present in tree", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}

	}

	private  void printImmediateChildren(FamilyTree familyTree, int id) {
		try {
			ArrayList<Member> list = familyTree.getImmediateChildren(id);
			for (Member m : list)
				System.out.print(m.getMemberID() + " ");
			System.out.println();
		} catch (NullPointerException e) {
			s_logger.error("given node is not present in tree", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}
	}

	private  void printImmediateParents(FamilyTree familyTree, int id) {
		try {
			if (familyTree.getMember(id) == null) {
				System.out.println("Given Id is not present in tree");
			}
			Set<Member> set = familyTree.getImmediateParents(id);
			for (Member m : set)
				System.out.print(m.getMemberID() + " ");
			System.out.println();
		} catch (NullPointerException e) {
			s_logger.error("given node is not present in tree", e);
			System.out.println("Given Id node is not present in the tree!");
		} catch (Exception e) {
			s_logger.error("Exception", e);
		}

	}

}

