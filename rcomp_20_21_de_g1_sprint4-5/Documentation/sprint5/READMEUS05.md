RCOMP 2020-2021 Project - Sprint 1 - Member 1190811 folder
===========================================

Este readme tem como objetivo explicar a tarefa 5 do Sprint 4 de RCOMP.

Foi utilizado o protocolo SDP2021 como em todas as US's.

Nesta US foi pedido implementar as comunicações entre o Cliente emissor com a aplicação Centro de Distribuição(CDD).

Ao dar run o cliente irá enviar uma mensagem ao Centro de Distribuição, que irá ser recebida com a mesma integridade pelo Servidor(Centro de Distribuição) que irá responder ao cliente uma mensagem de entendido(Código 2), para mostrar ao cliente que recebeu a mensagem sem nenhum problema.

De modo a garantir estas funcionalidades foram criadas as seguintes classes:

* tcpCliente(atua como aplicação cliente de acordo com o protocolo SDP2021(cliente emissor));
* TcpClienteConnection.

Após o cliente emitir uma mensagem com o protocolo previamente definido, o servidor deve receber essa mensagem e trata-la e enviar uma mensagem de resposta para o Cliente de entendido.  


### Protocolo SDP2021:

| Código | Significado/Utilização |
|:-------------:| :------------- |
|0| **Teste** - Pedido de teste sem qualquer efeito para além da devolução de uma resposta com código 2. Este pedido não transporta dados.|
|1| **Fim** - Pedido de fim de ligação. O servidor deve devolver uma resposta com código 2, após o que ambas as aplicações devem fechar a ligação TCP. |
|2| **Entendido** - Resposta vazia (não transporta dados) que acusa a receção de um pedido. É enviada em resposta a pedidos com código 0 e código 1, mas poderá ser usada em outros contextos.|
|3| **Disponibilidade** - Pedido para ser inserido na lista de alojadores disponíveis do centro de distribuição.|
|...| ___________________________________________________________________ |
|255| **Segmento** - identifica os dados transportado como sendo uma parte de um conjunto de dados mais extenso. Este código é usado para transferir volumes de dados superiores a 255 bytes. Nesse cenário um pedido ou uma resposta pode ser constituído por uma sequência de mensagens com código 255 finalizada por uma mensagem contendo um código diferente de 255. |

Nota: Na totalidade da US é utilizado o protocolo SDP2021.

### Pedidos :

Como podemos verificar na tabela acima:
> * caso o valor do byte for 0, teremos que realizar um pedido de <u>Teste</u> , com devolução de um resposta com código 2 (<u>Entendido</u>);
> *  caso o valor do byte for 1, teremos que realizar um pedido de <u>Fim</u>. Este pedido tem como objetivo fechar
     ambas as ligações TPC e envia uma mensagem de código 2 (<u>Entendido</u>);
> * caso o valor do byte for 2, teremos que realizar um pedido de <u>Entendido</u>, este pedido apenas retornará uma resposta com dados nulos.
> * caso o valor do byte for 255, realizar-se-à um pedido de <u>Segmento</u>.

Nota: os pedidos sem código correspondente irão produzir uma mensagem de </u>erro</u>.

### Formato Mensagem:

| Designação de campo | Posição(bytes) | Comprimento(bytes)|
|:-------------:| :-------------: | :-------------: |
|**Versão**|0|1|
|**Código**|1|1|
|**Nº de bytes**|2|1|
|**Dados**|3|'>'=0|

>Nota:
> O campo "Dados" poderá não existir nos casos em que o n.º de bytes seja 0.

### Explicação do código implementado:

O código implementado foi considerado a partir das Téoricas, TP's e a partir de alguns exercícios das PL.
A conexão TCP é estabelecida instanciando um objeto de classe de Socket especificando para o construtor o endereço IP do servidor e o número da porta.
Para ler e escrever através da conexão TCP estabelecida, o InputStream do socket conectado e OutputStream são usados.
A implementação do protocolo SDP2021 está parcialmente implementado, é perguntado na consola o tipo de mensagem que o cliente quer enviar e depois utilizando um switch é gerada a mensagem requerida e essa mesma é enviada para o Centro de Distribuição.
Assim e de modo a facilitar a compreensão de qualquer leitor do código, o mesmo foi comentado em JavaDOC.
Todos os métodos e instruções estão comentadas explicando brevemente a sua funcionalidade.

* SSL:
- Adicionando o providor JSSE (Java Secure Socket Extension) que fornece protocolos SSL e TLS e inclui funcionalidade para criptografia de dados, autenticação de servidor, integridade de mensagem, e autenticação de cliente que esta é opcional.
- Especifica-se o arquivo de armazenamento de chave que contém o certificado / chave pública e a chave privada.
- Especifica-se a senha do arquivo keystore.
- SSLServerSocketFactory estabelece o contexto ssl e cria SSLServerSocket.
- Cria-se SSLServerSocket usando SSLServerSocketFactory contexto SSL estabelecido.
- No cliente estabelece-se o handshake.
