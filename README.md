# Sistema Pagamento
**Projeto Integrador**


### Características

Spring Boot REST API. O serviço de pagamento possui um serviço que os demais módulos devem consumir para a forma de pagamento com boleto ou cartão. O sistema divide-se em duas aplicações distribuídas, uma web e outra api para o consumo de dados.


link para acesso a aplicação Web: https://projeto-pag-web.herokuapp.com/


### Descrição 

**1-** O Cliente poderá fazer login no sistema passando os dados por parâmetros, como email e senha.

**2-** Após fazer login o sistema gerará um token.

**3-** O Cliente com o token gerado poderá passar os dados da compra como o valor, data e o id do usuário. Na mesma requisição em Headers informar a origem e a autenticação token gerada ao fazer login.

**4-** Após passados os dados do requisito 3 acima gerará um link onde o usuário poderá copiar e colar no broswer para entar na tela de compra.


**Serviços realizados com O Postman**

Caminho para passar as informações no Postman: https://projeto-pag-api.herokuapp.com/


Podemos criar um novo usuário com a requisição post, passando por parâmetros nome email e senha.

Se os dados forem passados corretamente retornará ao status 201.

Se os dados forem passados incorretos retornará ao status de erro 500.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/PAGAE.png)


Login  utilizando Postman com a requisição Post,  passando por parâmetros email e senha. Mostrado como resposta os dados do usuário cadastrado e o token gerado após as informações dos dados.

Se os dados forem passados corretamente retornará ao status 200.

Se os dados forem passados incorretos retornará ao status de erro 404.


![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/LOGINn(1).jpeg)


Dados passados para realizar a compra com a requisição POST.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/DADOS.jpeg)


Passando Authenticação e origem no Headers para gerar o link de acesso a tela Compra.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/GERARLINK.jpeg)

Se os dados forem passados corretamente retornará ao status 200.

Se os dados forem passados incorretos retornará ao status 404.


Tela de compra que o usuário irá acessar através do link após enviar os dados necessários e fazer o consumo dos serviços. Informando a quantidade de itens escohidos.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/Compra.png)





**Caminhos da api Passados no Postman:**

POST   https://projeto-pag-api.herokuapp.com/api/usuarios/login    Login

POST  https://projeto-pag-api.herokuapp.com/api/compras/gerarLink  Gerar Link de acesso a tela compra.



**Serviços com a realização de testes com a documentação Swagger**

Api disponibilizada  para os demais módulos que irão consumir do módulo pagamento através da documentação Swagger.

usuario-json: Usuario Json,   para cadastrar um novo usuário, usuário fazer login e detalhes do usuário pelo email.

pagamento-json : Pagamento Json,  para retornar todos os pagamentos feitos pelo usuário, gerar o link para acessar a tela pagamento.


  














































