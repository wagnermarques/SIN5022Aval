package usp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;

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
    void testaComParticoes_VALIDAS_DeEquivalenciaEVlrsLimites(LocalDateTime mi, LocalDateTime mt, double vlrFromOracle,String tpoDeTarifacaoFromOracle) {
    	Ligacao ligacao = new Ligacao(mi);
    	ligacao.encerrarLigacao(mt);     
        try {        	
        	System.out.println("Asserts..."); 
        	System.err.println(ligacao.getDu().getSeconds());
        	System.out.println();
			assertEquals(vlrFromOracle,ligacao.calculaValorDaChamada());
			assertEquals(tpoDeTarifacaoFromOracle,ligacao.getTipoDeTarifacao());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    @ParameterizedTest
    @MethodSource("metodoProvedorDeCasosDeTestesParaClassesDeEquivalencias_IN_Validas")
    @DisplayName("Testa com particoes I-N-V-A-L-I-D-A-S de Equivalencia e Vlrs Limite")
    void testaComParticoes_INVALIDAS_DeEquivalenciaEVlrsLimites(LocalDateTime mi, LocalDateTime mt, double vlrFromOracle,String tpoDeTarifacaoFromOracle) {
    	Ligacao ligacao = new Ligacao(mi);
    	ligacao.encerrarLigacao(mt);     
        try {        	
			assertNotEquals(vlrFromOracle,ligacao.calculaValorDaChamada());
			assertNotEquals(tpoDeTarifacaoFromOracle,ligacao.getTipoDeTarifacao());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    static Collection<Object[]> metodoProvedorDeCasosDeTestesParaClassesDeEquivalenciasValidas() {
    	
//    	** Classes de Equivalência para horário de Tarifa Padrão (TarifPadr)  0.40/min (Sem Desc Adicional)
//    	   M >= 8h e M<18h Cobrança com preço cheio 0,4 p/min
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du < 60            | Du >= 60             |
    	
    	//CLASSES DE EQUIVALENCIAS VALIDAS COM SEUS VALORES LIMITES
    	//inicia 8 horas em ponto e dura 20 minutos 
        LocalDateTime mi1 = LocalDateTime.of(2018,Month.NOVEMBER,25,8,0);
        LocalDateTime mt1 = mi1.plusMinutes(59);        
        double vlr1 = 23.6;
        String tipoDeTarificacao1 = "TarifPadr";
        
        LocalDateTime mi2 = LocalDateTime.of(2018,Month.NOVEMBER,25,17,0);
        LocalDateTime mt2 = mi2.plusMinutes(59);
        double vlr2 = 23.6;
        String tipoDeTarificacao2 = "TarifPadr";
        
       
        
//    	** Classes de Equivalencia para horário de Tarifa Padrão (TafifPadr) + Desconto Adicional
//    	   M >= 8h e M<18h Cobrança com preço cheio 0,4 p/min menos 15% de desconto.
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du > 60            | Du < 60              |
//    	No caso de Du >= 60 a cobrança já não é mais de 0.40/min e sim 0.40 - 15%/min.

        LocalDateTime mi3 = LocalDateTime.of(2018,Month.NOVEMBER,25,8,0);
        LocalDateTime mt3 = mi3.plusMinutes(61);        
        double vlr3 = 20.74;
        String tipoDeTarificacao3 = "TarifPadr_ComDescAdicional";
      
        //pra terminar ainda no tarifPadr tem que ser um minutos a menos
        //e menos 1 segundo porque nao pode chegar a 18 inclusive
        LocalDateTime mi4 = LocalDateTime.of(2018,Month.NOVEMBER,25,17,0).minusMinutes(1).minusSeconds(1);
        LocalDateTime mt4 = mi4.plusMinutes(61);
        double vlr4 = 20.74;
        String tipoDeTarificacao4 = "TarifPadr_ComDescAdicional";
        
        
        
//    	** Classes de Equivalência para horário de tarifa com desconto (TarifComDesc) de 50% 
//    	*** Para o caso de inicio na tarifa com desconto e termino na tarifa padrao  (Vice)
//    	    Mi >= 18h e Mt > 8h 
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi >= DD/MM/AA:18h | Mi >= DD/MM/AA:8h    |
//    	| Mt                  | Mt >= DD/MM/AA:8h  | Mt qualquer          |
//    	| Du = (Mt-Mi)        | Du < 60            | Du > 60              |

//    	*** Para o caso de inicio na tarifa padrao e término na tarifa com desconto (Versa)
//    	    Mi >=8 e Mt > 18h
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du < 60            | Du > 60              |

//    	** Classes de Equivalência para horário de tarifa com desconto (TafifComDesc) de 50% mais 15% de desconto
//    	*** Para o caso de inicio na tarifa com desconto e termino na tarifa padrao  (Vice) + Desc Adicional
//    	    Mi >= 18h e Mt > 8h 
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi >= DD/MM/AA:18h | Mi >= DD/MM/AA:8h    |
//    	| Mt                  | Mt >= DD/MM/AA:8h  | Mt qualquer          |
//    	| Du = (Mt-Mi)        | Du > 60            | Du < 60              |

//    	*** Para o caso de inicio na tarifa padrao e término na tarifa com desconto (Versa) + Desc Adicional
//    	    Mi >=8 e Mt > 18h
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du > 60            | Du < 60              |


    	
    	
        return Arrays.asList(new Object[][]{
        	{mi1,mt1,vlr1,tipoDeTarificacao1},
        	{mi2,mt2,vlr2,tipoDeTarificacao2},
        	{mi3,mt3,vlr3,tipoDeTarificacao3},
        	{mi4,mt4,vlr4,tipoDeTarificacao4}
        });
        
    }

    
    
    static Collection<Object[]> metodoProvedorDeCasosDeTestesParaClassesDeEquivalencias_IN_Validas() {
        
    	//Classes Invalidas para tarifPadr sem desconto
    	//OS asserts sao notequals no teste
        LocalDateTime mt1 = LocalDateTime.of(2018,Month.NOVEMBER,25,8,0).minusSeconds(2);//8 horas menos dois segundos
        LocalDateTime mi1 = mt1.minusMinutes(59);
        double vlr1 = 23.6;
        String tipoDeTarificacao1 = "TarifPadr";
        
        LocalDateTime mt2 = LocalDateTime.of(2018,Month.NOVEMBER,25,18,0).plusSeconds(2);//dois segundos apos as 18
        LocalDateTime mi2 = mt2.minusMinutes(59);
        double vlr2 = 23.6;
        String tipoDeTarificacao2 = "TarifPadr";


        //Invalidas para tarifPadr com descontoadicional
        LocalDateTime mi3 = LocalDateTime.of(2018,Month.NOVEMBER,25,8,0).minusSeconds(1);
        LocalDateTime mt3 = mi3.plusMinutes(61);        
        double vlr3 = 20.74;
        String tipoDeTarificacao3 = "TarifPadr_ComDescAdicional";

        LocalDateTime mi4 = LocalDateTime.of(2018,Month.NOVEMBER,25,18,0).plusSeconds(1);
        LocalDateTime mt4 = mi4.plusMinutes(61);
        double vlr4 = 20.74;
        String tipoDeTarificacao4 = "TarifPadr_ComDescAdicional";
        
        
        return Arrays.asList(new Object[][]{
            {mi1,mt1,vlr1,tipoDeTarificacao1},
            {mi2,mt2,vlr2,tipoDeTarificacao2},
            {mi3,mt3,vlr3,tipoDeTarificacao3}
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

