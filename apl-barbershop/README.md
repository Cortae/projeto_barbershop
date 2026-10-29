# JR Barbershop

Frontend React 19 + Vite para agenda, clientes, serviços e financeiro.

## Requisitos e instalação

- Node.js 22+
- npm 11+

```bash
npm install
cp .env.example .env.local
npm run dev
```

O frontend abre em `http://localhost:5173`.

## Modos de integração

A escolha fica centralizada em `src/shared/api/httpClient.js`:

```dotenv
VITE_USE_TRACE_API=false
VITE_TRACE_API_URL=/trace-api
VITE_BACKEND_API_URL=/backend-api
VITE_REPORT_API_URL=/trace-api

TRACE_API_PROXY_TARGET=http://localhost:3000
BACKEND_API_PROXY_TARGET=http://localhost:8080
```

`VITE_REPORT_API_URL` aponta para o Express, pois a geracao do relatorio financeiro mensal usa Puppeteer no servidor. O botao **Gerar relatorio mensal** baixa o PDF do mes selecionado.

`false` usa React -> Spring. `true` usa React -> Express -> Spring. O Vite remove os prefixos locais antes de encaminhar; `/clientes` chega com esse mesmo path aos dois destinos. Reinicie o Vite após alterar variáveis `VITE_*`.

Despesas e expediente usam `/despesas` e `/usuarios/{id}/expediente`. Agendamentos, clientes, serviços, formas de pagamento e bloqueios também usam o backend. O login usa `POST /autenticacao/login` com e-mail e senha; a resposta identifica a sessão local, mas o backend ainda não emite token.

## Scripts

```bash
npm test
npm run lint
npm run build
```

## Estrutura

O código segue Feature-Sliced Design:

- `src/app`: inicialização, providers, rotas e estilos;
- `src/pages`: páginas;
- `src/widgets`: blocos de interface;
- `src/features`: ações do usuário;
- `src/entities`: contratos, mapeadores e regras das entidades;
- `src/shared`: UI e infraestrutura compartilhada.
