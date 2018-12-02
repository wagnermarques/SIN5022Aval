package usp;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

//https://github.com/junit-team/junit5/blob/master/documentation/src/test/java/example/ParameterizedTestDemo.java

@DisplayName("Suite de Testes: Criterio de Particao de Equivalencia e Vlrs Limites")
public class AppTest 
{

    @BeforeAll
    static void initAll() {
        //System.out.println("### @BeforeAll static void initAll() {...");
        //Estabelece os valores para tarifacao
        //private final tp = 0.40f; //tarifa padrao
        //private final descTarfComDesc = 0.50f; //desconto da tarifa com desconto
        ///private final descAdicional = 0.15f; //desconto adicional    
    }
    
    @BeforeEach
    void init() {
        //System.out.println("### @BeforeEach void init() {...");
    }
    
    @ParameterizedTest
    @MethodSource("metodoProvedorDeCasosDeTestesParaClassesDeEquivalenciasValidas")
    @DisplayName("Testa com particoes V-A-L-I-D-A-S de Equivalencia e Vlrs Limite")
    void testaComParticoes_VALIDAS_DeEquivalenciaEVlrsLimites(String label, LocalDateTime mi, LocalDateTime mt, double vlrFromOracle,String tpoDeTarifacaoFromOracle) {
    	System.out.println(">>>>>"+label);
    	Ligacao ligacao = new Ligacao(mi);
    	ligacao.encerrarLigacao(mt);     
        try {        	
			assertEquals(vlrFromOracle,ligacao.calculaValorDaChamada());
			assertEquals(tpoDeTarifacaoFromOracle,ligacao.getTipoDeTarifacao());
			System.out.println(">>>>> Tipo de Tarifacao esperado = "+tpoDeTarifacaoFromOracle+" / Obtido:"+ligacao.getTipoDeTarifacao());
			System.out.println(">>>>>"+mi.toString()+" - "+mt.toString()+"du="+(Duration.between(mi, mt).getSeconds()/60)+" VlrEsperado="+vlrFromOracle+"/VlrObtido="+ligacao.calculaValorDaChamada());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    @ParameterizedTest
    @MethodSource("metodoProvedorDeCasosDeTestesParaClassesDeEquivalencias_IN_Validas")
    @DisplayName("Testa com particoes I-N-V-A-L-I-D-A-S de Equivalencia e Vlrs Limite")
    void testaComParticoes_INVALIDAS_DeEquivalenciaEVlrsLimites(String label, LocalDateTime mi, LocalDateTime mt, double vlrFromOracle,String tpoDeTarifacaoFromOracle) {
    	System.out.println(">>>>>"+label);    	
    	
    	Ligacao ligacao = new Ligacao(mi);
    	ligacao.encerrarLigacao(mt);     
        try {        	
			assertNotEquals(vlrFromOracle,ligacao.calculaValorDaChamada());
			assertNotEquals(tpoDeTarifacaoFromOracle,ligacao.getTipoDeTarifacao());
			System.out.println(">>>>> Tipo de Tarifacao esperado = "+tpoDeTarifacaoFromOracle+" / Obtido:"+ligacao.getTipoDeTarifacao());
			System.out.println(">>>>>"+mi.toString()+" - "+mt.toString()+"du="+(Duration.between(mi, mt).getSeconds()/60)+" VlrEsperado="+vlrFromOracle+"/VlrObtido="+ligacao.calculaValorDaChamada());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    static Collection<Object[]> metodoProvedorDeCasosDeTestesParaClassesDeEquivalenciasValidas() {
    	
//    	** Classes de Equivalência para horário de Tarifa Padrão (TarifPadr)  0.40/min (Sem Desc Adicional)
//    	   Mi >= 8h e M<18h Cobrança com preço cheio 0,4 p/min
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du < 60            | Du >= 60             |
    	
    	//CLASSES DE EQUIVALENCIAS VALIDAS COM SEUS VALORES LIMITES
    	//inicia 8 horas em ponto e dura 20 minutos
    	String label0="###[1] [TarifPadr SemDescAdic] (pegando um vlr tipico de uma classe valida) ";
        LocalDateTime mi0= LocalDateTime.of(2015,Month.NOVEMBER,04,15,0);
        LocalDateTime mt0 = mi0.plusMinutes(20);        
        double vlr0 = 8.0;
        String tipoDeTarificacao0 = "TarifPadr";
        
        String label1="###[3] [TarifPadr SemDescAdic] (Explora vlr limite 8h: Ligacao de 59min que comeca 8h em ponto) ";    	
        LocalDateTime mi1 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0);
        LocalDateTime mt1 = mi1.plusMinutes(59);        
        double vlr1 = 23.6;
        String tipoDeTarificacao1 = "TarifPadr";              
    
        String label2="###[5] [TarifPadr SemDescAdic] (Explora vlr limite Posterior 8h: ligacao de 59 min que comeca as 8h:00:01) ";
        LocalDateTime mi2 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).plusSeconds(1);
        LocalDateTime mt2 = mi2.plusMinutes(59);
        double vlr2 = 23.6;
        String tipoDeTarificacao2 = "TarifPadr";
                                
        String label3="###[6] [TarifPadr SemDescAdic] (Explora vlr limite anterior 18h: ligacao de 59 min que termina as 18 menos 1 segundo) ";
        LocalDateTime mi3 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,0).minusSeconds((59*60)-1);
        LocalDateTime mt3 = mi3.plusMinutes(59);
        double vlr3 = 23.6;
        String tipoDeTarificacao3 = "TarifPadr";

//    	** Classes de Equivalencia para horário de Tarifa Padrão (TafifPadr) + Desconto Adicional
//    	   M >= 8h e M<18h Cobrança com preço cheio 0,4 p/min menos 15% de desconto.
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du > 60            | Du < 60              |
//    	No caso de Du >= 60 a cobrança já não é mais de 0.40/min e sim 0.40 - 15%/min.
        
        String label4="###[9] [TarifPadr+DescAdic] (Explora vlr limite posterior 60min: ligacao de 120min) ";
        LocalDateTime mi4 = LocalDateTime.of(2015,Month.NOVEMBER,04,15,0);
        LocalDateTime mt4 = mi4.plusMinutes(120);
        double vlr4 = 40.80;
        String tipoDeTarificacao4 = "TarifPadr-DescAdicional";
                      
        
//    	** Classes de Equivalência para horário de tarifa com desconto (TarifComDesc) de 50% 
//    	*** Para o caso de inicio na tarifa com desconto e termino na tarifa padrao  (Vice)
//    	    Mi >= 18h e Mt > 8h 
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi >= DD/MM/AA:18h | Mi >= DD/MM/AA:8h    |
//    	| Mt                  | Mt >= DD/MM/AA:8h  | Mt qualquer          |
//    	| Du = (Mt-Mi)        | Du < 60            | Du > 60              |

    	//CLASSES DE EQUIVALENCIAS VALIDAS PARA HORARIOS COM TARIFAS COM DESCONTO
    	//inicio entre 18 horas (inclusive) e 8:00h (exclusive) do dia posterior 
    	String label5="###[1 10] [TarifComDesc SemDescAdic] (pegando um vlr tipico de uma classe valida) ";
        LocalDateTime mi5= LocalDateTime.of(2015,Month.NOVEMBER,04,0,0);
        LocalDateTime mt5 = mi5.plusMinutes(20);        
        double vlr5 = 4.0;
        String tipoDeTarificacao5 = "TarifComDesc";
        
        String label6="###[3 12] [TarifComDesc SemDescAdic] (Explora vlr limite 18h: Ligacao de 59min que comeca 18h em ponto) ";    	
        LocalDateTime mi6 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,0);
        LocalDateTime mt6 = mi6.plusMinutes(59);        
        double vlr6 = 11;
        String tipoDeTarificacao6 = "TarifComDesc";              
    
        String label7="###[5 14] [TarifComDesc SemDescAdic] (Explora vlr limite Posterior 18h: ligacao de 59 min que comeca as 18h:00:01) ";
        LocalDateTime mi7 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,0).plusSeconds(1);
        LocalDateTime mt7 = mi7.plusMinutes(59);
        double vlr7 = 11;
        String tipoDeTarificacao7 = "TarifComDesc";
                                
        String label8="###[6 15] [TarifComDesc SemDescAdic] (Explora vlr limite anterior 8h do dia Seguinte: ligacao de 59 min que termina as 8 menos 1 segundo) ";
        LocalDateTime mi8 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(60);
        LocalDateTime mt8 = mi8.plusMinutes(59);
        double vlr8 = 11;        
        String tipoDeTarificacao8 = "TarifComDesc";
        
//*** Para o caso de inicio na tarifa com desconto (de manha) e termino na tarifa padrao (Sem Desc Adicional) (Vice)        
//        | Variável de Entrada | Classes Eq Válidas                          | Classes Eq Inválidas                        |
//        | Mi                  | DD/MM/AA:00:00:00 <= Mi < DD/MM/AA:08:00:00 | DD/MM/AA:08:00:00 <= Mi < DD/MM/AA:18:00:00 |
//        | Mt                  | Mt > = DD/MM/AA:08:00:00                    | Mt qualquer                                 |
//        | Du = (Mt-Mi)        | Du < 60                                     | Du qualquer                                 |

        String label9="###[19] [TarifDif SemDescAdic] (Ligacao de uma classe tipica valido: ligacao de 20 min das 7:50 as 8:10) ";
        LocalDateTime mi9 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(60);
        LocalDateTime mt9 = mi9.plusMinutes(59);
        double vlr9 = 6;
        
        String label10="###[21] [TarifDif SemDescAdic] (Explora limite de termino as 8h: ) ";
        LocalDateTime mi10 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(59);
        LocalDateTime mt10 = mi10.plusMinutes(59);
        double vlr10 = 11.8;
        
        String label11="###[23] [TarifDif SemDescAdic] (Explorar limite de termino apos as 8h: Ligacao das 7:00:02 as 8:00:01) ";
        LocalDateTime mi11 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(58);
        LocalDateTime mt11 = mi11.plusMinutes(59);
        double vlr11 = 11.8;
        
        
        
//        *** Para o caso de inicio na tarifa com desconto (de manha) e termino na tarifa padrao  (Com Desc Adicional) (Vice)
//        | Variável de Entrada | Classes Eq Válidas                          | Classes Eq Inválidas                        |
//        | Mi                  | DD/MM/AA:00:00:00 <= Mi < DD/MM/AA:08:00:00 | DD/MM/AA:08:00:00 <= Mi < DD/MM/AA:18:00:00 |
//        | Mt                  | Mt > = DD/MM/AA:08:00:00                    | Mt qualquer                                 |
//        | Du = (Mt-Mi)        | Du > 60                                     | Du qualquer                                 |
        

        String label12="###[24] [TarifDif ComDescAdic] (Ligação de 120min de uma classe típica válida: Ligação de 120min a partir das 00h) ";
        LocalDateTime mi12 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0);
        LocalDateTime mt12 = mi12.plusMinutes(120);
        double vlr12 = 24.0;
        
        
        String label13="###[26] [TarifDif ComDescAdic] (Ligação de 120min Explora limite de termino as 8h: Ligação de  	120min que termina 8 em ponto) ";
        LocalDateTime mi13 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusMinutes(120);
        LocalDateTime mt13 = mi13.plusMinutes(120);
        double vlr13 = 24.0;
        

        String label14="###[28] [TarifDif ComDescAdic] ( Ligação de 120min Explorar limite de termino apos as 8h: Ligação de 120 min que termina 8:00:01) ";
        LocalDateTime mi14 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds((120*60)-1);
        LocalDateTime mt14 = mi14.plusMinutes(120);
        double vlr14 = 24.0;
                     
        
        
//        *** Para o caso de inicio na tarifa com desconto (parte da tarde) e termino na tarifa padrao (Com Desconto Adicional)  (Vice)
//        | Variável de Entrada | Classes Eq Válidas                            | Classes Eq Inválidas                        |
//        | Mi                  | DD/MM/AA:18:00:00 <= Mi < DD+1/MM/AA:08:00:00 | DD/MM/AA:08:00:00 <= Mi < DD/MM/AA:18:00:00 |
//        | Mt                  | Mt >= DD+1/MM/AA:08:00:00                     | Mt qualquer                                 |
//        | Du = (Mt-Mi)        | Du > 60                                       | Du < 60                                     |

        
        String label15="###[29] [TarifDif ComDescAdic] (Ligacao de uma classe valida: Ligação de 8:10min de duração que comeca as 23:50 e termina as 8:00 do dia seguinte ) ";
        LocalDateTime mi15 = LocalDateTime.of(2015,Month.NOVEMBER,04,23,50);
        LocalDateTime mt15 = LocalDateTime.of(2015,Month.NOVEMBER,05,8,00);
        double vlr15 = 98.0;
        

        
        
//        *** Para o caso de inicio na tarifa padrao e término na tarifa com desconto (Sem Desconto Adicional) (Versa)
//        | Variável de Entrada | Classes Eq Válidas                          | Classes Eq Inválidas                          |
//        | Mi                  | DD/MM/AA:08:00:00 <= Mi < DD/MM/AA:18:00:00 | DD/MM/AA:18:00:00 <= Mi < DD+1/MM/AA:08:00:00 |
//        | Mt                  | Mt > DD/MM/AA:18h                           | Mt >= DD/MM/AA:18                             |
//        | Du = (Mt-Mi)        | Du < 60                                     | Du > 60                                       |
        
        String label16="###[30] [TarifDif SemDescAdic] (Ligacao classe valida: Ligacao de 59min que comeca as 17:50:00) ";
        LocalDateTime mi16 = LocalDateTime.of(2015,Month.NOVEMBER,04,17,50);
        LocalDateTime mt16 = mi16.plusMinutes(59);
        double vlr16 = 13.8; 
        
        String label17="###[32] [TarifDif SemDescAdic] (Explorar 18h: Ligacao de 59min que comeca as 17:00:59 e termina as 18 em ponto) ";
        LocalDateTime mi17 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,00).minusMinutes(59);
        LocalDateTime mt17 = mi17.plusMinutes(59);
        double vlr17 = 23.6; 
                
        String label18="###[33] [TarifDif SemDescAdic] (Explorar limite posterior 18h: Ligacao de 59min que termina 18:00:01) ";
        LocalDateTime mi18 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,00).minusSeconds((59*60)-1);
        LocalDateTime mt18 = mi17.plusMinutes(59);
        double vlr18 = 23.4; //o minuto das 18h custa ,2 

        
//        *** Para o caso de inicio na tarifa padrao e término na tarifa com desconto (Com Desconto Adicional) (Versa)
//        | Variável de Entrada | Classes Eq Válidas                          | Classes Eq Inválidas                          |
//        | Mi                  | DD/MM/AA:08:00:00 <= Mi < DD/MM/AA:18:00:00 | DD/MM/AA:18:00:00 <= Mi < DD+1/MM/AA:08:00:00 |
//        | Mt                  | Mt > DD/MM/AA:18h                           | Mt >= DD/MM/AA:18                             |
//        | Du = (Mt-Mi)        | Du < 60                                     | Du > 60                                       |
        

        
        

        
        return Arrays.asList(new Object[][]{
        	//M >= 8h e M<18h CobranÃ§a com preÃ§o cheio 0,4 p/min
        	{label0, mi0, mt0, vlr0, tipoDeTarificacao0}, 
        	{label1, mi1, mt1, vlr1, tipoDeTarificacao1}, 
        	{label2, mi2, mt2, vlr2, tipoDeTarificacao2},
        	{label3, mi3, mt3, vlr3, tipoDeTarificacao3},
        	{label4, mi4, mt4, vlr4, tipoDeTarificacao4},
        	//TarifComDesc

        	{label5, mi5, mt5, vlr5, tipoDeTarificacao5}, 
        	{label6, mi6, mt6, vlr6, tipoDeTarificacao6},
        	{label7, mi7, mt7, vlr7, tipoDeTarificacao7},
        	{label8, mi8, mt8, vlr8, tipoDeTarificacao8}

        });
        
    }

    
    
    static Collection<Object[]> metodoProvedorDeCasosDeTestesParaClassesDeEquivalencias_IN_Validas() {
        
    	//Classes Invalidas para tarifPadr sem desconto
    	//OS asserts sao notequals no teste
    	String label0="###[2] [TarifPadr SemDescAdic] (pegando um vlr tipico de uma classe INvalida) ";
        LocalDateTime mi0= LocalDateTime.of(2015,Month.NOVEMBER,04,19,0);
        LocalDateTime mt0 = mi0.plusMinutes(20);        
        double vlr0 = 8.0;
        String tipoDeTarificacao0 = "TarifPadr";

    	
        String label1="###[4] [TarifPadr SemDescAdic] (Explora vlr limite anterior as 8h: Ligacao de 59min que comeca 7h:59min:59Seg";
        LocalDateTime mt1 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(2);//8 horas menos dois segundos
        LocalDateTime mi1 = mt1.minusMinutes(59);
        double vlr1 = 23.6;
        String tipoDeTarificacao1 = "TarifPadr";
        

        String label2="###[7] [TarifPadr SemDescAdic] (Explora vlr limite posterior as 18h: ligacao de 59min que termina as 18:00:01)";
        LocalDateTime mi2 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,0).plusSeconds(1);
        LocalDateTime mt2 = mi2.plusMinutes(59);
        double vlr2 = 23.6;
        String tipoDeTarificacao2 = "TarifPadr";
        

        String label3="###[8] [TarifPadr SemDescAdic] (Explora vlr limite 60min: ligacao de 60min)";
        LocalDateTime mi3 = LocalDateTime.of(2015,Month.NOVEMBER,04,15,0);
        LocalDateTime mt3 = mi3.plusMinutes(60);
        double vlr3 = 23.6;
        String tipoDeTarificacao3 = "TarifPadr";

        
    	//Classes Invalidas para tarifComDes sem descontoAdicional
        //
    	String label4="###[2 11] [TarifComDesc SemDescAdic] (pegando um vlr tipico de uma classe INvalida) ";
        LocalDateTime mi4= LocalDateTime.of(2015,Month.NOVEMBER,04,10,0);
        LocalDateTime mt4 = mi4.plusMinutes(20);        
        double vlr4 = 8.0;
        String tipoDeTarificacao4 = "TarifPadr";

    	
        String label5="###[4 13] [TarifComDesc SemDescAdic] (Explora vlr limite anterior as 18h: Ligacao de 59min que comeca 17h:59min:59Seg";
        LocalDateTime mt5 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,0).minusSeconds(2);//8 horas menos dois segundos
        LocalDateTime mi5 = mt5.minusMinutes(59);
        double vlr5 = 11;
        String tipoDeTarificacao5 = "TarifComDesc";
        

        String label6="###[7 16] [TarifComDesc SemDescAdic] (Explora vlr limite posterior as 8h do dia seguinte: ligacao de 59min que termina as 8h do dia seguinte)";
        LocalDateTime mi6 = LocalDateTime.of(2015,Month.NOVEMBER,05,8,0).minusMinutes(59);
        LocalDateTime mt6 = mi6.plusMinutes(59);
        double vlr6 = 11;//isso da outro valor
        String tipoDeTarificacao6 = "TarifComDesc+DescAdic";
        

        String label7="###[8 17] [TarifComDesc SemDescAdic] (Explora vlr limite 60min: ligacao de 60min)";
        LocalDateTime mi7 = LocalDateTime.of(2015,Month.NOVEMBER,04,0,0);
        LocalDateTime mt7 = mi7.plusMinutes(60);
        double vlr7 = 11;//teria que ter o desconto adicional
        String tipoDeTarificacao7 = "TarifComDesc"; //essa tem desc adicional
        

        String label8="###[18] [TarifComDesc SemDescAdic] (Explora vlr limite 60min: ligacao de 120min)";
        LocalDateTime mi8 = LocalDateTime.of(2015,Month.NOVEMBER,04,0,0);
        LocalDateTime mt8 = mi8.plusMinutes(120);
        double vlr8 = 11;//teria que ter o desconto adicional
        String tipoDeTarificacao8 = "TarifComDesc"; //essa tem desc adicional
        

        
        
        String label9="###[20] [TarifDif SemDescAdic] (Ligacao de uma classe tipica INvalido: ligacao de 59 min das 8:00 as 8:59) ";
        LocalDateTime mi9 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(60);
        LocalDateTime mt9 = mi9.plusMinutes(59);
        double vlr9 = 23.6;
        
        String label10="###[22] [TarifDif SemDescAdic] (Ligacao de uma classe tipica valido: Explorar limite de termino antes das 8) ";
        LocalDateTime mi10 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds(60);
        LocalDateTime mt10 = mi10.plusMinutes(59);
        double vlr10 = 11.8;



        
        
        String label12="###[25] [TarifDif ComDescAdic] (Ligação de 120min de uma classe típica inválida: Ligação de 120min a partir das 10:00h) ";
        LocalDateTime mi12 = LocalDateTime.of(2015,Month.NOVEMBER,04,10,0);
        LocalDateTime mt12 = mi12.plusMinutes(120);
        double vlr12 = 24.0;
        
        String label13="###[27] [TarifDif ComDescAdic] (Ligação de 120min Explorar limite de termino antes das 8: Ligação de 120min que termina 1s antes das 8) ";
        LocalDateTime mi13 = LocalDateTime.of(2015,Month.NOVEMBER,04,8,0).minusSeconds((120*60)+1);
        LocalDateTime mt13 = mi13.plusMinutes(120);
        double vlr13 = 24.0; //como nao termina as 8 em ponto em diante nao he esse vlr que deve ser cobrado
        
        


        
        
        String label16="###[31] [TarifDif SemDescAdic] (Ligacao classe invalida: Ligacao de 59min que comeca as 20:00:00 ) ";
        LocalDateTime mi16 = LocalDateTime.of(2015,Month.NOVEMBER,04,20,00);
        LocalDateTime mt16 = mi16.plusMinutes(59);
        double vlr16 = 13.8; 
        

        String label17="###[34] [TarifDif SemDescAdic] (Explorar limite anterior 18h: Ligacao de 59 Min que termina as 17:59:59 ) ";
        LocalDateTime mi17 = LocalDateTime.of(2015,Month.NOVEMBER,04,18,00).minusSeconds((59*60)-1);
        LocalDateTime mt17 = mi17.plusMinutes(59);
        double vlr17 = 23.6; 
        

        
        return Arrays.asList(new Object[][]{
        	//tarifPadr
        	{label0, mi0, mt0, vlr0, tipoDeTarificacao0},
            {label1, mi1, mt1, vlr1, tipoDeTarificacao1},
            {label2, mi2, mt2, vlr2, tipoDeTarificacao2},
            {label3, mi3, mt3, vlr3, tipoDeTarificacao3},
            //tarifComDesc
            {label4, mi4, mt4, vlr4, tipoDeTarificacao4},
            {label5, mi5, mt5, vlr5, tipoDeTarificacao5},
            {label6, mi6, mt6, vlr6, tipoDeTarificacao6},
            {label7, mi7, mt7, vlr7, tipoDeTarificacao7},                        
        });

    }


    @AfterEach
    void tearDown() {
        //System.out.println("### @AfterEach void tearDown() {...");
    }
    
    @AfterAll
    static void tearDownAll() {        
        //System.out.println("### @AfterAll static void tearDownAll() {...");
    }
}

