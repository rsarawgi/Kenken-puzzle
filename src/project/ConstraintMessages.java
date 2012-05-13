package project;
import java.math.BigDecimal;

public class ConstraintMessages {
	// eta for each of the domain values
	public BigDecimal eta_new [];
	public BigDecimal eta_old [];
	
	ConstraintMessages()
	{
		BigDecimal One= new BigDecimal(1.0);
		eta_new = new BigDecimal[KenKen_3.size+1];
		eta_old = new BigDecimal[KenKen_3.size+1];
		for(int i=1; i<=KenKen_3.size;i++){
			eta_new[i]= One.divide(new BigDecimal(KenKen_3.size+1),10,BigDecimal.ROUND_CEILING);
		    eta_old[i]= new BigDecimal(1.0);
		
		}
	}	
	
}
