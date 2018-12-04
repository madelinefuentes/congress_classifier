//decision tree classifier for congressional votes

import java.io.*;
import java.util.*;
import java.math.*;

public class CongressDec{
  
  public static void main(String argv[]){
    //root of decision tree
	VoteNode head = new VoteNode(0);
	head.prevVotes = new ArrayList<>();
	head.votes = parseVoteList(argv[0]);
	
	Queue<VoteNode> buildTree = new LinkedList<>();
	buildTree.add(head);
	
	//build decision classification tree
	//divide data according to vote attribute with best info gain
	while(!buildTree.isEmpty()){
	  VoteNode node = buildTree.remove();
	  int[] memCount = node.countMem();

	  if(memCount[0] == 0)
		  continue;
	  
	  if(memCount[1] == 0)
		  continue;
	  
	  if(node.prevVotes.size() == 42)
		continue;
	
	  int split = node.getBestAtrribute();
	  node.vote = split;
		
	  VoteNode voteYea = new VoteNode(node);
	  VoteNode voteNay = new VoteNode(node);
	  voteYea.votes = node.split(split, "Yea");
	  voteNay.votes = node.split(split, "Nay");
	  
	  node.yeaNext = voteYea;
	  node.nayNext = voteNay;
	  
	  buildTree.add(voteYea);
	  buildTree.add(voteNay);
	}
	
	try{
	  //use decision tree classifier on test data
	  Scanner input = new Scanner(new File(argv[1]));
	  String line = "";
	  String splitComma = ","; 
 
	  while (input.hasNext()) {
		line = input.nextLine();
		String[] vote = line.split(splitComma);
        VoteNode search = head;
		
		while(search.yeaNext != null){
		  int attr = search.vote;
	      if(vote[attr].equals("Yea")){
			int[] nextMemCount = search.yeaNext.countMem();
			if(nextMemCount[0] == 0 && nextMemCount[1] == 0)
			  break;
		    else
			  search = search.yeaNext;
		  }
		  
		  else{ //vote is nay
		    int[] nextMemCount = search.nayNext.countMem();
			if(nextMemCount[0] == 0 && nextMemCount[1] == 0)
			  break;
		    else
			  search = search.nayNext;
		  }
	    }
		
		int[] memCount = search.countMem();
		double dem = memCount[0];
		double rep = memCount[1];
		
		if(dem > rep)
		  System.out.println("Democrat," + dem/(dem + rep));
	  
	    else
		  System.out.println("Republican," + rep/(dem + rep));
      }
    }
	catch (FileNotFoundException except) {
	  except.printStackTrace();
    }
  }
  
  //read votes from data file into list
  public static ArrayList parseVoteList(String data){
	ArrayList voteList = new ArrayList<>();
	String line = "";
	String splitComma = ",";
	try{
	  Scanner input = new Scanner(new File(data));
	  while (input.hasNext()) {
		line = input.nextLine();
        String[] vote = line.split(splitComma);
		voteList.add(vote);
      }
    } catch (FileNotFoundException except) {
	  except.printStackTrace();
    }
	return voteList;
  } 
 }