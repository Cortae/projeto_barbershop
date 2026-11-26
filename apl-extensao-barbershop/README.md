# Extensão JR Barbershop

Extensão Chrome Manifest V3 que permite cadastrar agendamentos em um painel lateral sem sair da guia atual. O caso de uso principal é registrar um atendimento enquanto a barbearia conversa com o cliente pelo WhatsApp Web.

## Funcionalidades

- autenticação com as mesmas credenciais do sistema web;
- sessão temporária de cinco minutos;
- seleção do barbeiro pelo nome;
- busca de clientes por nome ou telefone;
- seleção de um ou mais serviços;
- preenchimento de data e horário;
- cálculo visual de duração e valor;
- persistência automática do rascunho;
- modal de confirmação após o cadastro;
- temas claro e escuro iguais aos do frontend;
- tratamento de conflitos e erros retornados pelo backend.

## Arquitetura

```text
Extensão Chrome (painel lateral)
             |
             | HTTP / JSON
             v
Gateway de rastreabilidade :3000
             |
             v
Backend Spring Boot :8080
             |
             v
Banco de dados
```

A extensão sempre se comunica com `http://localhost:3000`. O endereço não pode ser alterado pela interface. Assim, as requisições passam pelo gateway e continuam participando do fluxo de auditoria do projeto.

## Estrutura de arquivos

```text
apl-extensao-barbershop/
├── assets/
│   ├── icon.png          # ícone exibido pelo Chrome
│   └── logo.png          # logotipo mostrado no cabeçalho
├── api.js                # cliente HTTP e tratamento de erros
├── background.js         # abre o painel ao clicar na extensão
├── manifest.json         # permissões e configuração Manifest V3
├── panel.css             # temas e componentes visuais
├── panel.html            # estrutura do painel e modal
├── panel.js              # estado, autenticação e formulário
└── README.md
```

## Requisitos

- Google Chrome 116 ou superior;
- backend Spring Boot em `http://localhost:8080`;
- gateway de rastreabilidade em `http://localhost:3000`;
- banco e demais dependências configurados conforme o README da raiz.

## Instalação local

1. Inicie o backend e o gateway.
2. Abra `chrome://extensions`.
3. Ative o **Modo do desenvolvedor**.
4. Clique em **Carregar sem compactação**.
5. Selecione a pasta `apl-extensao-barbershop`.
6. Fixe a extensão na barra do Chrome.
7. Clique no ícone da JR Barbershop para abrir o painel lateral.

Após alterações nos arquivos, clique em **Recarregar** no cartão da extensão em `chrome://extensions`.

## Permissões do manifesto

| Permissão | Finalidade |
| --- | --- |
| `sidePanel` | Exibir o formulário ao lado da guia atual. |
| `storage` | Armazenar sessão temporária, tema e rascunho. |
| `http://localhost:3000/*` | Consumir exclusivamente o gateway local. |

A extensão não solicita acesso ao conteúdo do WhatsApp ou de outras páginas abertas. Ela apenas permanece disponível ao lado dessas páginas.

## Autenticação e sessão

O login envia e-mail e senha para:

```http
POST /autenticacao/login
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "senha": "sua-senha"
}
```

Quando o backend aceita as credenciais:

1. o usuário retornado e o horário de expiração são gravados em `chrome.storage.session`;
2. a senha é descartada e nunca é armazenada;
3. uma contagem regressiva de cinco minutos é exibida;
4. ao expirar, o formulário é bloqueado e um novo login é solicitado.

O último e-mail utilizado é salvo para facilitar o próximo acesso.

> A autenticação atual não utiliza token. Portanto, ela controla o acesso à interface da extensão, mas não protege chamadas feitas diretamente à API. Uma proteção completa exige autenticação no backend, por exemplo com Spring Security e JWT.

## Carregamento do formulário

Após o login, os dados são carregados em paralelo:

| Recurso | Endpoint | Uso |
| --- | --- | --- |
| Clientes | `GET /clientes` | Busca por nome ou telefone. |
| Serviços | `GET /servicos?ativo=true` | Exibe apenas serviços ativos. |
| Barbeiros | `GET /usuarios/barbeiros` | Exibe nomes e mantém os IDs internamente. |

O endpoint de barbeiros foi incluído no backend especificamente para evitar que o usuário precise informar um identificador numérico.

## Cadastro de agendamento

O formulário exige:

- barbeiro;
- cliente;
- ao menos um serviço;
- data futura;
- horário futuro.

O envio utiliza o contrato existente do backend:

```http
POST /agendamentos
Content-Type: application/json

{
  "barbeiro": 2,
  "cliente": 1,
  "servicos": [1, 2],
  "inicio": "2026-12-21T09:00:00"
}
```

O backend continua responsável por validar a existência dos registros, serviços inativos, bloqueios e conflitos de horário.

Após uma resposta bem-sucedida:

1. um modal informa o cliente, a data e o horário;
2. o barbeiro, cliente, serviços, data e horário são limpos;
3. o rascunho é removido;
4. o formulário fica pronto para o próximo agendamento.

## Armazenamento local

| Chave | Área | Conteúdo |
| --- | --- | --- |
| `authSession` | `chrome.storage.session` | Usuário autenticado e expiração. |
| `appointmentDraft` | `chrome.storage.local` | Barbeiro, cliente, serviços, data e horário. |
| `lastLoginEmail` | `chrome.storage.local` | Último e-mail utilizado. |
| `jr-theme` | `chrome.storage.local` | Tema `light` ou `dark`. |

O rascunho permite fechar e reabrir o painel sem perder um agendamento ainda não enviado.

## Temas e identidade visual

Os temas usam os mesmos tokens do frontend:

- verde como cor primária;
- superfícies grafite no tema escuro;
- cartões brancos e fundo cinza-claro no tema claro;
- cores específicas para sucesso, alerta e erro;
- raios e estados de foco equivalentes aos componentes da aplicação.

Na primeira execução, a extensão acompanha a preferência do sistema operacional. Depois disso, a seleção realizada no cabeçalho é preservada.

O logotipo e o ícone são cópias dos assets oficiais em `apl-barbershop/public`.

## Tratamento de erros

O cliente HTTP possui timeout de dez segundos e apresenta mensagens para:

- indisponibilidade do gateway ou backend;
- demora excessiva da API;
- credenciais inválidas;
- dados incompletos;
- conflitos de horário;
- respostas HTTP inesperadas.

## Validação

O fluxo foi validado com Playwright utilizando a interface da extensão e o gateway real. Os cenários cobriram:

- login;
- carregamento de barbeiros, clientes e serviços;
- criação consecutiva de vários agendamentos;
- respostas HTTP `201`;
- consulta posterior dos registros;
- limpeza do formulário;
- exibição do modal de sucesso;
- ausência de erros no console.

Também existem verificações de sintaxe dos módulos JavaScript e teste de integração para `GET /usuarios/barbeiros` no projeto Spring Boot.

## Solução de problemas

### Não foi possível conectar à API

Confirme os serviços:

```powershell
Invoke-RestMethod http://localhost:3000/health/live
Invoke-RestMethod http://localhost:3000/health/ready
Invoke-RestMethod http://localhost:8080/servicos
```

### A lista de barbeiros está vazia

Confirme que existem usuários com o tipo `BARBEIRO` e teste:

```powershell
Invoke-RestMethod http://localhost:3000/usuarios/barbeiros
```

### Alterações não aparecem

Abra `chrome://extensions` e clique em **Recarregar** no cartão da extensão. Feche e abra novamente o painel lateral.

### O login expira rapidamente

O tempo de cinco minutos é intencional. Ele está definido por `SESSION_DURATION_MS` em `panel.js`.
