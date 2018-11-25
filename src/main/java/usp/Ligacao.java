package usp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.TemporalField;

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
    
	private double calculaValorDaChamadaComFracaoDeMinutosNaRazaoDeSegundos() {
		this.du = Duration.between(this.mi,this.mt);		
		this.vlrDaLigacao = this.tarifPadrPorSegundo() * du.getSeconds();
		System.out.println(vlrDaLigacao);
		System.out.println(vlrDaLigacao);
		System.out.println(vlrDaLigacao);
		//usando o BigDecimal so pra arrendondar
		BigDecimal vlrArrendondado  = new BigDecimal(this.vlrDaLigacao).setScale(3, RoundingMode.HALF_EVEN);
		System.out.println(vlrArrendondado.doubleValue());
		System.out.println(vlrArrendondado.doubleValue());
		System.out.println(vlrArrendondado.doubleValue());
		return vlrArrendondado.doubleValue();
	}

	public String getTipoDeTarifacao() throws Exception {
		String tpoTar = "";
		if(!this.status.equals("ENCERRADA")) throw new Exception("Pra ver qual o tipo de tarifacao [Normal, Com Desconto ou Com Desc Adicional] a ligacao precisa estart encerrada");
		if (this.du.getSeconds() >= 59) {			
			tpoTar+=";TarifComDescAdic";
		}
		if(this.mi.getHour() >= 8 && this.mt.getHour() < 17) {
			tpoTar+=";TarifPadr";
		}
		if(this.mt.getHour() >= 17 && this.mi.getHour() < 8) {
			tpoTar+=";TarifDif";
		}
		return tpoTar;
	}
    
}
