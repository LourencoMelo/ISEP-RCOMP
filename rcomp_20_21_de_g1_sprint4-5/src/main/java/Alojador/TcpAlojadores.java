package Alojador;

import protocol.AnalisadorMensagem;
import protocol.SDP2021ProtocolRequest;
import utils.Constantes;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.nio.file.Path;
import java.security.Security;

public class TcpAlojadores {

    /**
     * Socket
     */
    static ServerSocket sock;

    /**
     * Ip relacionado ao socket do srv
     */
    static InetAddress serverIP;

    static Socket socket;

    //static SSLSocket sock;

    public static void main(String[] args) throws Exception {

//        Security.addProvider(new Provider());
//
//        System.setProperty("javax.net.ssl.trustStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/client2_J.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "forgotten");
//
//        System.setProperty("javax.net.ssl.keyStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/client2_J.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword", "forgotten");


//        /**
//         * Opcional pois elimina e mostra os detalhes do handshake process
//         */
//        System.setProperty("javax.net.debug", "all");

        /**
        * Mensagem a enviar
        */
        byte[] mensagem = new byte[Constantes.TAMANHO_MENSAGENS];

        try {
//             /**
//             * SSL CLIENT SOCKET FACTORY
//             */
//            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", Constantes.PORTA);
//
//            sslSocket.startHandshake();

            /**
             * Retorna o IP da maquina atual
             */
            serverIP = InetAddress.getLocalHost();

            /**
             * Socket alojador
             */
            sock = new ServerSocket(30000);

            /**
             * Criação de um socket com o IP definido acima e a porta predefinida no enunciado
             */
            socket = new Socket(serverIP,32000);

        } catch (IOException ex) {
            /**
             * Exception é ativado caso haja um erro a criar a socket
             */
            System.out.println("Failed to open server socket");
            /**
             * Exit 1
             */
            System.exit(1);
        }

        /**
         * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
         */
        DataOutputStream sOut = new DataOutputStream(socket.getOutputStream());

        mensagem[Constantes.VERSAO_OFFSET] = 0;
        mensagem[Constantes.CODIGO_OFFSET] = Constantes.DISPONIBILIDADE;
        mensagem[Constantes.NUMERO_BYTES_OFFSET] = 0;

        /**
         * Envio o tamanho da mensagem
         */
        sOut.write((byte) mensagem.length);

        /**
         * Envio da mensagem
         */
        sOut.write(mensagem, 0, (byte) mensagem.length);

        /**
         * Espera ativa por novos alojadores
         *
         */
        while (true) {
            /**
             * Aceita uma nova socket
             */
            Socket s = sock.accept();
            /**
             * Ip associado ao socket
             */
            InetAddress ip = s.getInetAddress();
            /**
             * Escreve na consola a nova conexão e o seu IP
             */
            System.out.println("New connection from the Centro De Distribuição with ip " + ip);
            /**
             * Cria uma nova thread com socket recebido
             */
            Thread alojador = new TcpAlojadoresSrvThread(s);
            /**
             * Comeca a thread
             */
            alojador.start();
        }
    }
}

class TcpAlojadoresSrvThread extends Thread {
    /**
     * Socket
     */
    private Socket s;
    /**
     * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataOutputStream dataOutputStream;
    /**
     * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataInputStream dataInputStream;

    /**
     * Construtor
     * @param socket para guardar
     */
    public TcpAlojadoresSrvThread(Socket socket) {
        s = socket;
    }

    public void run() {
        /**
         * Variavel necessária para guardar o tamanho
         */
        int tamanho;
        /**
         * Mensagem recebida vinda do servidor
         */
        byte[] mensagem = new byte[Constantes.TAMANHO_MENSAGENS];
        try {
            /**
             * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
             */
            dataInputStream = new DataInputStream(s.getInputStream());
            /**
             * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
             */
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            /**
             * Espera ativa por novos alojadores
             */
            while (true) {
                /**
                 * Lê o tamanho
                 */
                tamanho = dataInputStream.read();
                /**
                 * Se o tamanho for igual a 0
                 */
                if (tamanho == 0) {
                    /**
                     * O loop é imediatamente terminado
                     */
                    break;
                }
                /**
                 * Le a mensagem recebida
                 */
                dataInputStream.read(mensagem, 0, (byte) mensagem.length);
                /**
                 * Formato de como a mensagem vai ser escrita na consola
                 */
                System.out.println("Mensagem recebida do Centro de distribuição: ");
                System.out.println("\tVersão protocolo : " + mensagem[Constantes.VERSAO_OFFSET]);
                System.out.println("\tCódigo mensagem : " + mensagem[Constantes.CODIGO_OFFSET]);
                System.out.println("\tTamanho da mensagem : " + mensagem.length + " bytes");
//                /**
//                 * Retorna o tipo de request que foi feito usando o protocolo
//                 */
//                SDP2021ProtocolRequest sdp2021ProtocolRequest = AnalisadorMensagem.parse(mensagem);
//                /**
//                 * Executa a classe respetiva ao request feito
//                 */
//                byte[] resposta = sdp2021ProtocolRequest.execute();
//                /**
//                 * Se a resposta for nula
//                 */
//                if (resposta == null) {
//                    /**
//                     * O loop é imediatamente terminado, e escreve na consola "Resposta nula"
//                     */
//                    System.out.println("Resposta nula");
//                    break;
//                }
//                /**
//                 * Escreve o tamanho da resposta
//                 */
//                dataOutputStream.write(resposta.length);
//                /**
//                 * Escreve a resposta
//                 */
//                dataOutputStream.write(resposta,0,resposta.length);
//                System.out.println("Resposta enviada");
//                System.out.println("\n==============================================================================\n");
            }
        } catch (IOException e) {
            /**
             * Exception é ativado caso haja um erro
             */
            System.out.println("Error");;
        }
    }
}
