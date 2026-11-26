# Observabilidade com Grafana

Esta pasta contém a implementação da observabilidade da API de rastreabilidade do Barbershop. O Grafana consulta os logs HTTP gravados pelo gateway Express na tabela MySQL `db_barbershop.api_request_log`.

## Arquitetura

```text
React :5173 -> Gateway Express :3000 -> Spring Boot :8080
                    |
                    +-> MySQL / api_request_log
                              ^
                              |
                       Grafana :3001
```

O gateway grava os logs com um usuário que possui permissão de escrita. O Grafana usa outro usuário, `barbershop_grafana`, com permissão somente de leitura. Essa separação impede que uma credencial usada para visualização altere ou exclua dados da aplicação.

## Estrutura dos arquivos

```text
grafana/
|-- configure-grafana.ps1
|-- dashboards/
|   `-- rastreabilidade.json
`-- provisioning/
    |-- dashboards/
    |   `-- barbershop.yaml
    `-- datasources/
        `-- barbershop-mysql.yaml
```

- `configure-grafana.ps1`: cria ou atualiza o datasource pela API do Grafana, testa o MySQL e importa a dashboard;
- `dashboards/rastreabilidade.json`: definição versionada da dashboard;
- `provisioning/dashboards/barbershop.yaml`: provider que carrega dashboards a partir do sistema de arquivos;
- `provisioning/datasources/barbershop-mysql.yaml`: configuração declarativa do datasource MySQL.

## Pré-requisitos

- Grafana acessível em `http://localhost:3001`;
- MySQL com o schema `db_barbershop` e a tabela `api_request_log`;
- credenciais administrativas do Grafana;
- usuário MySQL somente leitura para o Grafana.

Crie o usuário no MySQL, substituindo a senha de exemplo:

```sql
CREATE USER 'barbershop_grafana'@'localhost'
IDENTIFIED BY 'senha-forte-aqui';

GRANT SELECT
ON db_barbershop.api_request_log
TO 'barbershop_grafana'@'localhost';

FLUSH PRIVILEGES;
```

## Configuração automática pela API

Na raiz do projeto, execute:

```powershell
./grafana/configure-grafana.ps1
```

O script solicita as credenciais do Grafana e a senha do usuário `barbershop_grafana` sem gravá-las em arquivos. Em seguida, ele:

1. valida se o Grafana está saudável;
2. cria ou atualiza o datasource de UID `barbershop-mysql`;
3. testa a conexão com o MySQL;
4. importa ou atualiza a dashboard de UID `barbershop-rastreabilidade`;
5. imprime a URL da dashboard.

Para utilizar outros endereços:

```powershell
./grafana/configure-grafana.ps1 `
  -GrafanaUrl "http://localhost:3001" `
  -DatabaseHost "localhost:3306" `
  -DatabaseName "db_barbershop" `
  -DatabaseUser "barbershop_grafana"
```

Dashboard:

```text
http://localhost:3001/d/barbershop-rastreabilidade/rastreabilidade-da-api
```

## Provisionamento por arquivos

O provisionamento declarativo é recomendado para VMs, containers e pipelines. Disponibilize os YAMLs nas pastas de provisionamento do Grafana e o JSON em uma pasta de dashboards.

Exemplo em Linux:

```text
/etc/grafana/provisioning/datasources/barbershop-mysql.yaml
/etc/grafana/provisioning/dashboards/barbershop.yaml
/var/lib/grafana/dashboards/rastreabilidade.json
```

Defina no ambiente do processo do Grafana:

```dotenv
BARBERSHOP_DB_PASSWORD=senha-do-usuario-grafana
BARBERSHOP_GRAFANA_DASHBOARDS_PATH=/var/lib/grafana/dashboards
```

Depois, reinicie o serviço:

```bash
sudo systemctl restart grafana-server
sudo systemctl status grafana-server
```

Não adicione a senha real ao YAML ou ao repositório. Em produção, prefira um secret manager ou um arquivo de ambiente protegido pelo sistema operacional.

## Dashboard implementada

A dashboard usa por padrão as últimas 24 horas e atualiza a cada 10 segundos. Ela possui os seguintes painéis:

- total de requisições;
- taxa de sucesso;
- quantidade de respostas com erro;
- latência média;
- usuários identificados;
- requisições ao longo do tempo por classe HTTP;
- evolução da latência média e máxima;
- distribuição por classe HTTP;
- dez endpoints mais acessados;
- tabela com os erros recentes.

Todos os painéis usam o datasource de UID `barbershop-mysql` e respeitam o intervalo de tempo selecionado no Grafana.

## Tratamento de data e hora

O gateway grava `started_at` e `finished_at` em UTC. Por isso, o datasource configura:

```yaml
timezone: "+00:00"
```

Sem essa configuração, uma instalação em `America/Sao_Paulo` pode interpretar os registros com três horas de diferença e exibir cartões zerados mesmo quando existem logs no banco.

## Segurança e implantação em nuvem

Em uma VM ou ambiente de nuvem:

- conecte Grafana e MySQL por rede privada, VPN ou peering;
- permita a porta `3306` apenas a partir do IP ou security group do Grafana;
- restrinja o usuário MySQL ao endereço privado da VM;
- use TLS na conexão com o banco;
- publique o Grafana atrás de HTTPS;
- mantenha o usuário do Grafana limitado a `SELECT`;
- armazene a senha em um serviço de segredos.

O gateway deve continuar usando sua própria credencial com `INSERT` em `api_request_log`. Não configure o gateway com `barbershop_grafana`, pois a auditoria deixaria de gravar registros.

## Diagnóstico

Verifique a saúde do Grafana:

```powershell
Invoke-RestMethod http://localhost:3001/api/health
```

Verifique se o gateway consegue acessar a tabela de logs:

```powershell
Invoke-RestMethod http://localhost:3000/health/ready
```

Gere um log de teste:

```powershell
Invoke-WebRequest http://localhost:3000/servicos
```

Confirme os registros diretamente no MySQL:

```sql
SELECT
    request_id,
    http_method,
    endpoint,
    response_status,
    duration_ms,
    started_at
FROM db_barbershop.api_request_log
ORDER BY id_api_request_log DESC
LIMIT 20;
```

Se o datasource conectar, mas a dashboard ficar vazia, confira o período selecionado, a configuração UTC e se as chamadas estão passando pelo gateway Express na porta `3000`.
