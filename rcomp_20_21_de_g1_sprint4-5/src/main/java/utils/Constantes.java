package utils;

public class Constantes {

    public static final int TAMANHO_MENSAGENS = 258;

    /**
     * OFFSETS
     */
    public static final int VERSAO_OFFSET = 0;
    public static final int CODIGO_OFFSET = 1;
    public static final int NUMERO_BYTES_OFFSET = 2;
    public static final int DADOS_OFFSET = 3;

    /**
     * Códigos de tipo de pedido ou resposta na versão zero do protocolo SDP2021
     */
    public static final int TESTE = 0;
    public static final int FIM = 1;
    public static final int ENTENDIDO = 2;
    public static final int SEGMENTO = (byte) 255;
    public static final int DISPONIBILIDADE = 3;

    /**
     * Mensagens predifinidas
     */
    public static final String ENTENDIDO_RESPOSTA = "ENTENDIDO";

    /**
     * Porta predifinida
     */
    public static final int PORTA = 32507;


}
