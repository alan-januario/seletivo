# Sistema de Gerenciamento de Servidores Públicos

## Descrição
API REST para gerenciamento de servidores públicos efetivos e temporários, unidades e lotações.

Gitbub: https://github.com/alan-januario/seletivo

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.4.4
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL
- MinIO (para armazenamento de objetos S3)
- Docker e Docker Compose
- Swagger/OpenAPI para documentação

## Requisitos
- Docker e Docker Compose instalados

## Como Executar

1. Clone o repositório
2. Navegue até a pasta do projeto
3. Execute o Docker Compose:

```bash
docker-compose up -d
```

4. Acesse a API em http://localhost:8080
5. Acesse a documentação Swagger em http://localhost:8080/custom-swagger-ui.html
6. Para acessar o console do MinIO, acesse http://localhost:9001 (usuário: minioadmin, senha: minioadmin)

## Autenticação

Para acessar os endpoints protegidos, é necessário obter um token JWT:

1. Faça uma requisição POST para `/api/auth/login` com as credenciais:
```json
{
  "username": "admin",
  "password": "seletivo"
}
```

2. O sistema retornará um token JWT que deve ser incluído no header `Authorization` das requisições:
```
Authorization: Bearer {token}
```

3. O token expira em 5 minutos. Para renovar, faça uma requisição POST para `/api/auth/refresh` com o refresh token:
```json
{
  "refreshToken": "{refresh-token}"
}
```

## Endpoints Principais

### Servidores Efetivos
- GET /api/servidores-efetivos - Lista todos os servidores efetivos (paginado)
- GET /api/servidores-efetivos/{id} - Busca servidor efetivo por ID
- POST /api/servidores-efetivos - Cadastra novo servidor efetivo
- PUT /api/servidores-efetivos/{id} - Atualiza servidor efetivo
- DELETE /api/servidores-efetivos/{id} - Exclui servidor efetivo
- GET /api/servidores-efetivos/unidade/{unidadeId} - Lista servidores efetivos por unidade (paginado)
- GET /api/servidores-efetivos/endereco-funcional?nome={nome} - Busca endereço funcional por nome do servidor (paginado)

### Servidores Temporários
- GET /api/servidores-temporarios - Lista todos os servidores temporários (paginado)
- GET /api/servidores-temporarios/{id} - Busca servidor temporário por ID
- POST /api/servidores-temporarios - Cadastra novo servidor temporário
- PUT /api/servidores-temporarios/{id} - Atualiza servidor temporário
- DELETE /api/servidores-temporarios/{id} - Exclui servidor temporário

### Unidades
- GET /api/unidades - Lista todas as unidades (paginado)
- GET /api/unidades/{id} - Busca unidade por ID
- POST /api/unidades - Cadastra nova unidade
- PUT /api/unidades/{id} - Atualiza unidade
- DELETE /api/unidades/{id} - Exclui unidade

### Lotações
- GET /api/lotacoes - Lista todas as lotações (paginado)
- GET /api/lotacoes/{id} - Busca lotação por ID
- POST /api/lotacoes - Cadastra nova lotação
- PUT /api/lotacoes/{id} - Atualiza lotação
- DELETE /api/lotacoes/{id} - Exclui lotação

### Fotos
- GET /api/fotos - Lista todas as fotos (paginado)
- GET /api/fotos/{id} - Busca foto por ID
- POST /api/fotos/upload/{pessoaId} - Faz upload de foto para uma pessoa
- DELETE /api/fotos/{id} - Exclui foto

## Observações
- As fotos são armazenadas no MinIO e acessadas através de URLs temporárias com expiração de 5 minutos
- A API implementa CORS para permitir acesso apenas de origens específicas configuradas
- Todos os endpoints de listagem suportam paginação
