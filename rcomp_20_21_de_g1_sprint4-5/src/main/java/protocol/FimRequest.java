package protocol;

import utils.Constantes;

public class FimRequest extends SDP2021ProtocolRequest{

    /**
     * Constructor to save the request
     *
     * @param request
     */
    protected FimRequest(byte[] request) {
        super(request);
    }

    @Override
        public byte[] execute() {
            System.out.println("Foi feito um request de Fim!");
            byte[] mensagem_recebida = messageType().getBytes(); //Recebe a informação passada pelo cliente
            System.out.println(messageType());

            byte[] resposta_servidor = new byte[258]; //Inicializa array bytes auxiliara para colocar dados recebidos

            resposta_servidor[Constantes.VERSAO_OFFSET] = 0;
            resposta_servidor[Constantes.CODIGO_OFFSET] = Constantes.ENTENDIDO; //retorna resposta com código 2 como pedido no enunciado
            resposta_servidor[Constantes.NUMERO_BYTES_OFFSET] = 0; //Respostas do tipo 2 não transportam dados pelo que o tamanho é 0
            //Campo Dados não existe uma vez que o tamanho definido na linha anterior foi 0

            return resposta_servidor;

        }

        @Override
        public String messageType() {
            return Constantes.ENTENDIDO_RESPOSTA;
        }
    }
