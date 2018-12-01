package usp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class Ligacao{	
	//Valores a serem cobrados na tarifa
	//Fix-me: fixei aqui por simplicidade, poderia ser valores padroes da APPs
    private final double tarifPadr = 0.40; //tarifa padrao
    private final double descNaTarifPadr = 0.50; //desconto da tarifa com desconto
    private final double descAdicional = 0.15; //desconto adicional
    
    //Flag pra dizer que fracoes de minutos sao cobrados ou nao
    //por padrao sao cobrados
    private final boolean tarifaFracaoDeMinutos = App.tarifaFracaoDeMinutosAtivada;    
    //
    private LocalDateTime mi; //momento inicial
    private LocalDateTime mt; //momento final
    private Duration du;
    
    //uma ligacao pode estar nos seguintes estados
    //terminada ou online
    //isso e relevante para calculo da duracao da chamada
    private String status; //"EM_ANDAMENTO" ou "ENCERRADA"
	private double vlrDaLigacao;
	private String tpoDeTarifacaoCalculada;
	
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
    	    	
    	LocalDateTime diaSeguinteAs8DaManha = mi.minusHours(mi.getHour()).plusHours(8);
    	
    	if (this.heMesmoDia(this.mi, this.mt) && mi.getHour() > 0 && mt.getHour() < 8 && du.getSeconds() < 60*60) {
    		//de 0 ate 8 (exclusive) com menos de 60 minutos (Chamada no horario TarifComDesc)
    		this.tpoDeTarifacaoCalculada = "TarfComDesc";
    		return this.du.getSeconds() * ((this.tarifPadr - (this.tarifPadr*this.descNaTarifPadr)) / 60);
    		
    	}else if(mi.getHour() >= 0 && mt.getHour() < 8 && du.getSeconds() > 60*60 && du.getSeconds() < 8*60*60) {
    		//de 0 ate 8 (exclusive) com mais de 60 min (Chamada ainda no horario TarifComDesc *Com desconto adicional)
    		this.tpoDeTarifacaoCalculada = "TarfComDes-DescAdicional";
    		double vlr = this.du.getSeconds() * ((this.tarifPadr - (this.tarifPadr*this.descNaTarifPadr)) / 60);
    		return vlr - (vlr*this.descAdicional);
    		
    	}else if(mi.getHour() > 0 && du.getSeconds() >= 8*60*60 ) {
    		//de 0 e passou das 8 da manha, cobranca dif
    		return cobra_TarifDif_comDescontoAdicional();
    		
    	}else if(this.heMesmoDia(this.mi, this.mt) && mi.getHour() >=8 && mt.getHour() < 18 && du.getSeconds() <60*60) {
    		//Ligacao entre 8 e 18(exclusive) com menos de 60 min
    		this.tpoDeTarifacaoCalculada = "TarifPadr";
    		return this.du.getSeconds() * (this.tarifPadr/60);
    		
    	}else if(this.heMesmoDia(this.mi, this.mt) && mi.getHour() >=8 && mt.getHour() <18 && du.getSeconds() > 60*60 && du.getSeconds() < 9*60*60 ) {
    		//Ligacao entre 8 e 18 (exclusive) com mais de 60 min
    		this.tpoDeTarifacaoCalculada = "TarifPadr-DescAdicional";
    		double vlr = this.du.getSeconds() * (this.tarifPadr/60);
    		return vlr - (vlr*this.descAdicional);
    		
    	}else if(mi.getHour() >=8 && du.getSeconds() >= 9*60*60) {
    		//Ligacao entre 8 e passa das 17 o que ha he diferenciada e com desconto
    		return cobra_TarfDif_ComDescontoAdicional();
    		
    	}else if(this.heMesmoDia(this.mi, this.mt) && mi.getHour() >=18 && mt.isBefore(diaSeguinteAs8DaManha) && du.getSeconds() <= 60*60) {
    		//Ligacao que inicia entre 17 e até 8 (exclusive) do dia seguinte com menos de 60 minutos
    		return cobra_TarifComDesc_ComDescontoAdicional();
    		
    	}else if(mi.getHour() >= 18 && mt.isBefore(diaSeguinteAs8DaManha) &&  du.getSeconds() >= 60*60) {
    		//Ligacao que comeca as 17 em diante passando de 60 min pode passar ate as 8(exclusive) do dia seguinte ao inicio da ligacao
    		return cobra_TarifComDesc_ComDescontoAdicional();
    	}
		return 0;		
    }

	
	private double cobra_TarifDif_comDescontoAdicional() {
		// TODO Auto-generated method stub
		return 0;
	}

	private double cobra_TarfDif_ComDescontoAdicional() {
		// TODO Auto-generated method stub
		return 0;
	}

	private double cobra_TarifComDesc_ComDescontoAdicional() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	private boolean heMesmoDia(LocalDateTime mi2, LocalDateTime mt2) {
		int diaI = this.mi.getDayOfMonth();
		int mesI = this.mi.getMonthValue();
		int anoI = this.mi.getYear();
		
		int diaT = this.mt.getDayOfMonth();
		int mesT = this.mt.getMonthValue();		
		int anoT = this.mt.getYear();		
		
		if (anoI == anoT && mesI == mesT && diaI == diaT) return true;
		return false;
	}

	public String getTipoDeTarifacao() {
		return this.tpoDeTarifacaoCalculada;
	}    	
}
