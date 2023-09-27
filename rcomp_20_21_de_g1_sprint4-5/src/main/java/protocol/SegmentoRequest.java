package protocol;

import utils.Constantes;

public class SegmentoRequest extends SDP2021ProtocolRequest{
    /**
     * Constructor to save the request
     *
     * @param request
     */
    protected SegmentoRequest(byte[] request) {
        super(request);
    }

    @Override
    public byte[] execute() {
        System.out.println("|Segmento Request|");
        byte[] mensagem_recebida = messageType().getBytes(); ////Recebe a informação passada pelo cliente
        System.out.println(messageType());

        byte[] resposta_servidor = new byte[Constantes.TAMANHO_MENSAGENS];
        resposta_servidor[Constantes.VERSAO_OFFSET] = 0;
        resposta_servidor[Constantes.CODIGO_OFFSET] = Constantes.ENTENDIDO; //retorna resposta com código 2 como pedido no enunciado
        resposta_servidor[Constantes.NUMERO_BYTES_OFFSET] = 0; //Respostas do tipo 2 não transportam dados pelo que o tamanho é 0
        //Campo Dados não existe uma vez que o tamanho definido na linha anterior foi 0
        return resposta_servidor; //mudar para ter mais um codigo para tratar error check , se ele recebe informação toda
    }

    @Override
    public String messageType() {
        return "Entendido";
    }


}
