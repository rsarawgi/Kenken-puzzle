package project;

import java.util.ArrayList;
import java.util.Map;

public class remove {
	public static boolean removeVar(int variableNum, int domainValue, ArrayList<Constraint> Constraint_Array, ArrayList<Variable> Variable_Array)
	{
		for(Constraint constraint : Constraint_Array)
		{
			if((constraint!=null) && (constraint.VaraibleMap.containsKey(variableNum)))
			{
				if(!constraint.remove(variableNum, domainValue))
					return false;
		    }
			
		}
		Variable_Array.set(variableNum, null);
		return true;
	}
	
	public static boolean setAssertion( ArrayList<Constraint> Constraint_Array, ArrayList<Variable> Variable_Array)
	{
		for(Constraint constraint : Constraint_Array)
		{
			//3 is for assertion
			if(constraint != null && constraint.operator==3 )
			for(Map.Entry<Integer,VariableMessages> en : constraint.VaraibleMap.entrySet())
			{
				KenKen_3.variableValues.set(en.getKey(),constraint.constraint_val);
				Constraint_Array.set(constraint.id, null);
				Variable.removeConstraintFromVariables(constraint.id,KenKen_3.Variable_Array);
				removeVar(en.getKey(),constraint.constraint_val,Constraint_Array,Variable_Array);
				KenKen_3.temp_assign[en.getKey()]=constraint.constraint_val;
				
				
			}
				
		}
		return false;
	}

	
}	


