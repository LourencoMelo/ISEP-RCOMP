package centroDistribuicao;


//import com.sun.net.ssl.internal.ssl.Provider;
import protocol.AnalisadorMensagem;
import protocol.SDP2021ProtocolRequest;
import utils.Constantes;


import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.security.Security;
import java.util.HashMap;


class TcpCentroDeDistribuicao {
    /**
     * Lista de alojadores disponíveis a armazenar dados de clientes emissores
     */
    private static HashMap<Socket, DataOutputStream> lista_alojadores_disponíveis = new HashMap<>();

    /**
     * Adiciona alojador À lista de alojadores disponiveis
     *
     * @param s do enunciado
     * @throws Exception caso nao adicione
     */
    public static synchronized void addAlojador(Socket s) throws Exception {
        lista_alojadores_disponíveis.put(s, new DataOutputStream(s.getOutputStream()));
    }

    /**
     * Remover alojador À lista de alojadores disponiveis
     *
     * @param s do enunciado
     * @throws Exception caso nao remova
     */
    public static synchronized void removeAlojador(Socket s) throws Exception {
        lista_alojadores_disponíveis.get(s).write(0);
        lista_alojadores_disponíveis.remove(s);
        s.close();
    }

    public static synchronized void send_answer(DataOutputStream dataOutputStream, int tamanho, byte[] resposta) throws Exception {
        dataOutputStream.write(tamanho);
        dataOutputStream.write(resposta);
    }

    /**
     * Socket do enunciado
     */
    private static ServerSocket socket;

    /**
     * Socket do alojador
     */
    private static ServerSocket alojador_socket;

//    /**
//     * SSL serverSocket
//     */
//    public  static SSLServerSocket socket;


    public static void main(String[] args) throws IOException {

//        Security.addProvider(new Provider());
//
//        System.setProperty("javax.net.ssl.trustStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/TcpCentroDeDistribuicao.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "forgotten");
//
//        System.setProperty("javax.net.ssl.keyStore", "rcomp_20_21_de_g1_sprint4-5/src/main/java/SSL/keys/TcpCentroDeDistribuicao.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword","forgotten");
//
//        /**
//         * Opcional pois elimina e mostra os detalhes do handshake process
//         */
//        System.setProperty("javax.net.debug","all");

        try {
//            /**
//             * SLL SERVER SOCKET FACTORY
//             */
//            SSLServerSocketFactory sllServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//            socket = (SSLServerSocket) sllServerSocketFactory.createServerSocket(Constantes.PORTA);
//            socket.setNeedClientAuth(true);


            /**
             * Criação de um socket com o IP definido acima e a porta predefinida no enunciado
             */
            socket = new ServerSocket(Constantes.PORTA);

            alojador_socket = new ServerSocket(32000);

        } catch (IOException e) {
            /**
             * Tratamento de erro quando nao cria um socket com sucesso
             */
            System.out.println("Porta local não disponível!");
            System.exit(1);
        }

        System.out.println("\n\t\t\tO servidor está agora ligado!\n\n");
        
        /**
         * Espera ativa por novos clientes
         */
        while (true) {
            /**
             * Aceita um socket cliente
             */
            Socket sa = alojador_socket.accept(); // waits for a new alojador

            Socket s = socket.accept();  //waits for a new client request

            InetAddress ip = s.getInetAddress();

            InetAddress ipA = sa.getInetAddress();

            System.out.println("> Um cliente conectou-se ao servidor.\n");

            System.out.println("> O alojador com ip " + ipA + " conectou-se ao servidor.\n");

            /**
             * Cria uma nova thread com socket recebido
             */
            Thread cliente = new TcpClienteThread(s); //Creates new thread for incoming client

            Thread alojador = new TcpAlojadorThread(sa); // Creates a new thread for the "alojador"

            /**
             * Começa as threads
             */
            cliente.start();
            alojador.start();
        }

    }
}

class TcpClienteThread extends Thread {
    /**
     * Inicaliza o socket na thread
     */
    private Socket cliente_socket;
    /**
     * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataInputStream dataInputStream;
    /**
     * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataOutputStream dataOutputStream;

    /**
     * Construtor da thread
     *
     * @param s para guardar
     */
    public TcpClienteThread(Socket s) {
        cliente_socket = s;
    }

    public void run() {
        /**
         * tamanho da mensagem
         */
        int tamanho;

        /**
         * Mensagem recebida do servidor
         */
        byte[] mensagem = new byte[Constantes.TAMANHO_MENSAGENS];
        
        try {
            /**
             * Espera ativa por novos clientes
             */
            /**
             * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil
             */
            dataInputStream = new DataInputStream(cliente_socket.getInputStream());

            /**
             * Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil
             */
            dataOutputStream = new DataOutputStream(cliente_socket.getOutputStream());


            while (true) {

                /**
                 * Lê o tamanho da mensagem recebida
                 */
                tamanho = dataInputStream.read();

                /**
                 * Caso o tamanho seja igual a 0 termina o ciclo
                 */
                if (tamanho == 0) {
                    System.out.println("Tamanho mensagem igual a 0");
                    break;
                }

                /**
                 * Le a mensagem recebida
                 */
                dataInputStream.read(mensagem, 0, tamanho);

                /**
                 * Formato da mensagem recebida
                 */
                System.out.println("Mensagem recebida do Cliente: ");
                System.out.println("\tVersão protocolo : " + mensagem[Constantes.VERSAO_OFFSET]);
                System.out.println("\tCódigo mensagem : " + mensagem[Constantes.CODIGO_OFFSET]);
                System.out.println("\tTamanho da mensagem : " + tamanho + " bytes");


                //Sai se a mensagem recebida for do tipo fim o que quer dizer que o cliente pretende sair da conexão
                if (mensagem[Constantes.CODIGO_OFFSET] == Constantes.FIM) {
                    dataOutputStream.write((byte) 0);
                    System.out.println("\n\t\t\tCliente pretende sair!");
                    break;
                }

                Socket socketParaAlojador = new Socket(InetAddress.getLocalHost(), 30000);

                DataOutputStream dataOutputParaAlojador = new DataOutputStream(socketParaAlojador.getOutputStream());

                dataOutputParaAlojador.write((byte) mensagem.length);

                dataOutputParaAlojador.write(mensagem, 0, (byte) mensagem.length);

                /**
                 * Redireciona a mensagem recebida para o analisador de mensagens
                 */
                SDP2021ProtocolRequest sdp2021ProtocolRequest = AnalisadorMensagem.parse(mensagem);

                /**
                 * Guarda a resposta enviada pelo protocolo numa variável
                 */
                byte[] resposta = sdp2021ProtocolRequest.execute();

                System.out.println("Tamanho da resposta : " + resposta.length);

                System.out.println("\n================================================\n");

//                dataOutputStream.write(tamanho);
//                dataOutputStream.write(resposta);

                TcpCentroDeDistribuicao.send_answer(dataOutputStream, (byte) resposta.length, resposta);

            }

            System.out.println("\nTCP connection ended!");

            //Fecha a socket caso a ligação com o cliente acabe
            cliente_socket.close();
        } catch (Exception e) {
            /**
             * Caso dê erro
             */
            System.out.println("Error");
        }
    }
}

class TcpAlojadorThread extends Thread {

    /**
     * Inicaliza o socket na thread
     */
    private Socket alojador_socket;
    /**
     * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataInputStream dataInputStream;
    /**
     * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
     */
    private DataOutputStream dataOutputStream;

    /**
     * Construtor da thread
     *
     * @param s para guardar
     */
    public TcpAlojadorThread(Socket s) {
        alojador_socket = s;
    }

    public void run() {
        /**
         * tamanho da mensagem
         */
        int tamanho;
        /**
         * Mensagem recebida do servidor
         */
        byte[] mensagem = new byte[Constantes.TAMANHO_MENSAGENS];

        try {
            /**
             * Permite ler tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
             */
            dataInputStream = new DataInputStream(alojador_socket.getInputStream());
            /**
             * ‎ Permite escrever tipos primitivos de dados Java para um fluxo de saída de forma portátil‎
             */
            dataOutputStream = new DataOutputStream(alojador_socket.getOutputStream());
            /**
             * Espera ativa por novos alojadores
             */
            while (true) {
                /**
                 * Le o tamanho
                 */
                tamanho = dataInputStream.read();
                if (tamanho == 0) {
                    break;
                }
                /**
                 * Le a mensagem recebida
                 */
                dataInputStream.read(mensagem, 0, (byte) mensagem.length);
                /**
                 * Formato da mensagem recebida
                 */
                System.out.println("Mensagem recebida do alojador : ");
                System.out.println("\tVersão protocolo : " + mensagem[Constantes.VERSAO_OFFSET]);
                System.out.println("\tCódigo mensagem : " + mensagem[Constantes.CODIGO_OFFSET]);
                System.out.println("\tTamanho da mensagem : " + mensagem.length + " bytes");

                if (mensagem[Constantes.CODIGO_OFFSET] == Constantes.DISPONIBILIDADE) {
                    TcpCentroDeDistribuicao.addAlojador(alojador_socket);
                }

                /**
                 * Redireciona a mensagem recebida para o analisador de mensagens
                 */
                SDP2021ProtocolRequest sdp2021ProtocolRequest = AnalisadorMensagem.parse(mensagem); //Retorna o tipo de request que foi feito usando o protocolo
                /**
                 * Guarda a resposta enviada pelo protocolo numa variavel
                 */
                byte[] resposta = sdp2021ProtocolRequest.execute();
                /**
                 * Se a resposta for nula
                 */
                if (resposta == null) {
                    System.out.println("Resposta nula");
                    break;
                }
                /**
                 * Escreve o tamanho da resposta
                 */
                dataOutputStream.write(resposta.length);
                /**
                 * Escreve a resposta
                 */
                dataOutputStream.write(resposta, 0, resposta.length);
                System.out.println("Resposta enviada");
                System.out.println("\n==============================================================================\n");
            }
        } catch (Exception e) {
            /**
             * Caso dê erro
             */
            System.out.println("Error");
            ;
        }
    }
}