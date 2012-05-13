package project;
import java.math.BigDecimal;


public class VariableMessages {
	
	
	
		// Pi values for each of the domain of the variables
		public BigDecimal pi [];
		
		VariableMessages()
		{
			pi = new BigDecimal[KenKen_3.size+1];
			for(int i=0; i<=KenKen_3.size;i++)
				pi[i]= new BigDecimal(1.0);
			
		}	
		
	

}
