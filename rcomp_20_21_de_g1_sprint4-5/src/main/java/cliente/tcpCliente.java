package cliente;

import com.sun.net.ssl.internal.ssl.Provider;
import utils.Constantes;

import java.io.*;
import java.net.*;
import java.security.Security;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class tcpCliente {

    /**
     * Ip relacionado ao socket do srv
     */
    static InetAddress serverIP;
    /**
     * Socket predefinido no enunciado
     */
    static Socket sock;

//    static SSLSocket sock;

    public static void main(String[] args) throws Exception {
        /**
         * Mensagem a enviar
         */
        byte[] mensagem = new byte[Constantes.TAMANHO_MENSAGENS];

//        Security.addProvider(new Provider());
//
//        System.setProperty("javax.net.ssl.trustStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/client1_J.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "forgotten");
//
//        System.setProperty("javax.net.ssl.keyStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/client1_J.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword", "forgotten");


//        /**
//         * Opcional pois elimina e mostra os detalhes do handshake process
//         */
//        System.setProperty("javax.net.debug", "all");

        try {
//            /**
//             * SLL CLIENT SOCKET FACTORY
//             */
//            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", Constantes.PORTA);
//            
//            sslSocket.startHandshake();


            /**
             * Retorna o IP do servidor
             */
            serverIP = InetAddress.getLocalHost();

        } catch (UnknownHostException ex) {
            /**
             * Tratamento de erro caso nao exista
             */
            System.out.println("Invalid server specified: " + args[0]);
            System.exit(1);
        }
        try {
            /**
             * Criacao de um socket com o IP definido acima e a porta predefinida no enunciado
             */
            sock = new Socket(serverIP, Constantes.PORTA);
        } catch (IOException ex) {
            /**
             * Tratamento de erro na criação do socket
             */
            System.out.println("Failed to establish TCP connection");
            System.exit(1);
        }

        /**
         * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
         */
        DataOutputStream sOut = new DataOutputStream(sock.getOutputStream());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Conectado ao servidor!");

        /**
         * Criação de uma nova thread
         */
        Thread server_connection = new Thread(new TcpClienteConnection(sock));

        /**
         * Inicializa thread
         */
        server_connection.start();


        String answer;


        while (true) {

            System.out.println("\n\n0 -> Teste\n1 -> Fim\n2 -> Entendido\n255 -> Segmento\n");
            System.out.println("\nEscolha o tipo de mensagem que pretende enviar: ");
            answer = bufferedReader.readLine();

            switch (answer.trim()) {
                case "0":
                    mensagem[Constantes.VERSAO_OFFSET] = 0;
                    mensagem[Constantes.CODIGO_OFFSET] = Constantes.TESTE;
                    mensagem[Constantes.NUMERO_BYTES_OFFSET] = 0;
                    break;
                case "1":
                    mensagem[Constantes.VERSAO_OFFSET] = 0;
                    mensagem[Constantes.CODIGO_OFFSET] = Constantes.FIM;
                    mensagem[Constantes.NUMERO_BYTES_OFFSET] = 0;
                    break;
                case "2":
                    mensagem[Constantes.VERSAO_OFFSET] = 0;
                    mensagem[Constantes.CODIGO_OFFSET] = Constantes.ENTENDIDO;
                    mensagem[Constantes.NUMERO_BYTES_OFFSET] = 0;
                    break;
                case "255":
                    mensagem[Constantes.VERSAO_OFFSET] = 0;
                    mensagem[Constantes.CODIGO_OFFSET] = Constantes.SEGMENTO;
                    mensagem[Constantes.NUMERO_BYTES_OFFSET] = 0;
                    break;
                default:
                    System.out.println("Please choose one above!");
            }

            /**
             * Envio o tamanho da mensagem
             */
            sOut.write((byte) mensagem.length);

            /**
             * Envio da mensagem
             */
            sOut.write(mensagem, 0, (byte) mensagem.length);

            /**
             * Estrutura da mensagem
             */
            System.out.println("\nMensagem enviada ao Centro de Distribuição :");
            System.out.println("\tVersão protocolo : " + mensagem[Constantes.VERSAO_OFFSET]);
            System.out.println("\tCódigo mensagem : " + mensagem[Constantes.CODIGO_OFFSET]);
            System.out.println("\tTamanho da mensagem : " + mensagem.length + " bytes");
            System.out.println("\n================================================\n");

            Thread.sleep(1000);

            if (answer.equalsIgnoreCase("1")) {
                break;
            }

        }

        System.out.println("Saí da conexão");

        /**
         * Join na thread
         */
        server_connection.join();

        /**
         * Fecha o socket
         */
        sock.close();
    }
}

class TcpClienteConnection implements Runnable {
    /**
     * Inicaliza o socket na thread
     */
    private Socket s;
    /**
     * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataInputStream sIN;

    /**
     * Construtor da thread
     *
     * @param tcp_s para guardar
     */
    public TcpClienteConnection(Socket tcp_s) {
        s = tcp_s;
    }

    public void run() {


        /**
         * tamanho da mensagem
         */
        int tamanho;

        /**
         * Mensagem recebida do servidor
         */
        byte[] mensagem_recebida = new byte[Constantes.TAMANHO_MENSAGENS];

        try {

            /**
             * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil
             */
            sIN = new DataInputStream(s.getInputStream());

            while (true) {

                /**
                 * Le o tamanho
                 */
                tamanho = sIN.read();

                System.out.println("Tamanho : " + tamanho);

                if (tamanho == 0) {

                    /**
                     * Se o tamanho for 0
                     */
                    break;
                }
                /**
                 * Le a mensagem recebida
                 */
                sIN.read(mensagem_recebida, 0, tamanho);

                /**
                 * Formato da mensagem recebida
                 */
                System.out.println("\nMensagem recebida do Centro de Distribuição:");
                System.out.println("\tVersão protocolo : " + mensagem_recebida[Constantes.VERSAO_OFFSET]);
                System.out.println("\tCódigo mensagem : " + mensagem_recebida[Constantes.CODIGO_OFFSET]);
                System.out.println("\tTamanho da mensagem : " + mensagem_recebida.length + " bytes");

                //Thread.sleep(4000);

            }

        } catch (Exception e) {
            /**
             * Caso não haja client ou se tenha desconectado
             */
            System.out.println("Client disconnected!");
        }
    }

}


