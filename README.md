Projeto Integrador - Sistema de Controle de Estoque
Descrição do Projeto
Este projeto é um sistema de controle de estoque desenvolvido para pequenos e médios comércios. O sistema permite o gerenciamento de produtos, transações, promoções e alertas relacionados ao estoque. A aplicação foi desenvolvida usando Java com Spring Boot no backend e Angular no frontend, seguindo a arquitetura MVC (Model-View-Controller).

Funcionalidades Principais
Gerenciamento de Produtos:

Cadastro, edição, exclusão e visualização de produtos.
Produtos podem ser organizados por critérios como unidade, data de validade, preço por unidade, preço total e tipo de produto.
Sistema de Alerta:

Alertas automáticos para produtos com vencimento próximo ou com baixo estoque.
Promoções:

Possibilidade de criar promoções com base no estado do estoque e na validade dos produtos.
Relatórios Automatizados:

Geração de relatórios sobre o estoque, produtos mais vendidos e alertas de validade.
Sistema Multiusuário:

Contas com diferentes níveis de acesso (admin para gerenciamento completo e usuários de visualização).
Segurança:

Autenticação básica e proteção para endpoints sensíveis.
Tecnologias Utilizadas
Backend:

Java com Spring Boot
Spring Data JPA para persistência no banco de dados
Spring Security para autenticação e segurança
MySQL como banco de dados relacional
Frontend:

Angular (não implementado completamente, conforme especificado)
Estrutura do Projeto
Camada de Entidades (Model)
As classes de entidades representam as tabelas do banco de dados e são mapeadas utilizando JPA. Alguns exemplos de entidades:

Product: Representa os produtos no sistema.
User: Representa os usuários do sistema.
Transaction: Registra todas as transações de entrada e saída do estoque.
Promotion: Controla as promoções vinculadas aos produtos.
Alert: Dispara alertas de vencimento ou baixo estoque.
Camada de Repositório
A camada de repositório utiliza o Spring Data JPA para realizar operações CRUD (Create, Read, Update, Delete) nas entidades do banco de dados, sem a necessidade de escrever SQL manualmente.

Camada de Serviço
A camada de serviço implementa a lógica de negócios da aplicação. Aqui são realizadas validações, cálculos e integrações entre as camadas de controle e repositório.

Camada de Controle (Controller)
A camada de controle expõe as APIs REST que permitem interagir com o sistema via HTTP. Exemplos:

GET /products: Retorna todos os produtos.
POST /products: Cria um novo produto.
PUT /products/{id}: Atualiza um produto existente.
DELETE /products/{id}: Deleta um produto pelo seu ID.
DTO (Data Transfer Object)
Os DTOs são usados para transferir dados entre as camadas da aplicação, garantindo que apenas as informações necessárias sejam enviadas ou recebidas. Por exemplo, o ProductDTO representa os dados de um produto sem expor detalhes sensíveis.

Banco de Dados
O sistema utiliza o banco de dados MySQL com as seguintes tabelas principais:

Products: Armazena os detalhes de cada produto.
Users: Contém informações dos usuários.
Transactions: Registra as transações de movimentação de produtos.
Alerts: Gera alertas de validade e baixo estoque.
Promotions: Gerencia promoções associadas aos produtos.
