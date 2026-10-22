---
name: integrar-front-api-back
description: Implementa e valida a integração configurável entre o React, a API intermediária Express e o backend Spring deste repositório, incluindo proxy transparente, toggle por ambiente e auditoria. Use quando a tarefa envolver conectar essas três aplicações ou alternar entre acesso direto e intermediado.
---

# Integrar frontend, API intermediária e backend

Implemente dois fluxos equivalentes:

```text
toggle=false: React -> Spring Boot
toggle=true:  React -> Express -> Spring Boot
```

O modo intermediado acrescenta rastreabilidade, mas não altera o contrato funcional do Spring.

## Invariante de roteamento

O pathname funcional deve ser idêntico em todos os trechos. Se o Spring expõe `PATCH /agendamentos/cancelar/{id}`, a API Express também recebe e encaminha `PATCH /agendamentos/cancelar/{id}`. Preserve igualmente método, query string e corpo.

- Nunca crie endpoints espelho manualmente nem uma tabela de tradução de rotas.
- Nunca acrescente `/api`, `/proxy` ou outro prefixo ao path recebido pela Express.
- A toggle altera somente a URL-base escolhida pelo frontend; os módulos de entidades continuam chamando paths como `/clientes` e `/agendamentos/{id}`.
- Prefixos usados pelo Vite ou por um reverse proxy são detalhes de infraestrutura e devem ser removidos antes de a requisição chegar ao destino.
- Se o frontend e o Spring já divergirem em algum path, reporte a divergência e corrija o contrato na origem; não a esconda no gateway.

Essa invariância é obrigatória para que desligar a toggle não quebre funcionalidades.

## Fontes de verdade

Antes de editar, leia as instruções do repositório e inspecione o estado atual destes pontos:

- mappings em `apl-api-barbershop/src/main/java/**/controller` para o contrato dos endpoints;
- `apl-barbershop/src/shared/api/httpClient.js` para a URL-base única;
- módulos `api` do frontend para os paths realmente consumidos;
- `apl-barbershop/vite.config.ts` e `.env.example` para o ambiente local;
- `apl-api-rastreabilidade/src` para o proxy e a auditoria;
- `database-info` para nomes, tipos e relacionamentos da modelagem.

Não presuma que planos ou READMEs antigos refletem o código. Preserve alterações do usuário e não aplique migrations automaticamente.

## Configuração do frontend

Centralize a decisão exclusivamente no cliente HTTP compartilhado. Use estas variáveis públicas:

```dotenv
VITE_USE_TRACE_API=false
VITE_TRACE_API_URL=/trace-api
VITE_BACKEND_API_URL=/backend-api
```

Selecione `VITE_TRACE_API_URL` quando `VITE_USE_TRACE_API` for exatamente `true`; nos demais casos selecione `VITE_BACKEND_API_URL`. Remova somente a barra final da base. Não altere os paths passados a `apiRequest`.

Variáveis `VITE_*` são incorporadas ao build e não contêm segredos. Em desenvolvimento, reinicie o Vite depois de alterá-las.

No `vite.config.ts`, configure alvos não públicos:

```dotenv
TRACE_API_PROXY_TARGET=http://localhost:3000
BACKEND_API_PROXY_TARGET=http://localhost:8080
```

O proxy local deve produzir estas transformações:

```text
/trace-api/clientes?ativo=true   -> Express /clientes?ativo=true
/backend-api/clientes?ativo=true -> Spring  /clientes?ativo=true
```

Portanto, remova `/trace-api` e `/backend-api` no Vite. Esses prefixos existem apenas entre o navegador e o servidor de desenvolvimento. Em produção, prefira origens distintas, como `https://trace.example.com` e `https://backend.example.com`; se houver prefixo no ingress, configure o ingress para removê-lo antes do destino.

## Configuração da API Express

Use `SPRING_API_URL` como origem fixa e validada do upstream. Não aceite host de destino vindo da requisição, pois isso transformaria o serviço em proxy aberto e permitiria SSRF.

O router de negócio deve ser montado na raiz, depois dos endpoints próprios de saúde. Substitua o placeholder atual sem manter a montagem `/api`. Endpoints próprios, como health checks, não são encaminhados e por isso não violam a invariância.

Para cada requisição de negócio:

1. obtenha ou gere um UUID em `X-Request-Id`;
2. preserve método, pathname e query string ao montar a URL em `SPRING_API_URL`;
3. encaminhe corpo e headers funcionais, removendo headers hop-by-hop, `Host` e `Content-Length` recebido;
4. aplique timeout e diferencie indisponibilidade (`502`) de timeout (`504`);
5. receba o corpo como bytes para não modificar JSON, texto ou resposta vazia;
6. devolva status, corpo e headers relevantes do Spring sem reinterpretar erros de domínio;
7. propague `X-Request-Id` ao Spring, à resposta e ao registro de auditoria.

Não use o cliente HTTP do frontend para compensar diferenças introduzidas pelo gateway.

## Auditoria

No fluxo intermediado, registre exatamente um ciclo por requisição, inclusive respostas 4xx/5xx, timeout e falha de conexão. O registro deve correlacionar request e response pelo mesmo `request_id` e incluir método, endpoint sem prefixo artificial, query, status, duração e metadados de tamanho/truncamento.

- Sanitize segredos, cookies, tokens, senhas e dados pessoais antes da persistência.
- Limite os corpos armazenados sem limitar silenciosamente o corpo encaminhado dentro do limite aceito pela API.
- Use a política configurada em `AUDIT_FAILURE_MODE`; documente claramente o comportamento `open` e `closed`.
- Alinhe nomes de tabela, PK e FK ao padrão aprovado em `database-info`. Não considere uma migration válida apenas porque o `INSERT` coincide com ela.
- Não confie em `X-User-Id` em produção. Use-o somente no modo de desenvolvimento já protegido pela configuração, até existir identidade assinada.

O modo direto não gera auditoria na Express. Se a auditoria for obrigatória em produção, deixe o Spring inacessível publicamente em vez de confiar somente na toggle do frontend.

## Validação obrigatória

Execute os testes e builds proporcionais às mudanças. Adicione testes de contrato que façam a mesma operação pelos dois modos e comprovem:

- o Spring recebe exatamente o mesmo método, pathname, query e corpo;
- status e corpo retornados ao frontend são equivalentes;
- `204`, 4xx, 5xx e respostas não JSON atravessam sem transformação indevida;
- timeout e upstream indisponível têm os códigos do gateway esperados;
- segredos não aparecem no banco nem nos logs operacionais;
- o modo intermediado cria exatamente um log e o direto não cria log na Express;
- alternar apenas `VITE_USE_TRACE_API` não exige alterar nenhum endpoint do frontend.

Antes de concluir, atualize `.env.example` e a documentação das aplicações com comandos de execução, portas, variáveis e o estado real da implementação. Não declare a conexão pronta se os testes ponta a ponta dependerem de infraestrutura indisponível; informe objetivamente o que foi e não foi verificado.
