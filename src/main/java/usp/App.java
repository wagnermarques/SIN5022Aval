package usp;


public class App{

    public static boolean tarifaFracaoDeMinutosAtivada = true;
    
	//A tarifa padr�o para chamadas de longa distancia  de R$0,40 por minuto
    private double tarifaPadrao = 0.4D;
    
    public static void main( String[] args )
    {
    	
    	String[] strArr = new String[5];
    	
    	
    	//consulta paramertros de inicializacao do sistema que foram recebidos
    	for(int i = 0; i < args.length; i++) {
    		//averigua se algum deles tarifaFracaoDeMinutos    		
    		if (args[i].contains("tarifaFracaoDeMinutos")) {
        	 String[] tarifaFracaoDeMinutosParameter = args[i].split("=");
        	 if(tarifaFracaoDeMinutosParameter[1].equals("false")) {
        		 App.tarifaFracaoDeMinutosAtivada = false;
        	 }
         }	
        }	
    }	
}
