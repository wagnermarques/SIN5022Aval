package usp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.time.*;
import java.util.*;
import org.junit.jupiter.params.provider.*;

//https://github.com/junit-team/junit5/blob/master/documentation/src/test/java/example/ParameterizedTestDemo.java

@DisplayName("Suite de Testes: Criterio de Particao de Equivalencia e Vlrs Limites")
public class AppTest 
{

    @BeforeAll
    static void initAll() {
        System.out.println("### @BeforeAll static void initAll() {...");
        //Estabelece os valores para tarifacao
        //private final tp = 0.40f; //tarifa padrao
        //private final descTarfComDesc = 0.50f; //desconto da tarifa com desconto
        ///private final descAdicional = 0.15f; //desconto adicional    
    }
    
    @BeforeEach
    void init() {
        System.out.println("### @BeforeEach void init() {...");
    }
    
    @ParameterizedTest
    @MethodSource("metodoProvedorDeCasosDeTestes")
    @DisplayName("Testa com particoes de Equivalencia e Vlrs Limite")
    void testaComParticoesDeEquivalenciaEVlrsLimites(LocalDateTime mi, LocalDateTime mt) {
    	Duration du = Duration.between(mi, mt);
        System.out.println(mi +" - "+mt+" = "+du);     
    }


    static Collection<Object[]> metodoProvedorDeCasosDeTestes() {

    	
//    	** Classes de Equivalência para horário de Tarifa Padrão (TarifPadr)  0.40/min (Sem Desc Adicional)
//    	   M >= 8h e M<18h Cobrança com preço cheio 0,4 p/min
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du < 60            | Du >= 60             |

        LocalDateTime mi = LocalDateTime.of(2018,Month.NOVEMBER,7,0,0);//horas é de 0 a 23 entao 7 significa 8h
        LocalDateTime mt = mi.plusMinutes(59);
        
    	
//    	** Classes de Equivalencia para horário de Tarifa Padrão (TafifPadr) + Desconto Adicional
//    	   M >= 8h e M<18h Cobrança com preço cheio 0,4 p/min menos 15% de desconto.
//    	| Variável de Entrada | Classes Eq Válidas | Classes Eq Inválidas |
//    	| Mi                  | Mi>=DD/MM/AA:8h    | Mi <  DD/MM/AA:8h    |
//    	| Mt                  | Mt< DD/MM/AA:18h   | Mt >= DD/MM/AA:18    |
//    	| Du = (Mt-Mi)        | Du > 60            | Du < 60              |
//    	No caso de Du >= 60 a cobrança já não é mais de 0.40/min e sim 0.40 - 15%/min.

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


    	
    	
        
        return Arrays.asList(new LocalDateTime[][]{
                {baseDateTime,baseDateTime.plusMinutes(20).plusSeconds(65) },
                {baseDateTime,baseDateTime.plusMinutes(30) }                
            });
    }

    
    @AfterEach
    void tearDown() {
        System.out.println("### @AfterEach void tearDown() {...");
    }
    
    @AfterAll
    static void tearDownAll() {        
        System.out.println("### @AfterAll static void tearDownAll() {...");
    }
}

