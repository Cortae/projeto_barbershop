---
name: iniciar-ambiente-barbershop
description: Inicia, reinicia e valida o ambiente local deste repositório com MySQL, backend Spring Boot, gateway Express e frontend Vite. Use quando o usuário pedir para subir, ligar, reiniciar ou verificar as três aplicações do projeto Barbershop.
---

# Iniciar o ambiente Barbershop

Suba o ambiente completo no fluxo rastreável:

```text
React :5173 -> Express :3000 -> Spring Boot :8080
                    |
                    +-> MySQL :3306 / api_request_log
```

O Spring usa H2 em memória para os dados funcionais. O MySQL armazena a modelagem oficial e a auditoria do gateway. Não migre o Spring para MySQL durante uma simples inicialização.

## Antes de iniciar

1. Leia o `README.md` da raiz e os READMEs das três aplicações se tiverem mudado desde a última execução.
2. Preserve alterações existentes e arquivos `.env`; nunca substitua credenciais já configuradas.
3. Confirme Java 21, Node 22+, npm e MySQL 8.
4. Verifique as portas `3306`, `8080`, `3000` e `5173`. Se uma porta estiver ocupada, identifique e teste o processo antes de iniciar outro. Não encerre processos sem confirmar que pertencem a este ambiente.
5. Confirme que o serviço MySQL está ativo. No Windows local, o nome esperado é `MySQL80`.

Não reaplique `database-info/script.sql` em toda inicialização. Aplique-o somente se `db_barbershop` ou suas 13 tabelas ainda não existirem, solicitando uma credencial administrativa do MySQL antes da mutação. A tabela essencial para readiness é `db_barbershop.api_request_log`.

## Configuração local

O gateway exige `apl-api-rastreabilidade/.env`. Se estiver ausente, copie `.env.example` e solicite/defina credenciais próprias do usuário `barbershop_logger`; não reutilize nem exponha a senha administrativa do MySQL. O usuário precisa apenas de `INSERT, SELECT` em `db_barbershop.api_request_log`.

O frontend exige `apl-barbershop/.env.local`. Para o fluxo completo, confirme:

```dotenv
VITE_USE_TRACE_API=true
VITE_TRACE_API_URL=/trace-api
VITE_BACKEND_API_URL=/backend-api
VITE_REPORT_API_URL=/trace-api
TRACE_API_PROXY_TARGET=http://localhost:3000
BACKEND_API_PROXY_TARGET=http://localhost:8080
```

Esses arquivos são ignorados pelo Git. Nunca inclua seus valores secretos na resposta ao usuário.

Instale dependências somente quando `node_modules` estiver ausente ou incompatível com o lockfile:

```powershell
cd apl-api-rastreabilidade
npm install
npx puppeteer browsers install chrome

cd ..\apl-barbershop
npm install
```

Downloads podem exigir aprovação. Não execute `npm audit fix` automaticamente.

## Ordem de inicialização

### 1. Backend Spring Boot

Garanta que o processo Maven use JDK 21 sem alterar permanentemente o `JAVA_HOME` global. Neste computador, o `JAVA_HOME` global pode apontar para JDK 17 mesmo quando `java` no `PATH` é 21.

Prefira:

```powershell
cd apl-api-barbershop
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.10'
.\mvnw.cmd spring-boot:run
```

Se o wrapper falhar no PowerShell ao acessar `Target[0]`, localize um `mvn.cmd` já instalado em `%USERPROFILE%\.m2\wrapper\dists` e execute-o com Java 21. Use um repositório Maven gravável, como `target/m2repo`, se o padrão resolver incorretamente para `C:\.m2\repository`:

```powershell
& '<caminho-encontrado>\mvn.cmd' '-Dmaven.repo.local=target/m2repo' spring-boot:run
```

Espere `GET http://localhost:8080/servicos` retornar `200` antes de continuar.

### 2. Gateway Express

```powershell
cd apl-api-rastreabilidade
npm start
```

Espere ambos retornarem `200` e `UP`:

```text
http://localhost:3000/health/live
http://localhost:3000/health/ready
```

`ready=DOWN` indica problema no MySQL, nas credenciais ou na tabela `api_request_log`; não é motivo para iniciar outra instância do gateway.

### 3. Frontend Vite

```powershell
cd apl-barbershop
npm run dev
```

Espere `http://localhost:5173` retornar `200`. Reinicie o Vite sempre que alterar qualquer variável `VITE_*`.

Ao iniciar em segundo plano no Windows, use `Start-Process` com `-WindowStyle Hidden`, diretórios de trabalho explícitos e logs separados em `%TEMP%\barbershop-runtime`. Informe ao usuário os processos e onde consultar os logs.

## Validação obrigatória

Antes de declarar o ambiente pronto, valide:

1. `GET :8080/servicos` retorna `200`.
2. `GET :3000/health/live` e `/health/ready` retornam `200`/`UP`.
3. `GET :3000/servicos` retorna `200` e um `X-Request-Id`.
4. `GET :5173` retorna `200`.
5. `GET :5173/trace-api/servicos` retorna `200` e um `X-Request-Id`, comprovando o proxy do Vite.
6. O registro mais recente em `api_request_log` contém o mesmo `request_id` da chamada pelo gateway.

Quando a geração de relatórios estiver no escopo, faça também um `POST /relatorios/financeiro/pdf` pelo prefixo `/trace-api`. Confirme PDF `200` e auditoria no MySQL. O log deve guardar tipo e tamanho do PDF, nunca o binário.

Chamadas diretas a `:8080` não são auditadas. Health checks também não são eventos de negócio. Com `VITE_USE_TRACE_API=true`, as chamadas funcionais do frontend devem passar por `:3000`; a geração de PDF termina no próprio gateway e também deve ser auditada.

## Diagnóstico e encerramento

- Consulte primeiro os logs do processo que não passou no health check.
- Se precisar reiniciar, encerre apenas o PID que efetivamente escuta a porta correspondente e seus filhos conhecidos.
- Não mate todos os processos `java`, `node` ou `chrome` da máquina.
- Não altere regras funcionais apenas para fazer a aplicação iniciar.
- Ao concluir, informe URLs, status dos quatro componentes, validação da auditoria e qualquer teste conhecido que permaneça falhando.
