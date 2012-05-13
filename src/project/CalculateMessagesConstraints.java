package project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalculateMessagesConstraints {
	
	public static void ConstraintstoCell(ArrayList<Constraint> Constraints_Array,ArrayList<Variable> Variable_Array){
		for(Constraint constraint : Constraints_Array)
		{
			if(constraint != null)
			{
				for(Map.Entry<Integer, VariableMessages> en : constraint.VaraibleMap.entrySet())
				{
					Variable tempVar=Variable_Array.get(en.getKey());
					ConstraintMessages tempConstraintMessage= tempVar.ConstraintMap.get(constraint.id);
					if(tempConstraintMessage != null){
						for(int m:tempVar.DomainMap.keySet())
						{
							BigDecimal tempProb= CalculateProbability(m, constraint.id, en.getKey(), Constraints_Array, Variable_Array);
							tempConstraintMessage.eta_old[m]=tempConstraintMessage.eta_new[m];
							tempConstraintMessage.eta_new[m]=tempProb;
						}
						
						tempVar.ConstraintMap.put(constraint.id,tempConstraintMessage);
						Variable_Array.set(en.getKey(), tempVar);
					}
				}
			}
		}
	}

	public static BigDecimal CalculateProbability(int domainValue, int constraintNumber,int variableNumber,ArrayList<Constraint> Constraints_Array,ArrayList<Variable> Variable_Array)
	{
		Constraint tempConstraint= Constraints_Array.get(constraintNumber);
		/* 0 for all different, 1 for add, 2 for -, 3 for =, 4 for * and 5 for %*/
		switch (tempConstraint.operator){
		case 0:
			if(domainValue>KenKen_3.size)
			{
				return new BigDecimal(0.0);
			}
			HashMap<Integer,Integer> alreadySetValues = new HashMap<Integer,Integer>();
			ArrayList<Integer> varListUnique = new ArrayList<Integer>();
			varListUnique.add(0,variableNumber);
			int iUnique =1;
			for(Map.Entry<Integer,VariableMessages> en : tempConstraint.VaraibleMap.entrySet())
			{
				if(en.getKey()!=variableNumber)
					varListUnique.add(iUnique++,en.getKey());
			}
			
			
			return probUnique(true,tempConstraint,domainValue, varListUnique,alreadySetValues,Variable_Array,Constraints_Array);	
			
		case 1:
			if(tempConstraint.constraint_val < domainValue)
			{
				return new BigDecimal(0.0);
			}
			ArrayList<Integer> varListAdd = new ArrayList<Integer>();
			varListAdd.add(0,variableNumber);
			int iAdd =1;
			for(Map.Entry<Integer,VariableMessages> en : tempConstraint.VaraibleMap.entrySet())
			{
				if(en.getKey()!=variableNumber)
					varListAdd.add(iAdd++,en.getKey());
			}
			
			return prob(true,tempConstraint,domainValue, varListAdd,1,tempConstraint.constraint_val,Variable_Array,Constraints_Array);	
			
		case 2:
			/*if(tempConstraint.constraint_val == domainValue)
			{
				return new BigDecimal(0.0);
			}*/
			ArrayList<Integer> varListSub = new ArrayList<Integer>();
			varListSub.add(0,variableNumber);
			int iSub =1;
			for(Map.Entry<Integer,VariableMessages> en : tempConstraint.VaraibleMap.entrySet())
			{
				if(en.getKey()!=variableNumber)
					varListSub.add(iSub++,en.getKey());
			}
			
			return prob(true,tempConstraint,domainValue, varListSub,2,tempConstraint.constraint_val,Variable_Array,Constraints_Array);	
			
		case 3:
			break;
		case 4:
			if(tempConstraint.constraint_val%domainValue !=0)
			{
				return new BigDecimal(0.0);
			}
			ArrayList<Integer> varList = new ArrayList<Integer>();
			varList.add(0,variableNumber);
			int i =1;
			for(Map.Entry<Integer,VariableMessages> en : tempConstraint.VaraibleMap.entrySet())
			{
				if(en.getKey()!=variableNumber)
					varList.add(i++,en.getKey());
			}
			
			return prob(true,tempConstraint,domainValue, varList,4,tempConstraint.constraint_val,Variable_Array,Constraints_Array);	
			
		case 5:
			ArrayList<Integer> varListDiv = new ArrayList<Integer>();
			varListDiv.add(0,variableNumber);
			int iDiv =1;
			for(Map.Entry<Integer,VariableMessages> en : tempConstraint.VaraibleMap.entrySet())
			{
				if(en.getKey()!=variableNumber)
					varListDiv.add(iDiv++,en.getKey());
			}
			
			return prob(true,tempConstraint,domainValue, varListDiv,5,tempConstraint.constraint_val,Variable_Array,Constraints_Array);	
			
			
		}
		return null;
		
	}


	@SuppressWarnings("unchecked")
	private static BigDecimal prob(boolean isTopLevel, Constraint tempConstraint, int domainValue,
			ArrayList<Integer> varList, int operator, int constraintVal,
			ArrayList<Variable> variableArray,ArrayList<Constraint> Constraints_Array) {
		// TODO Auto-generated method stub
		
		
		VariableMessages tempVarM = tempConstraint.VaraibleMap.get(varList.get(0));
		if(varList.size()==1)
		{
			BigDecimal returnval = new BigDecimal(0.0);
			Variable tempVar = variableArray.get(varList.get(0)); 
			if(constraintVal != domainValue)
			{
				return returnval;
			}
			if(! tempVar.DomainMap.containsKey(constraintVal))
			{
				return returnval;
			}
			
			returnval = tempVarM.pi[constraintVal];
			return returnval;
		}
		
		
		
		//Get the multiplication value 
		BigDecimal returnval = new BigDecimal(0.0);
		
		
		
		
		adjustList(varList);
		// Now take in the next variable in the list 
		Variable nextVariable = variableArray.get(varList.get(0)); 
		//clone so that any subsequent call does not change the array which might be required in another branch 
		// This function will remove the first item in the list and adjust all items accordingly 
		switch (operator)
		{
			case 0:
				break;
				
			case 1: //Addition
				if(constraintVal < domainValue)
				{
					return new BigDecimal(0.0);
				}
				constraintVal -= domainValue;
				
				for(Map.Entry<Integer,Integer> en : nextVariable.DomainMap.entrySet())
				{
					if(constraintVal >=en.getKey())
					{	
						//System.out.println("Constraint " + en.getKey() );
						ArrayList<Integer> varListClone = (ArrayList<Integer>)varList.clone();
						BigDecimal tempReturn= prob(false,tempConstraint,en.getKey(),varListClone, operator, constraintVal,variableArray,Constraints_Array);
						returnval=returnval.add(tempReturn);
					}
				}
				if(!isTopLevel)
				returnval=returnval.multiply(tempVarM.pi[domainValue]);
				
			
				return returnval  ;
		
			case 2://Subtraction
								
				for(Map.Entry<Integer,Integer> en : nextVariable.DomainMap.entrySet())
				{
					VariableMessages nextVar = tempConstraint.VaraibleMap.get(varList.get(0));
					if((en.getKey() == domainValue - constraintVal) || (en.getKey() == constraintVal + domainValue))
					{	
						//System.out.println("Constraint " + en.getKey() );
						returnval = returnval.add(nextVar.pi[en.getKey()]);
					}
				}
				return returnval  ;
				
			case 4: // Multiply 
				if(constraintVal %domainValue != 0)
				{
					return new BigDecimal(0.0);
				}
				constraintVal /= domainValue;
				
				for(Map.Entry<Integer,Integer> en : nextVariable.DomainMap.entrySet())
				{
					if(constraintVal%en.getKey()==0)
					{	
						//System.out.println("Constraint " + en.getKey() );
						ArrayList<Integer> varListClone = (ArrayList<Integer>)varList.clone();
						BigDecimal tempReturn= prob(false,tempConstraint,en.getKey(),varListClone, operator, constraintVal,variableArray,Constraints_Array);
						returnval=returnval.add(tempReturn);
					}
				}
				if(!isTopLevel)
				returnval=returnval.multiply(tempVarM.pi[domainValue]);
				
			
				return returnval  ;
				
			case 5 ://Division
				for(Map.Entry<Integer,Integer> en : nextVariable.DomainMap.entrySet())
				{
					VariableMessages nextVar = tempConstraint.VaraibleMap.get(varList.get(0));
					if((en.getKey() == domainValue/constraintVal) || (en.getKey() == constraintVal * domainValue))
					{	
						//System.out.println("Constraint " + en.getKey() );
						returnval = returnval.add(nextVar.pi[en.getKey()]);
					}
				}
				return returnval  ;
				
		
		}
		return null;
	}


	private static void adjustList(ArrayList<Integer> varList) {
		// TODO Remove the first element and adjust everything accordingly
		varList.remove(0);
		
	}
	
	@SuppressWarnings("unchecked")
	private static BigDecimal probUnique(boolean isTopLevel, Constraint tempConstraint,
			int domainValue, ArrayList<Integer> varList,
			HashMap<Integer, Integer> alreadySetValues, ArrayList<Variable> variableArray,
			ArrayList<Constraint> constraintsArray) {
		
		if(domainValue>KenKen_3.size)
		{
			return new BigDecimal(0.0);
		}
		alreadySetValues.put(domainValue,1);
		// This just does what prob does but is only used by the unique constraint, hence no operator switching
		//and no constraint value matching. This is just a combination probability multiplier 
		VariableMessages tempVarM = tempConstraint.VaraibleMap.get(varList.get(0));
		if(varList.size()==1)
		{
			BigDecimal returnval = new BigDecimal(0.0);
			Variable tempVar = variableArray.get(varList.get(0)); 
			
			if(! tempVar.DomainMap.containsKey(domainValue))
			{
				return returnval;
			}
			
			returnval = tempVarM.pi[domainValue];
			return returnval;
		}
		
		BigDecimal returnval = new BigDecimal(0.0);
		// Remove the first element from the list and adjust the remaining list 
		adjustList(varList);
		// Now take in the next variable in the list 
		Variable nextVariable = variableArray.get(varList.get(0)); 
		//clone so that any subsequent call does not change the array which might be required in another branch 
		// This function will remove the first item in the list and adjust all items accordingly 
		
		for(Map.Entry<Integer,Integer> en : nextVariable.DomainMap.entrySet())
		{
			if(!alreadySetValues.containsKey(en.getKey()))
			{	
				//System.out.println("Constraint " + en.getKey() );
				ArrayList<Integer> varListClone = (ArrayList<Integer>) varList.clone();
				HashMap<Integer,Integer> alreadySetValuesClone = (HashMap<Integer, Integer>) alreadySetValues.clone();
				BigDecimal tempReturn= probUnique(false,tempConstraint,en.getKey(),varListClone,alreadySetValuesClone, variableArray,constraintsArray);
				returnval=returnval.add(tempReturn);
			}
		}
		if(!isTopLevel)
		{
			returnval=returnval.multiply(tempVarM.pi[domainValue]);
		}
		return returnval ;
			
		
	}


}
