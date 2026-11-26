# Projeto Barbershop

<p align="center">
  <img src="apl-barbershop/public/logo.png" alt="Logo JR Barbershop" width="420" />
</p>

Aplicação web para gerenciamento de barbearia, com agenda, clientes, serviços, expediente e controle financeiro.

## Arquitetura

O repositório possui três aplicações:

- `apl-barbershop`: frontend React + Vite, porta `5173`;
- `apl-api-rastreabilidade`: gateway Express, porta `3000`;
- `apl-api-barbershop`: backend Spring Boot, porta `8080`.

O frontend pode operar pelos dois fluxos abaixo:

```text
VITE_USE_TRACE_API=false: React -> Spring Boot
VITE_USE_TRACE_API=true:  React -> Express -> Spring Boot
                                      |
                                      +-> MySQL / api_request_log
```

## Pré-requisitos

- Java 21;
- Maven, caso o Maven Wrapper não esteja disponível;
- Node.js 22 ou superior;
- npm;
- MySQL 8.

As portas `5173`, `3000`, `8080` e `3306` devem estar disponíveis.

## 1. Preparar o MySQL

Execute `database-info/script.sql` no MySQL Workbench. Ele cria o schema `db_barbershop`, incluindo a tabela de auditoria `api_request_log`.

Crie um usuário local para o gateway:

```sql
CREATE USER IF NOT EXISTS 'barbershop_logger'@'localhost'
IDENTIFIED BY 'senha-local';

GRANT INSERT, SELECT
ON db_barbershop.api_request_log
TO 'barbershop_logger'@'localhost';

FLUSH PRIVILEGES;
```

No perfil atual, o Spring Boot usa um banco H2 em memória para os dados da aplicação. O gateway usa o MySQL para armazenar a auditoria.

## 2. Iniciar o backend Spring Boot

Em um terminal PowerShell:

```powershell
cd "apl-api-barbershop"
./mvnw.cmd spring-boot:run
```

Se o Maven Wrapper não estiver disponível:

```powershell
mvn spring-boot:run
```

Valide o backend:

```powershell
Invoke-RestMethod http://localhost:8080/servicos
```

Swagger: `http://localhost:8080/swagger-ui/index.html`.

## 3. Iniciar a API de rastreabilidade

Em outro terminal PowerShell:

```powershell
cd "apl-api-rastreabilidade"
Copy-Item .env.example .env
npm install
```

Configure o arquivo `.env`:

```dotenv
NODE_ENV=development
PORT=3000
SPRING_API_URL=http://localhost:8080
AUDIT_FAILURE_MODE=open

DB_HOST=localhost
DB_PORT=3306
DB_NAME=db_barbershop
DB_USER=barbershop_logger
DB_PASSWORD=senha-local

ALLOW_DEV_USER_HEADER=true
```

Inicie o gateway:

```powershell
npm run dev
```

Valide os health checks:

```powershell
Invoke-RestMethod http://localhost:3000/health/live
Invoke-RestMethod http://localhost:3000/health/ready
```

Os dois endpoints devem retornar `UP`. Se `/health/ready` retornar `503`, verifique o MySQL, as credenciais e a existência da tabela `api_request_log`.

## 4. Iniciar o frontend

Em um terceiro terminal PowerShell:

```powershell
cd "apl-barbershop"
Copy-Item .env.example .env.local
npm install
```

Para testar o fluxo completo com rastreabilidade, configure `.env.local` assim:

```dotenv
VITE_USE_TRACE_API=true
VITE_TRACE_API_URL=/trace-api
VITE_BACKEND_API_URL=/backend-api

TRACE_API_PROXY_TARGET=http://localhost:3000
BACKEND_API_PROXY_TARGET=http://localhost:8080
```

Inicie o frontend:

```powershell
npm run dev
```

Acesse `http://localhost:5173`.

Variáveis `VITE_*` são carregadas na inicialização. Reinicie o Vite sempre que alterar `.env.local`.

## 5. Testar a integração e a auditoria

Faça uma chamada pelo gateway:

```powershell
$response = Invoke-WebRequest http://localhost:3000/servicos
$response.StatusCode
$response.Headers["X-Request-Id"]
$response.Content
```

Consulte o registro correspondente no MySQL:

```sql
SELECT
    request_id,
    event_type,
    http_method,
    endpoint,
    response_status,
    duration_ms,
    fk_usuario,
    started_at
FROM db_barbershop.api_request_log
ORDER BY id_api_request_log DESC;
```

A chamada deve aparecer como `GET /servicos`, com status `200`. O `request_id` salvo deve ser igual ao header `X-Request-Id` retornado pelo gateway.

Para testar o acesso direto ao Spring, altere no frontend:

```dotenv
VITE_USE_TRACE_API=false
```

Depois, reinicie o Vite. Nesse modo as funcionalidades continuam usando o backend, mas as chamadas não passam pelo gateway e não geram registros em `api_request_log`.

## Testes automatizados

Backend:

```powershell
cd "apl-api-barbershop"
./mvnw.cmd test
```

Gateway:

```powershell
cd "apl-api-rastreabilidade"
npm test
```

Frontend:

```powershell
cd "apl-barbershop"
npm test
npm run lint
npm run build
```

## Observação sobre autenticação

O frontend usa `POST /autenticacao/login` com e-mail e senha. Entretanto, o seed atual do H2 contém hashes fictícios e o backend ainda compara a senha textualmente. A autenticação com BCrypt permanece como evolução pendente e requer alteração específica no backend.

## Documentação

- `docs/swagger-backend.md`: contrato OpenAPI do backend;
- `docs/plano-api-rastreabilidade.md`: arquitetura e decisões do gateway;
- `apl-api-rastreabilidade/README.md`: detalhes da API de rastreabilidade;
- `apl-barbershop/README.md`: detalhes do frontend.
