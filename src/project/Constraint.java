package project;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Constraint {
	public int operator;
	public int id;
	public int constraint_val;
	
	public Map<Integer, VariableMessages> VaraibleMap;
	
	Constraint(int id){
		operator = 0;
		constraint_val =0;
		this.id=id;
		VaraibleMap= new HashMap<Integer,VariableMessages>();
       
	}
	
	public boolean remove(int variableNumber, int domainValue)
	{
	
		switch (operator)
		{
		case 0://Unique
			
			this.VaraibleMap.remove(variableNumber);
			 if(this.VaraibleMap.size()==0)
				 {
				 KenKen_3.Constraints_Array.set(id, null);
				 Variable.removeConstraintFromVariables(id,KenKen_3.Variable_Array);
				 return true;
			   }
			for(Map.Entry<Integer,VariableMessages> en : VaraibleMap.entrySet())
			{
				 Variable tempVar = KenKen_3.Variable_Array.get(en.getKey());
				 tempVar.DomainMap.remove(domainValue);
				 
				 if(tempVar.DomainMap.size()==0)
				 {
					 return false;// Inconsistent solution found ..since this variable has no more values left to be assigned
				 }
				 
				
				 KenKen_3.Variable_Array.set(en.getKey(), tempVar);
			
			}
			
			break;
		case 1://Add
			constraint_val-=domainValue;
			this.VaraibleMap.remove(variableNumber);
			if(VaraibleMap.size()==0)
			{
				if(constraint_val==0)
				{
					KenKen_3.Constraints_Array.set(id, null);
					Variable.removeConstraintFromVariables(id,KenKen_3.Variable_Array);
					return true;
					// Constraint satisfied remove it 
				}
				else
				{
					//Solution is inconsistent. This constraint has no variable and is not satisfied
					return false;
				}
			}
			break;
		case 2://Subtract
			this.VaraibleMap.remove(variableNumber);
			for(Map.Entry<Integer,VariableMessages> en : VaraibleMap.entrySet())
			{// A for loop here appears costly since there will always be only one more variable 
				// but you will anyways have to do it since you don know what the next guy is 
				VariableMessages tempVarMessage = en.getValue();
				Variable tempVar = KenKen_3.Variable_Array.get(en.getKey());
				int domainValueToBeSet;
				if(tempVar.DomainMap.containsKey((domainValue -constraint_val)) && tempVar.DomainMap.containsKey((domainValue +constraint_val)) )
				{//Set the nextVar as the highest probabilty of the two poss values
					
					if(tempVarMessage.pi[domainValue -constraint_val].compareTo(tempVarMessage.pi[domainValue +constraint_val]) > 0)
					{
						domainValueToBeSet= domainValue -constraint_val;
					}
					else
					{
						domainValueToBeSet= domainValue +constraint_val;
					}
				}
				else if(tempVar.DomainMap.containsKey((domainValue -constraint_val)))
				{
					domainValueToBeSet= domainValue -constraint_val;
				}
				else if(tempVar.DomainMap.containsKey((domainValue +constraint_val)))
				{
					domainValueToBeSet= domainValue +constraint_val;
				}
				else{
					// We cannot find a value. Inconsistent solution 
					return false;
					
				}
				//TODO
				// TODO : Set domainValueToBeSet for this variable
				KenKen_3.variableValues.set(en.getKey(), domainValueToBeSet);
				//TODO
				KenKen_3.Constraints_Array.set(id, null);
				Variable.removeConstraintFromVariables(id,KenKen_3.Variable_Array);
				//Call main remove for this variable and this domainValueToBeSet 
				remove.removeVar(en.getKey(),domainValueToBeSet,KenKen_3.Constraints_Array,KenKen_3.Variable_Array);
				
				return true;
				
				
			
			}
			
			break;
		case 3://Assignment
			break;
		case 4://Multiply
			constraint_val/=domainValue;
			this.VaraibleMap.remove(variableNumber);
			if(VaraibleMap.size()==0)
			{
				if(constraint_val==1)
				{
					KenKen_3.Constraints_Array.set(id, null);
					Variable.removeConstraintFromVariables(id,KenKen_3.Variable_Array);
					return true;
					// Constraint satisfied remove it 
				}
				else
				{
					//Solution is inconsistent. This constraint has no variable and is not satisfied
					return false;
				}
			}
			break;
		case 5:// Divide
			this.VaraibleMap.remove(variableNumber);
			for(Map.Entry<Integer,VariableMessages> en : VaraibleMap.entrySet())
			{// A for loop here appears costly since there will always be only one more variable 
				// but you will anyways have to do it since you don know what the next guy is 
				VariableMessages tempVarMessage = en.getValue();
				Variable tempVar = KenKen_3.Variable_Array.get(en.getKey());
				int domainValueToBeSet;
				if(tempVar.DomainMap.containsKey((domainValue * constraint_val)) && tempVar.DomainMap.containsKey((constraint_val/domainValue)) )
				{//Set the nextVar as the highest probabilty of the two poss values
					
					if(tempVarMessage.pi[domainValue *constraint_val].compareTo(tempVarMessage.pi[constraint_val/domainValue]) > 0)
					{
						domainValueToBeSet= domainValue *constraint_val;
					}
					else
					{
						domainValueToBeSet= constraint_val/domainValue;
					}
				}
				else if(tempVar.DomainMap.containsKey((domainValue *constraint_val)))
				{
					domainValueToBeSet= domainValue *constraint_val;
				}
				else if(tempVar.DomainMap.containsKey((constraint_val/domainValue)))
				{
					domainValueToBeSet= constraint_val/domainValue;
				}
				else{
					// We cannot find a value. Inconsistent solution 
					return false;
					
				}
				//TODO
				KenKen_3.variableValues.set(en.getKey(), domainValueToBeSet);
				//TODO
				KenKen_3.Constraints_Array.set(id, null);
				Variable.removeConstraintFromVariables(id,KenKen_3.Variable_Array);
				//Call main remove for this variable and this domainValueToBeSet 
				remove.removeVar(en.getKey(),domainValueToBeSet,KenKen_3.Constraints_Array,KenKen_3.Variable_Array);
				
				return true;
				
				
			
			}
			
			break;
				
		}
		return true;
	}
	

	public void printConstraints(){
	for(Map.Entry<Integer,VariableMessages> en : VaraibleMap.entrySet())
	{
		//System.out.println("Variable " + en.getKey() + " Eta " + en.getValue());
		System.out.println("Variable " + en.getKey());
	}
		
	}
	
	//Method to check if the constraint is satisfied
	public boolean satisfy()
	{
		boolean satisfy = false;
		switch(this.operator)
		{
		case 0: //all diff
			    satisfy = alldiff();
			    break;
		case 1: //addition
			int sum = 0;
			for(Map.Entry<Integer,VariableMessages> en : this.VaraibleMap.entrySet())
			{
				sum += KenKen_3.temp_assign[en.getKey()];	
			}

			if(sum == this.constraint_val)
				satisfy = true;
			else
				satisfy= false;
			break;

		case 2://subtraction
			int var[] = new int[2];
			int i=0;
			for(Map.Entry<Integer,VariableMessages> en : this.VaraibleMap.entrySet())
			{
				var[i++] = KenKen_3.temp_assign[en.getKey()];	
			}
			if((Math.abs(var[0] - var[1]) == this.constraint_val) || (Math.abs(var[1] - var[0]) == this.constraint_val))
				satisfy= true;
			else
				satisfy = false;
			break;
		case 3: //assertion
			for(Map.Entry<Integer,VariableMessages> en : this.VaraibleMap.entrySet())
			{
				if(KenKen_3.temp_assign[en.getKey()] == this.constraint_val)
					satisfy = true;
				else
					satisfy = false;
			}
			break;

		case 4: //multiply
			int product = 1;
			for(Map.Entry<Integer,VariableMessages> en : this.VaraibleMap.entrySet())
			{
				product *= KenKen_3.temp_assign[en.getKey()];	
			}

			if(product == this.constraint_val)
				satisfy= true;
			else
				satisfy = false;

			break;
		case 5://division
			int var_div[] = new int[2];
			int j=0;
			for(Map.Entry<Integer,VariableMessages> en : this.VaraibleMap.entrySet())
			{
				var_div[j++] = KenKen_3.temp_assign[en.getKey()];	
			}
			if((Math.abs(var_div[0]/var_div[1]) == this.constraint_val) || (Math.abs(var_div[1]/var_div[0]) == this.constraint_val))
				satisfy = true;
			else
				satisfy = false;

		}
		return satisfy;
	}

	//Method to check for all different constraint
	public boolean alldiff()
	{
		int j,i,k,m;
		
		//handle row -all diff
		j =i =k= 1;
		Map<Integer,Integer> temph = new HashMap<Integer,Integer>();
		while( j <= KenKen_3.size*KenKen_3.size)
		{
			for(i = j; i<KenKen_3.size+j;i++)
			{
				Integer freq = temph.get(KenKen_3.temp_assign[i]);
				if(freq == null)
					temph.put(KenKen_3.temp_assign[i],1);
				else
					temph.put(KenKen_3.temp_assign[i],freq + 1);
			}
			for(Integer en : temph.keySet())
			{
				if(temph.get(en) > 1)
					return false;
			}
			j += KenKen_3.size;
			temph.clear();
		}
		
		//handle column-all diff
		m =1;
		j =i =k= 1;
		temph.clear();
		while( m < KenKen_3.size*KenKen_3.size)
		{
			i = j;
			while(k<KenKen_3.size)
			{
				Integer freq = temph.get(KenKen_3.temp_assign[i]);
				if(freq == null)
					temph.put(KenKen_3.temp_assign[i],1);
				else
					temph.put(KenKen_3.temp_assign[i],freq + 1);
				i+=KenKen_3.size;
				k++;
			}
			for(Integer en : temph.keySet())
			{
				if(temph.get(en) > 1)
					return false;
			}
			j++;
			k=0;
			m += KenKen_3.size;
			temph.clear();
		}
		return true;
	}
}
