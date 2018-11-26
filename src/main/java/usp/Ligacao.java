package usp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class Ligacao{

	//Valores a serem cobrados na tarifa
	//Fix-me: fixei aqui por simplicidade, poderia ser valores padroes da APPs
    private final double tarifPadr = 0.40; //tarifa padrao
    private final double descNaTarifPadr = 0.50f; //desconto da tarifa com desconto
    private final double descAdicional = 0.15f; //desconto adicional
    
    //Flag pra dizer que fracoes de minutos sao cobrados ou nao
    //por padrao sao cobrados
    private final boolean tarifaFracaoDeMinutos = App.tarifaFracaoDeMinutos;
    
    //
    private LocalDateTime mi; //momento inicial
    private LocalDateTime mt; //momento final
    private Duration du;
    
    //uma ligacao pode estar nos seguintes estados
    //terminada ou online
    //isso e relevante para calculo da duracao da chamada
    private String status; //"EM_ANDAMENTO" ou "ENCERRADA"
	private double vlrDaLigacao;

    //construtor
    public Ligacao(LocalDateTime mi) {
    	this.mi = mi;
        this.status = "EM_ANDAMENTO";
	}

    public void encerrarLigacao(LocalDateTime mt){
    	this.mt = mt;
    	this.du = Duration.between(mi, mt);//mt exclusive
        this.status = "ENCERRADA";
    }
     
    public double calculaValorDaChamada( ) throws Exception
    {    	
    	if(!this.status.equals("ENCERRADA")) throw new Exception("Nao he possivel calcular antes de a ligacao ser encerrada");
    	if(tarifaFracaoDeMinutos) {
    		return calculaValorDaChamadaComFracaoDeMinutosNaRazaoDeSegundos();
    	}else {
    		throw new Exception("Ainda nao implementado...");
    	}        
    }

    //trabalhando com double porque supre a necessidade de 
    //precisao de centesimos
    private double tarifPadrPorSegundo() {
    	System.out.println(this.tarifPadr/60);
    	return this.tarifPadr/60;
    }
        
    private boolean isHorarioDeTarifPadr() {
		return this.mi.getHour() >= 8 && this.mt.getHour() < 18 ? true : false;
	}
    
    private boolean isHorarioDeTarifacaoDiferenciada() {
    	return this.mi.getHour() < 8 || this.mi.getHour() >= 18;
    }
    
	private double calculaValorDaChamadaComFracaoDeMinutosNaRazaoDeSegundos() {				
		double vlrLocalDaLigacao = 0.0;		
		
		if(isHorarioDeTarifPadr()) vlrLocalDaLigacao = calculaVlrNoHorarioDeTarifaPadrao();		
		if(temDescontoAdicional()) vlrLocalDaLigacao = vlrLocalDaLigacao - vlrLocalDaLigacao*this.descAdicional;
		
		//usando o BigDecimal so pra arrendondar
		this.vlrDaLigacao = vlrLocalDaLigacao;
		BigDecimal vlrArrendondado  = new BigDecimal(this.vlrDaLigacao).setScale(3, RoundingMode.HALF_EVEN);
		return vlrArrendondado.doubleValue();
	}

	private double calculaVlrNoHorarioDeTarifaPadrao() {
		return this.tarifPadrPorSegundo() * this.du.getSeconds();		
	}

	//Helper Methods pra detectar tipo de ligacao
	private boolean temDescontoAdicional() {
		//System.out.println("private boolean temDescontoAdicional() {...");		
		return this.du.getSeconds() >= 60*60 ? true:false; //59 minutos expressos em segundos 
	}	
	
	
	
	public String getTipoDeTarifacao() throws Exception {
		System.out.println("public String getTipoDeTarifacao() throws Exception {...");
		if(!this.status.equals("ENCERRADA")) throw new Exception("Pra ver qual o tipo de tarifacao [Normal, Com Desconto ou Com Desc Adicional] a ligacao precisa estart encerrada");
		
		boolean temDescAdicional = false;
		boolean tarifPadr = false;
		boolean tarofDif = false;
		String strTpoLig = "#";
		
		System.out.println("this.mi.getHour() >= 8 "+ this.mi.getHour());			
		if(isHorarioDeTarifPadr()) {
			strTpoLig = "TarifPadr";
		}
		
		if(this.mt.getHour() >= 18 && this.mi.getHour() < 8) {
			strTpoLig = "TarifComDesc";
		}
		
		if(this.temDescontoAdicional()) strTpoLig += "_ComDescAdicional";
		
		System.out.println("strTpoLig = "+ strTpoLig);
		return strTpoLig;
	}


	
	//G e t t e r s   a n d   S e t t e r s
	public Duration getDu() {
		return du;
	}
    	
}
