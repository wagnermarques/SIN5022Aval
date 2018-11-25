package usp;
import java.time.*;

public class Ligacao{

    private final double tp = 0.40; //tarifa padrao
    private final double descTarfComDesc = 0.50f; //desconto da tarifa com desconto
    private final double descAdicional = 0.15f; //desconto adicional
    
    private double tarifaPadrao = 0.4;
    private LocalDateTime mi; //momento inicial
    private LocalDateTime mt; //momento final
    private long du;
    private String tag;
    
    //uma ligacao pode estar nos seguintes estados
    //terminada ou online
    //isso e relevante para calculo da duracao da chamada
    private String status; //"EM_ANDAMENTO" ou "ENCERRADA"

    public void Ligacao(LocalDateTime mi){
        this.mi = mi;
        this.status = "EM_ANDAMENTO";
    }

    public void encerrarLigacao(){
        this.status = "ENCERRADA";
    }
    
    public static Double calculaValorDaChamada( )
    {
        return 0D;
    }
    
}
