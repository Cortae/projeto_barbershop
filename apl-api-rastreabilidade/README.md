# API de rastreabilidade

API Node.js/Express que atua como proxy transparente entre o frontend React e o backend Spring Boot. Ela preserva método, path, query, corpo, status e headers funcionais e adiciona correlação e auditoria.

## Fluxo

```text
React -> Express :3000 -> Spring :8080
             |
             +-> MySQL / api_request_log
```

O Express recebe os endpoints de negócio na raiz. Por exemplo, `PATCH /agendamentos/cancelar/15` é encaminhado para o mesmo path no Spring. `/health/live` e `/health/ready` são endpoints próprios e não são encaminhados.

## Preparação

Requisitos: Node.js 22+, MySQL 8 e o Spring em execução.

```bash
npm install
cp .env.example .env
mysql -u <usuario-migration> -p db_barbershop < migrations/001_create_api_request_log.sql
npm run dev
```

A migration é manual e não é aplicada pela aplicação.

`api_request_log` substitui `log_sistema` na modelagem oficial. Sua coluna `fk_usuario` é anulável e referencia `usuario.id_usuario` com `ON DELETE SET NULL`, preservando auditorias anônimas e o histórico de usuários removidos.

| Variável | Padrão | Finalidade |
| --- | --- | --- |
| `PORT` | `3000` | Porta do Express |
| `SPRING_API_URL` | `http://localhost:8080` | Origem fixa do Spring |
| `UPSTREAM_TIMEOUT_MS` | `10000` | Timeout do Spring |
| `REQUEST_BODY_LIMIT` | `1mb` | Limite do corpo encaminhado |
| `AUDIT_BODY_MAX_BYTES` | `65536` | Limite persistido de cada corpo |
| `AUDIT_FAILURE_MODE` | `open` | Política de falha da auditoria |
| `DB_*` | — | Conexão MySQL |

`AUDIT_FAILURE_MODE=open` entrega a resposta do Spring mesmo se o log falhar. `closed` devolve `503 AUDIT_UNAVAILABLE`. `ALLOW_DEV_USER_HEADER=true` permite associar `X-User-Id` apenas fora de produção; ainda não há identidade assinada.

## Comportamento

- propaga `X-Request-Id` ao Spring, resposta e auditoria;
- remove headers hop-by-hop, `Host`, `Content-Length` e `Cookie`;
- devolve `504` em timeout e `502` quando o Spring está indisponível;
- preserva erros 4xx/5xx gerados pelo Spring;
- sanitiza segredos e mascara e-mail/telefone antes de persistir;
- registra um ciclo por chamada intermediada.

## Verificação

```bash
npm test
```

Os testes cobrem equivalência direta/intermediada, `204`, 4xx, 5xx, texto não JSON, timeout, indisponibilidade, correlação e sanitização.

O Spring usa H2 no perfil atual, enquanto a auditoria usa o MySQL configurado. Sem MySQL ou sem a tabela `api_request_log`, o proxy continua operando somente no modo `open`; `/health/ready` retorna indisponibilidade.
