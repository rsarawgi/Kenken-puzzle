package project;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Variable {
	
    public HashMap<Integer,Integer>DomainMap;
	public HashMap<Integer, ConstraintMessages> ConstraintMap;
	public int id;
	public BigDecimal bias [];
	public BigDecimal max_bias;
	
	Variable(int id)
	
	{
		this.id = id;
		ConstraintMap = new HashMap<Integer,ConstraintMessages>();
		DomainMap = new HashMap<Integer,Integer>();
		for (int i =1; i<= KenKen_3.size; i++){
			DomainMap.put(i,1);
		}
		bias = new BigDecimal[KenKen_3.size+1];
		max_bias =BigDecimal.ZERO;
		
	}
	public static void removeConstraintFromVariables(int id2,
			ArrayList<Variable> variableArray) {
		// TODO Auto-generated method stub
for( int i =1; i<=KenKen_3.size*KenKen_3.size;i++){
	if(variableArray.get(i)!=null)
		variableArray.get(i).ConstraintMap.remove(id2);
	
}
		
	}
	
	public void printVariable(){
		for(Map.Entry<Integer,ConstraintMessages> en : ConstraintMap.entrySet())
		{
			System.out.println("Constraint " + en.getKey() );		}
			
		}
}
