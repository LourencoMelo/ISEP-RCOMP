RCOMP 2019-2020 Projeto - Sprint 4 US02
=========================================

Este readme tem como objetivo explicar a tarefa individual 2. Tarefa esta que consistia 
na implementação de comunicações com as aplicações Alojador, gerir a lista de alojadores e sempre 
que existir um ficheiro para enviar, proceder ao envio do mesmo.

### Alojadores:

  * Recebem ficheiros da aplicação Centro de Distribuição;
  * Os ficheiros recebidos devem ser guardados numa pasta local;
  * Um Alojador deve ser capaz de receber simultaneamente diferentes ficheiros do Centro de Distribuição;
  
### Centro de distribuição:

  * O Centro de Distribuição gere uma lista de alojadores disponíveis, quando um alojador contacta o Centro de Distribuição é acrescentado à lista (se não existir) e registado como disponível;
  * Quando o Centro de Distribuição tenta
  contactar um alojador para lhe enviar um ficheiro e falha, esse alojador passa ao estado indisponível, e o ficheiro terá
  de ser enviado para outro alojador;
  
### De modo a garantir estas funcionalidades foram criadas as seguintes classes:

* tcpCentroDeDistribuicaoCliente;
* tcpAlojadoresSrv (atua como aplicação servidora de acordo com o protocolo SDP2021).

Nota: Na totalidade da US é utilizado o protocolo SDP2021.

### Protocolo SDP2021:

| Código | Significado/Utilização |
|:-------------:| :------------- |
|0| **Teste** - Pedido de teste sem qualquer efeito para além da devolução de uma resposta com código 2. Este pedido não transporta dados.|
|1| **Fim** - Pedido de fim de ligação. O servidor deve devolver uma resposta com código 2, após o que ambas as aplicações devem fechar a ligação TCP. |
|2| **Entendido** - Resposta vazia (não transporta dados) que acusa a receção de um pedido. É enviada em resposta a pedidos com código 0 e código 1, mas poderá ser usada em outros contextos.|
|3| **Disponibilidade** - Pedido para ser inserido na lista de alojadores disponíveis do centro de distribuição.|
|...| ___________________________________________________________________ |
|255| **Segmento** - identifica os dados transportado como sendo uma parte de um conjunto de dados mais extenso. Este código é usado para transferir volumes de dados superiores a 255 bytes. Nesse cenário um pedido ou uma resposta pode ser constituído por uma sequência de mensagens com código 255 finalizada por uma mensagem contendo um código diferente de 255. |
>Por exemplo, para enviar um pedido ou resposta com código XXX transportando um conteúdo de 400 bytes: 
1º - É enviada uma mensagem com código 255 contendo os primeiros 255 bytes. 
2º - É enviada uma mensagem com código XXX contendo os restantes 145 bytes.
>
### Pedidos :

Como podemos verificar na tabela acima :
> * caso o valor do byte for 0, teremos que realizar um pedido de <u>Teste</u> , com devolução de um resposta com código 2 (<u>Entendido</u>);
> *  caso o valor do byte for 1, teremos que realizar um pedido de <u>Fim</u>. Este pedido tem como objetivo fechar
ambas as ligações TPC e envia uma mensagem de código 2 (<u>Entendido</u>);
> * caso o valor do byte for 2, teremos que realizar um pedido de <u>Entendido</u>, este pedido apenas retornará uma resposta com dados nulos.
> * caso o valor do byte for 255, teremos que realizar um pedido de <u>Segmento</u>. 

Nota: os pedidos sem código correspondente irão produzir uma mensagem de </u>erro</u>.

### Explicação do código implementado:

Como não foi criada a aplicação Dashboard , foi criada uma aplicação em jframe para tentar substituir esse requisito. A aplicação permite ao cliente escolher um ficheiro à sua vontade, que depois de selecionado pode ser enviado para o servidor. Na janela do servidor da para visualizar os ficheiros enviados e permite descarrega-los. Nesta aplicação não foi utilizado o protocolo definido no enunciado, pois a nossa implementação ainda não estava totalmente correta para poder usá-la na aplicação. Foi definido assim o host como “localhost” e a port como “1234”.
O envio e receção de ficheiros não está totalmente implementada e encontra-se não funcional neste Sprint.
O código implementado foi considerado a partir das Téoricas, TP's e a partir de alguns exercícios das PL.
Assim e de modo a facilitar a compreensão de qualquer leitor do código, o mesmo foi comentado em JavaDOC.
Todos os métodos e instruções estão comentadas explicando brevemente a sua funcionalidade.