package protocol;

import utils.Constantes;

public class AnalisadorMensagem {

    public static SDP2021ProtocolRequest parse(byte[] mensagem) {
        SDP2021ProtocolRequest sdp2021ProtocolRequest = new SDP2021ProtocolRequest(mensagem) {
            @Override
            public byte[] execute() {
                return new byte[Constantes.TAMANHO_MENSAGENS];
            }

            @Override
            public String messageType() {
                return "Protocolo\n";
            }
        };

        /**
         * Verifica se a versão de protocolo inserida na mensagem é a versão que o sistema suporta(versõa 0) e se os dados contidos na mensagem têm tamanho diferente de 0
         */
        if ((mensagem[Constantes.VERSAO_OFFSET] == 0)) {
            sdp2021ProtocolRequest = redirect(mensagem);
        } else {
            System.out.println("ERRO! Por favor verifique que o protocolo está bem definido!\n");
        }
        return sdp2021ProtocolRequest;
    }

    public static SDP2021ProtocolRequest redirect(byte[] mensagem) {
        SDP2021ProtocolRequest sdp2021ProtocolRequest;

        switch (mensagem[Constantes.CODIGO_OFFSET]) {
            case Constantes.TESTE:
                sdp2021ProtocolRequest = new TesteRequest(mensagem);
                break;
            case Constantes.FIM:
                sdp2021ProtocolRequest = new FimRequest(mensagem);
                break;
            case Constantes.SEGMENTO:
                sdp2021ProtocolRequest = new SegmentoRequest(mensagem);
                break;
            case Constantes.DISPONIBILIDADE:
                sdp2021ProtocolRequest = new DisponibilidadeRequest(mensagem);
                break;
            default:
                return null;
        }
        return sdp2021ProtocolRequest;
    }
}
