# Controle de Custos - API


![example branch parameter](https://github.com/Vince-html/Controle-custos-api/actions/workflows/ci.yml/badge.svg?branch=main)

## Descrição
Este projeto é uma API desenvolvida em Java utilizando Spring Boot 3 para gerenciar custos de produção, incluindo fornecedores, matérias-primas, valores de mão de obra, margens de lucro e tempo de execução dos trabalhos.

## Tecnologias Utilizadas
- **Spring Boot 3** - Framework para desenvolvimento rápido de aplicações Java.
- **Spring Security** - Implementação de camadas de segurança na API.
- **JWT (JSON Web Token)** - Autenticação e autorização stateless.
- **Spring Data JPA** - Facilita a integração com bancos de dados relacionais.
- **Jakarta Bean Validation** - Validação de dados da API.
- **Swagger e OpenAPI 3** - Documentação interativa da API.
- **DTO (Data Transfer Objects)** - Melhoria na segurança e eficiência da API.
- **Testes de ponta a ponta** - Garantia de qualidade e confiabilidade com WebTestClient.

## Modelagem
A API utiliza a seguinte estrutura de entidades:
- **Fornecedor**: Contém informações sobre fornecedores de matéria-prima.
- **Matéria-prima**: Representa os insumos utilizados na produção.
- **Valor Hora**: Define o custo da mão de obra.
- **Margem de Lucro**: Determina o percentual de lucro aplicado.
- **Trabalhos**: Gerencia os processos produtivos e seus custos.
- **Tempo Fracionado**: Registra o tempo de execução dos trabalhos.

## Instalação e Configuração
1. Clone este repositório:
   ```bash
   git clone https://github.com/Vince-html/Controle-custos-api.git
   ```
2. Acesse a pasta do projeto:
   ```bash
   cd Controle-custos-api
   ```
3. Configure o banco de dados no arquivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/controle_custos
   spring.datasource.username=root
   spring.datasource.password=senha
   ```
4. Execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

## Documentação da API
A documentação interativa pode ser acessada após iniciar a API, em:
```
http://localhost:8080/swagger-ui/index.html
```

## Testes
Para executar os testes automatizados, utilize:
```bash
./mvnw test
```

## Autenticação
A API utiliza autenticação baseada em JWT. Para acessar os endpoints protegidos:
1. Realize login na API e obtenha um token JWT.
2. Envie o token no cabeçalho `Authorization` das requisições:
   ```http
   Authorization: Bearer SEU_TOKEN
   ```

## Contribuição
Contribuições são bem-vindas! Para contribuir:
1. Fork este repositório.
2. Crie uma branch para sua feature (`git checkout -b minha-feature`).
3. Commit suas modificações (`git commit -m 'Adicionando minha feature'`).
4. Envie para o repositório (`git push origin minha-feature`).
5. Abra um Pull Request.

## Licença
Este projeto está sob a licença MIT. Para mais detalhes, consulte o arquivo `LICENSE`.

---
**Autor:** Vicente Augusto
**Contato:** vicente.magalhaes@live.com
**LinkedIn:** [Vince](https://www.linkedin.com/in/vicente-magalhaes/)

