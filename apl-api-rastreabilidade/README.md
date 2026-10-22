# API de rastreabilidade

API Node.js + Express planejada para ocupar a camada intermediária entre o frontend React (`apl-barbershop`) e o backend Spring Boot (`apl-api-barbershop`).

## Estado desta entrega

Esta entrega contém somente a estrutura inicial. O encaminhamento dos endpoints de negócio **ainda não está implementado**, conforme o escopo definido para esta etapa.

Já estão disponíveis:

- aplicação Express e desligamento gracioso;
- configuração e validação de variáveis de ambiente;
- pool MySQL e repositório preparado para os logs;
- migration da tabela estruturada `api_request_log`;
- funções de classificação, sanitização e limitação dos dados auditáveis;
- endpoints de saúde;
- rota reservada `/api/*`, que atualmente responde `501 PROXY_NOT_IMPLEMENTED`;
- testes unitários da configuração e das regras de auditoria.

Ainda não estão disponíveis:

- encaminhamento das requisições para o Spring;
- captura e persistência dos ciclos reais de request/response;
- autenticação e identificação definitiva do usuário;
- toggle e redirecionamento no frontend;
- testes de contrato e ponta a ponta.

## Arquitetura planejada

```text
React
  |
  | VITE_USE_TRACE_API=true (fase futura)
  v
API Express :3000 -----> Spring Boot :8080
  |
  +--------------------> MySQL / api_request_log
```

Com a toggle desligada, o React continuará acessando o Spring diretamente. Em produção, se a auditoria for obrigatória, o Spring deverá ficar em rede privada para impedir que clientes contornem a API intermediária.

## Estrutura

```text
apl-api-rastreabilidade/
├── migrations/
│   └── 001_create_api_request_log.sql
├── src/
│   ├── routes/
│   │   └── proxyRouter.js
│   ├── app.js
│   ├── audit.js
│   ├── config.js
│   ├── database.js
│   └── server.js
├── test/
├── .env.example
└── package.json
```

O router é apenas um ponto de extensão nesta fase. Na próxima etapa, a orquestração do proxy, o acesso ao upstream e a auditoria devem ser separados em módulos próprios, evitando regras de domínio no gateway.

## Banco de dados

O Spring usa H2 em memória no perfil atual. Como essa instância não pode ser compartilhada com outro processo, a API intermediária foi preparada para persistir a auditoria no MySQL descrito em `database-info`.

A tabela `api_request_log` guarda, de forma estruturada:

- UUID de correlação;
- início, fim e duração;
- método, endpoint e query params;
- headers sanitizados;
- request e response sanitizados e limitados;
- status HTTP, IP, user-agent e identificação opcional do usuário;
- indicadores de truncamento e código de erro técnico.

Para um banco já existente, execute:

```bash
mysql -u <usuario-migration> -p db_barbershop < migrations/001_create_api_request_log.sql
```

Use uma credencial de migration para criar a tabela. Em execução, a API deve usar uma credencial de privilégio mínimo, com `INSERT` em `api_request_log` e acesso suficiente para o health check.

## Configuração

Copie `.env.example` para `.env` e ajuste os valores. Principais variáveis:

| Variável | Finalidade |
| --- | --- |
| `PORT` | Porta da API Express |
| `SPRING_API_URL` | Origem fixa do backend Spring para a fase do proxy |
| `UPSTREAM_TIMEOUT_MS` | Timeout futuro do upstream |
| `REQUEST_BODY_LIMIT` | Limite aceito pelo Express |
| `AUDIT_BODY_MAX_BYTES` | Limite de request/response armazenado |
| `AUDIT_FAILURE_MODE` | Política futura `open` ou `closed` |
| `DB_*` | Conexão com o MySQL |

`ALLOW_DEV_USER_HEADER=true` reserva o uso de `X-User-Id` apenas para desenvolvimento. A aplicação recusa essa opção quando `NODE_ENV=production`.

## Execução local

Requisitos: Node.js 22 ou superior e MySQL 8.

```bash
npm install
npm run dev
```

Rotas disponíveis:

- `GET /health/live`: verifica se o processo está ativo;
- `GET /health/ready`: verifica o acesso ao MySQL;
- qualquer método em `/api/*`: placeholder que responde HTTP 501.

Para executar os testes:

```bash
npm test
```

## Próximas fases

1. Implementar o proxy transparente preservando método, path, query, corpo, status e headers relevantes.
2. Gravar exatamente um log por ciclo completo, inclusive para 4xx, 5xx, timeout e falha de conexão.
3. Propagar `X-Request-Id` ao Spring e ao frontend.
4. Adicionar a toggle `VITE_USE_TRACE_API` no cliente HTTP central do React.
5. Comparar chamadas diretas e intermediadas com testes de contrato.
6. Adicionar autenticação real; nunca confiar em um ID de usuário livre enviado pelo browser em produção.

O plano técnico detalhado está em [`../docs/plano-api-rastreabilidade.md`](../docs/plano-api-rastreabilidade.md).
