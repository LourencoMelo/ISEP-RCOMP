RCOMP 2020-2021 Projeto - Sprint 4 US03
=========================================

Este readme visa explicar a tarefa 3 do Sprint 4 de RCOMP realizada por 1190718.

Nesta tarefa era pedida a implementação de uma conexão tcp entre centro de distribuição e clientes emissores. Para tal,
foram criadas duas aplicações:

* Aplicação Centro de Distribuição(atua como aplicação cliente de acordo com o protocolo SDP2021);
* Aplicação Cliente Emissor(atua como aplicação servidora de acordo com o protocolo SDP2021).

Após ser enviada a mensagem pelo cliente emissor para o centro de distribuição, o último deve enviar uma resposta predefinida de acordo com o protocolo SDP2021.
>Exemplo:
No caso do cliente mandar uma mensagem com o código 0 ou 1, o Centro de Distribuição deve enviar uma resposta com código 2 que representa "Entendido".

### Protocolo definido:

| Código | Significado/Utilização |
|:-------------:| :------------- |
|0| **Teste** - Pedido de teste sem qualquer efeito para além da devolução de uma resposta com código 2. Este pedido não transporta dados.|
|1| **Fim** - Pedido de fim de ligação. O servidor deve devolver uma resposta com código 2, após o que ambas as aplicações devem fechar a ligação TCP. |
|2| **Entendido** - Resposta vazia (não transporta dados) que acusa a receção de um pedido. É enviada em resposta a pedidos com código 0 e código 1, mas poderá ser usada em outros contextos.|
|3| **Disponibilidade** - Pedido para ser inserido na lista de alojadores disponíveis do centro de distribuição.|
|...| ___________________________________________________________________ |
|255| **Segmento** - Identifica os dados transportado como sendo uma parte de um conjunto de dados mais extenso. Este código é usado para transferir volumes de dados superiores a 255 bytes. Nesse cenário um pedido ou uma resposta pode ser constituído por uma sequência de mensagens com código 255 finalizada por uma mensagem contendo um código diferente de 255. |
>Por exemplo, para enviar um pedido ou resposta com código XXX transportando um conteúdo de 400 bytes: 
1º - É enviada uma mensagem com código 255 contendo os primeiros 255 bytes. 
2º - É enviada uma mensagem com código XXX contendo os restantes 145 bytes.

### Pedidos:

Como podemos verificar na tabela acima:
> * caso o valor do byte for 0, teremos que realizar um pedido de <u>Teste</u>, com devolução de um resposta com código 2 (<u>Entendido</u>);
> *  caso o valor do byte for 1, teremos que realizar um pedido de <u>Fim</u>. Este pedido visa fechar
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
Na classe "TcpCentroDeDistribuicao" temos a classe em si com o método main() implementado e a classe (TcpClienteThread) que define a thread que vai ser criada para cada cliente conectado.
Um objeto de classe ServerSocket é instanciado, o construtor recebe o número da porta local onde conexões TCP
devem ser recebidas. Em seguida, o método accept () é chamado num loop(while true) e para cada conexão estabelecida
um tópico é criado e iniciado.
Assim e de modo a facilitar a compreensão de qualquer leitor do código, o mesmo foi comentado em JavaDOC.
Todos os métodos e instruções estão comentadas explicando brevemente a sua funcionalidade.

### SSL:
- Adicionando o providor JSSE (Java Secure Socket Extension) que fornece protocolos SSL e TLS e inclui funcionalidade para criptografia de dados, autenticação de servidor, integridade de mensagem, e autenticação de cliente que esta é opcional.
- Especifica-se o arquivo de armazenamento de chave que contém o certificado / chave pública e a chave privada.
- Especifica-se a senha do arquivo keystore.
- SSLServerSocketFactory estabelece o contexto ssl e cria SSLServerSocket.
- Cria-se SSLServerSocket usando SSLServerSocketFactory contexto SSL estabelecido.
- No cliente estabelece-se o handshake.