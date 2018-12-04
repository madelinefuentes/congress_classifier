//naive bayes classifier for congressional votes

import java.io.*;
import java.util.*;
import java.math.*;

public class CongressNBC{
  
  public static void main(String argv[]){
	int[][] counts = parseVotes(argv[0]); 
	
	double dem = counts[42][0];
	double rep = counts[42][1];
	
	double demProb = dem/(dem + rep);
	double repProb = rep/(dem + rep);
	
	//get total number of congress members
	double members = (dem + rep);
	
	//table of conditional probabilities
	//[P(votei = yea|Dem)][P(votei = nay|Dem)
	BigDecimal[][] demProbTable = new BigDecimal[42][42];
	BigDecimal[][] repProbTable = new BigDecimal[42][42];
	    

	//laplace smoothing for zero yea/nay votes
	boolean addDemYea = false;
	boolean addDemNay = false;

	boolean addRepYea = false;
	boolean addRepNay = false;
	
	for(int i = 0; i < 42; i++){
	  //zero yea votes for votei from democrats
	  if(counts[i][0] == 0)
        addDemYea = true;
	
	  //zero nay votes for votei from democrats
	  if(counts[i][0] == dem)
        addDemNay = true;
	
	  //zero yea votes for votei from republicans
	  if(counts[i][1] == 0)
	    addRepYea = true;
	
	  //zero nay votes for votei from democrats
	  if(counts[i][1] == rep)
        addRepNay = true;
	}
		
	//non-yea votes will count as nay
	for(int i = 0; i < 42; i++){
	  if(addDemYea)
	    demProbTable[i][0] = new BigDecimal((counts[i][0] + 1)/(dem + 2));
      else
		demProbTable[i][0] = new BigDecimal(counts[i][0]/dem);
	
	  if(addDemNay)
        demProbTable[i][1] = new BigDecimal(((dem - counts[i][0]) + 1)/(dem + 2)); 
	  else
		demProbTable[i][1] = new BigDecimal((dem - counts[i][0])/dem);
	
	  if(addRepYea)
		repProbTable[i][0] = new BigDecimal((counts[i][1] + 1)/(rep + 2));
	  else
	    repProbTable[i][0] = new BigDecimal(counts[i][1]/rep);
	
	  if(addRepNay)
	    repProbTable[i][1] = new BigDecimal((rep - counts[i][1] + 1)/(rep + 2));
	  else
		repProbTable[i][1] = new BigDecimal((rep - counts[i][1])/rep);
	}
	     
	try{
	  //calculate probabilities of test data
	  Scanner input = new Scanner(new File(argv[1]));
	  String line = "";
	  String splitComma = ","; 

	  while (input.hasNext()) {
		line = input.nextLine();
		String[] vote = line.split(splitComma);
        
		BigDecimal demProbTest = new BigDecimal(demProb);
		BigDecimal repProbTest = new BigDecimal(repProb);
		
        for(int i = 0; i < 42; i++){
		  if(vote[i].equals("Yea")){
			demProbTest = demProbTest.multiply(demProbTable[i][0]);
			repProbTest = repProbTest.multiply(repProbTable[i][0]);
		  }
		  
		  else{
			demProbTest = demProbTest.multiply(demProbTable[i][1]);
			repProbTest = repProbTest.multiply(repProbTable[i][1]);
		  }
		}
		
		//normalize probabilities
		BigDecimal norm = demProbTest.add(repProbTest);
		demProbTest = demProbTest.divide(norm, MathContext.DECIMAL128);
		repProbTest = repProbTest.divide(norm, MathContext.DECIMAL128);

		if(demProbTest.compareTo(repProbTest) > 0){
		  System.out.print("Democrat,");
		  System.out.printf("%.10f", demProbTest);
		}
		
		else{
		  System.out.print("Republican,");
		  System.out.printf("%.10f", repProbTest);
		}
		
		System.out.println("");
      }
    }
	catch (FileNotFoundException except) {
	  except.printStackTrace();
    }			  
  }
  
  //count number of each yea vote for democrats/republicans in training data
  //each line of array represents counts for each vote(vote1, vote2, etc...)
  //last two lines of array is number of democrats and republicans
  public static int[][] parseVotes(String data){
	int[][] voteCount = new int[43][43];
	String line = "";
	String splitComma = ",";
	try{
	  Scanner input = new Scanner(new File(data));
	  
	  int member;
	  while (input.hasNext()) {
		line = input.nextLine();
        String[] vote = line.split(splitComma);
		
		//0 -> Democrat, 1 -> Republican 
		if(vote[42].equals("Democrat"))
		  member = 0;

		else  
		  member = 1;
	  
	    voteCount[42][member]++;
        for(int i = 0; i < 42; i++){
		  if(vote[i].equals("Yea"))
			voteCount[i][member]++;	
		}
      }
    } catch (FileNotFoundException except) {
	  except.printStackTrace();
    }
	return voteCount;
  } 
}