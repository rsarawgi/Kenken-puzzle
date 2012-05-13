package project;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CalculateMessages {

	public static void CalculateMessagesVariables(ArrayList<Constraint> Constraints_Array, ArrayList<Variable> Variable_Array){
		BigDecimal temp_arr []= new BigDecimal[KenKen_3.size+1];
		
		for(int i = 1; i<=KenKen_3.column_size-2; i++){
			Variable tempVariable=(Variable)Variable_Array.get(i);
			if(tempVariable != null){
				 for(int j=1;j<=KenKen_3.num_constraint;j++){
					 ConstraintMessages tempConstraintMessage = tempVariable.ConstraintMap.get(j);
					 if(tempConstraintMessage != null){
						 for(int ite=1; ite<=KenKen_3.size;ite++){
							 temp_arr[ite]= new BigDecimal(1.0);
						 }
						 Constraint tempConstraint = (Constraint)Constraints_Array.get(j);
						 VariableMessages tempVariableMessage = tempConstraint.VaraibleMap.get(i);
						 for(int k=1;k<=KenKen_3.num_constraint;k++){
							 ConstraintMessages tempOtherConstraintMessage = tempVariable.ConstraintMap.get(k);
							 if((tempOtherConstraintMessage!= null)&& (j !=k) ){
								 for( int n=1; n<= KenKen_3.size; n++){
									 temp_arr[n]= temp_arr[n].multiply(tempOtherConstraintMessage.eta_new[n]);
								 }								 	
							 }
						 }
							 for(int r=1; r<=KenKen_3.size; r++){
								 tempVariableMessage.pi[r]=temp_arr[r];
							 }
							 tempConstraint.VaraibleMap.put(i, tempVariableMessage);
							 Constraints_Array.set(j, tempConstraint);
					 }
				 }			 
			}
		}
	}	
}
