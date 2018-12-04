//vote node for decision classification tree

public class VoteNode{
	
  int vote;
  ArrayList<Integer> prevVotes;
  ArrayList<String[]> votes;
  public VoteNode yeaNext;
  public VoteNode nayNext;
  
  public VoteNode(VoteNode prev){
	ArrayList newList = new ArrayList<>();
	for(Integer add: prev.prevVotes)
	  newList.add(add);
    newList.add(prev.vote);
	this.prevVotes = newList;
  }
  
  public VoteNode(int vote){
	this.vote = vote;
  }
  
  //exclude data to only include a yea or nay vote from a certain bill
  public ArrayList split(int voteNum, String yeaNay){
	ArrayList newList = new ArrayList<>();
	for (String[] vote: votes){
	  if(vote[voteNum].equals(yeaNay))
		newList.add(vote);
	}
	return newList;
  }
  
  //return number of democrats and republicans in data set
  //[0] - democrats, [1] - republicans
  public int[] countMem(){
	int[] members = new int[2];
	for (String[] vote: votes){
	  if(vote[42].equals("Democrat"))
		members[0]++;
	  else
		members[1]++;
	}
	return members;
  }
  
  //count how many democrats and republicans voted yea or nay for votei
  //[0] - yea democrat votes, [1] - yea republican votes
  //[2] - nay democrat votes, [3] - nay republican votes
  public int[] countDemRep(int votei){	
	int[] count = new int[4];
	for (String[] vote: votes){
	  String member = vote[42];
	  if(vote[votei].equals("Yea") && member.equals("Democrat"))
		count[0]++;
		
	  else if(vote[votei].equals("Yea") && member.equals("Republican"))
		count[1]++;
		
	  else if(member.equals("Democrat"))
		count[2]++;
		
	  else 
		count[3]++;
	}
	return count;
  }
  
  //calculate information gain for each vote to choose best vote to split on
  public int getBestAtrribute(){
	int best = 500;
	double bestGain = 500;
	for(int i = 0; i < 42; i++){
      if(!prevVotes.contains(i)){
		int[] count = countDemRep(i);
		double demYea = count[0];
		double repYea = count[1];
		double demNay = count[2];
		double repNay = count[3];  
	  
	    double entropyYea;
		double entropyNay;
	    if(demYea < 1 || repYea < 1)
		  entropyYea = 0;
	    else{
		  entropyYea = -((demYea/(demYea + repYea))* 
			                (Math.log(demYea/(demYea + repYea))/Math.log(2)))
					   -((repYea/(demYea + repYea))* 
			                (Math.log(repYea/(demYea + repYea))/Math.log(2)));
		}
        
		if(demNay < 1 || repNay < 1)
		  entropyNay = 0;
	    else{
      	  entropyNay = -((demNay/(demNay + repNay))* 
			                (Math.log(demNay/(demNay + repNay))/Math.log(2)))
					    -((repNay/(demNay + repNay))* 
			                (Math.log(repNay/(demNay + repNay))/Math.log(2)));
		}
								
		double yeaAndNay = demYea + repYea + demNay + repNay;
	    double infoGain = (((demYea + repYea)/yeaAndNay) * entropyYea)
	                  + (((demNay + repNay)/yeaAndNay) * entropyNay);
					  
	    if(infoGain < bestGain){
		  bestGain = infoGain;
		  best = i;
		}
	  }
	}	
	return best;
  }
}