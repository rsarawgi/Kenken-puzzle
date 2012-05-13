package project;

import java.io.BufferedInputStream;
//import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
//import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.SpringLayout.Constraints;

public class KenKen_3 {
	public static int size;
	static int num_constraint;
	static ArrayList<Constraint> Constraints_Array;
	static ArrayList<Variable> Variable_Array ;
	static int column_size ;
	static int row_size;
	public static ArrayList<Integer> variableValues ;
	public static int temp_assign[] ; 
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		
		for(int sizeCounter=9;sizeCounter <=9;sizeCounter++)
		{	
			FileWriter fstream = new FileWriter("KenKen_"+sizeCounter+"_Output.txt");
	        BufferedWriter out = new BufferedWriter(fstream);
			for(int fileCounter=4; fileCounter <=20; fileCounter+=4)
			{size=sizeCounter;
				Constraints_Array = new ArrayList<Constraint>();
				Variable_Array = new ArrayList<Variable>();
				column_size = (size*size) + 2;
				row_size = size*size*size;
				variableValues = new ArrayList<Integer>();
				temp_assign = new int[size*size+1]; 		
				      
			int [][] clauses = new int [row_size][column_size+1];
				//int [][] clauses = new int[12][12];
				File file = new File("InputFiles\\KenKen_"+size+"_"+fileCounter+".txt");
				FileInputStream fis = null;
			    BufferedInputStream bis = null;
			    DataInputStream dis = null;
			    int i=1, j=0;
			    String temp;
			    String [] splits ;
			    
			    try {
			      fis = new FileInputStream(file);
		
			      // Here BufferedInputStream is added for fast reading.
			      bis = new BufferedInputStream(fis);
			      dis = new DataInputStream(bis);
		
			      // dis.available() returns 0 if the file does not have more lines.
			      while (dis.available() != 0) {
			    	  	i=0;j++;
			      // this statement reads the line from the file and print it to
			        // the console.
			       temp=dis.readLine();
			      // int k =0; 
			       splits=temp.split(" ");
			       while(i< splits.length)
			       {
			    	   clauses[j][i+1]= Integer.valueOf(splits[i++]);
			       }
			       
			      }
		           num_constraint = j;
			      // dispose all the resources after using them.
			      fis.close();
			      bis.close();
			      dis.close();
			      
			     for (int k=1;k<=num_constraint;k++){
			    	 for (int m=1; m<=column_size;m++){
			    		 System.out.print(clauses[k][m]);
			    	 }
			    	 System.out.println();
			    	 
			     }
		
			    } catch (FileNotFoundException e) {
			      break;
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			    //the following two loops are just to initialize the arraylist 
			    //Note to self : this is why you use hashmaps but in a densely populated array like this 
			    //its  probably better to use Arrays 
			    for(i=0; i<=num_constraint;i++)
			    {
			    	Constraint newConstraint = new Constraint(i);
			    	Constraints_Array.add(i,newConstraint);
			    }
			    for (j=0;j<=column_size-2;j++)
			    {
			    	Variable newVariable = new Variable(j);
			    	Variable_Array.add(j,newVariable);
			    	
			    }
			    
			    for(int l =0; l<=(size*size);l++){
			    	variableValues.add(l,-1);
			    }
			    
			    for(int q=1;q<=num_constraint;q++)
			    {
			    	Constraint newConstraint = new Constraint(q);
			    	Variable tempVariable;
			    	 int p =0;
			    	 for (p=1;p <=(column_size-2);p++)
			    	 {	
			    		 if(clauses[q][p]!=0)
			    		 {
			    			 tempVariable = (Variable)Variable_Array.get(p);
			    			 ConstraintMessages newConstraintMessage = new ConstraintMessages();
		                     tempVariable.ConstraintMap.put(q,newConstraintMessage);
		                     Variable_Array.set(p, tempVariable);
		                     
		                     VariableMessages newVariableMessage = new VariableMessages();
			    			 newConstraint.VaraibleMap.put(p,newVariableMessage);
			    			 newConstraint.operator = clauses[q][column_size-1];
			    			 newConstraint.constraint_val = clauses[q][column_size];
			    			 
			    		 }	    		 
			    		 }
				    	
			    	 Constraints_Array.set(q,newConstraint);
				     }
			    	      
				   /* for(Constraint c: Constraints_Array)
					{
						//c.printConstraints();
					}
				   // for(Variable v : Variable_Array)
				    	//v.printVariable();
*/				    
				    
				   
				    int t=40;
				    int loopCounter=0;
				    int IterationCounter=0;
				    int ConvergeCounter=0;
				    remove.setAssertion(Constraints_Array, Variable_Array);
				    while (loopCounter < size*size)
				    {	
				    	for (int b=1; b<=size*size;b++){
							System.out.print(temp_assign[b]+"\t");
						}
				    	loopCounter++;
				    	refresh();
				    	System.out.println("Inside loop :"+loopCounter);
					    t =Loop(0 ,Constraints_Array,Variable_Array);
					    if(t<40)
					    {
					    	ConvergeCounter++;
					    }
					    IterationCounter+=t;
					    boolean EverythingNonZero =compute_bias(Variable_Array);
					    /*if(!EverythingNonZero)
					    {
					    	System.out.println("The solution cannot be found because");
		    				for (int b=1; b<=size*size;b++){
		    					System.out.println(variableValues.get(b).toString());
		    				}
		    				System.exit(0);
					    }*/
					    boolean solutionwithTemp =check_soln(Constraints_Array);
		    			if(solutionwithTemp){
		    				System.out.println("Solution found");
		    				out.write("1");
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter-1));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(ConvergeCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(IterationCounter));
		    		    	out.write("\t");
		    		    	
		    				for (int b=1; b<=size*size;b++){
		    					out.write(temp_assign[b]+"\t");
		    				}
		    				out.write("\n");
					    break;
		    			}
		    			Variable highestVariable= fix_variable();
		    			
		    			if(highestVariable==null || temp_assign[highestVariable.id]==0)
		    			{
		    				System.out.println("The solution cannot be found");
		    				out.write("0");
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(ConvergeCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(IterationCounter));
		    		    	out.write("\t");
		    		    	
		    				for (int b=1; b<=size*size;b++){
		    					out.write(temp_assign[b]+"\t");
		    				}
		    				out.write("\n");
		    				break;
		    			}
		    			
		    			variableValues.set(highestVariable.id, temp_assign[highestVariable.id]);
		    			boolean Inconsistent =remove.removeVar(highestVariable.id,temp_assign[highestVariable.id], Constraints_Array, Variable_Array);
		    			Variable_Array.set(highestVariable.id,null);
		    			if(Inconsistent== false){
		    				System.out.println("The solution cannot be found");
		    				
		    				out.write("0");
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(loopCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(ConvergeCounter));
		    		    	out.write("\t");
		    		    	out.write(String.valueOf(IterationCounter));
		    		    	out.write("\t");
		    		    	
		    				for (int b=1; b<=size*size;b++){
		    					out.write(temp_assign[b]+"\t");
		    				}
		    				out.write("\n");
		    				break;
		    			}
				    }
				    
			}
		out.close();
		}
		    
   
	    }
		    private static int Loop (int t ,ArrayList<Constraint> Constraints_Array, ArrayList<Variable> Variable_Array){
		    	boolean converged = false;
		    		while(converged != true && t < 40)
		    		{
		    			t++;
		    			//System.out.println("Iteration"  +t );
		    			
		    			CalculateMessages.CalculateMessagesVariables(Constraints_Array,Variable_Array);	
		    			CalculateMessagesConstraints.ConstraintstoCell(Constraints_Array, Variable_Array);
		    			converged = check_convergence(Variable_Array);
		    			
		    			}
		    		
		    		return t;
		    	
		    	}
		    
		    
		    private static void refresh() {
				 for(int i=1; i<=KenKen_3.column_size-2;i++)
				    {
				    					    	
				    	Variable tempVar = Variable_Array.get(i);
				    	if(tempVar != null)
				    	{
				    		for(int j=1;j<=KenKen_3.num_constraint;j++)
				    		{
				    			ConstraintMessages tempConstraintMessage = tempVar.ConstraintMap.get(j);
				    			if(tempConstraintMessage != null)
				    			{	BigDecimal One= new BigDecimal(1.0);
				    				for(int p=1; p<=KenKen_3.size;p++){
				    					
										tempConstraintMessage.eta_new[p]= One.divide(new BigDecimal(KenKen_3.size+1),10,BigDecimal.ROUND_CEILING);
				    				}
				    				
				    			}
				    			tempVar.ConstraintMap.put(j,tempConstraintMessage);
				    		}
				    		Variable_Array.set(i,tempVar);
				    	}
				    	

				    }
				
			}
	
	//convergence check
	public static boolean check_convergence(ArrayList<Variable> Variable_Array)
	{
		//for every variable
		for(Variable var :Variable_Array)
		{
			//for every constraint
			if(var != null){
				for(Integer constraintNumber : var.ConstraintMap.keySet())
				{
					if(var.ConstraintMap.get(constraintNumber) != null)
					{
					//for every domain value
						//but only for the ones that exist 
						for(int m : var.DomainMap.keySet())
						{
							BigDecimal newEta=var.ConstraintMap.get(constraintNumber).eta_new[m];
							BigDecimal oldEta=var.ConstraintMap.get(constraintNumber).eta_old[m];
							BigDecimal diff= newEta.subtract(oldEta);
							BigDecimal reverse = oldEta.subtract(newEta);
							//check if the etanew-etaold > 0.001.Even if one case is true, return false
							if(diff.doubleValue()>0.001 || reverse.doubleValue() > 0.001)
								return false;
						}
						
					}
				}
			}
		}
		//all eta messages converged, hence return true
		return true;
	}

	//For every variable, compute max bias over all domain values and assign right domain value in temp_assign
	public static boolean compute_bias(ArrayList<Variable> Variable_Array)
	{
		//for every variable
		for(Variable var: Variable_Array)
		{
			if(var != null && var.id>0){//over all domain //RS: which exists 
				for(int m:var.DomainMap.keySet())
				{
					//compute the product of eta messages got from all constraints
					BigDecimal productBias = new BigDecimal(1.0);
					for(Integer constraintNumber : var.ConstraintMap.keySet())
					{
						if(var.ConstraintMap.get(constraintNumber)!=null)
						{
							productBias =   productBias.multiply(var.ConstraintMap.get(constraintNumber).eta_new[m]);
					
						}
					}	
					var.bias[m] = productBias;
				}
				

			//find the domain value with highest bias // RS: only for the domains that exist 
				int dom_val = 0;
			    for(int m:var.DomainMap.keySet())
				{
					if(var.bias[m].compareTo(var.max_bias) > 0)
					{
						var.max_bias = var.bias[m];
						dom_val = m;
					}
				}
	            
				temp_assign[var.id] = dom_val;
				if(dom_val==0)
					return false;
				
		}

		}
		
		return true;
	}

	//Method to check if the assigned values produce soln
	public static boolean check_soln(ArrayList<Constraint> Constraint_Array)
	{
		for(Constraint constraint : Constraint_Array)
		{
			if(constraint != null)
			{
				if(!constraint.satisfy())
					return false;
			}
			
		}
		return true;
	}
	//Method to find a variable to fix if temp assignments did not find a soln
	public static Variable fix_variable()
	{
		Variable fix = null;
		BigDecimal max = new BigDecimal(0.0);
		for(Variable var : Variable_Array)
		{
			if(var!=null && var.max_bias.compareTo(max) > 0 && var.id>0)
			{
				fix = var;
			}
		}
		return fix;
		//to fetch the value assigned, use temp_assign[fix.id]
	}

	
	    
	    
	}


