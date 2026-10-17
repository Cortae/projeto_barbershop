# JR Barbershop

Aplicação web para gestão de uma barbearia, com agenda, clientes, serviços, despesas e visão financeira.

## Tecnologias

- React 19 com JavaScript e JSX
- Vite
- Tailwind CSS 4
- React Router
- TanStack Query
- npm

## Requisitos

- Node.js 22
- npm 11

As versões de ferramentas podem ser configuradas automaticamente com o [mise](https://mise.jdx.dev/).

## Instalação

```bash
npm install
```

## Desenvolvimento

Inicie primeiro o backend em `http://localhost:8080`. Depois, no frontend:

```bash
npm run dev
```

A aplicação utiliza `http://localhost:5173` por padrão. A variável de ambiente `PORT` pode sobrescrever essa porta. Em desenvolvimento, `/api` é encaminhado pelo Vite para `http://localhost:8080`; `API_PROXY_TARGET` permite alterar esse destino.

Copie `.env.example` para `.env.local` quando precisar personalizar o ambiente. Em homologação ou produção, defina `VITE_API_URL` com a URL pública do backend antes do build.

## Scripts

- `npm run dev`: inicia o servidor de desenvolvimento.
- `npm run build`: gera o build de produção.
- `npm run preview`: abre localmente o build de produção.
- `npm run lint`: verifica problemas no JavaScript e JSX sem alterar arquivos.
- `npm test`: executa os testes automatizados do frontend.
- `npm run lint:fix`: corrige automaticamente os problemas de lint que possuem solução segura.
- `npm run format`: formata o código-fonte com o oxfmt.
- `npm run format:check`: verifica a formatação do código-fonte sem alterar arquivos.

## Qualidade de código

O ESLint analisa os arquivos JavaScript e JSX em `src` e verifica:

- regras recomendadas de JavaScript;
- uso correto dos Hooks do React;
- compatibilidade dos componentes com o Fast Refresh do Vite;
- acessibilidade do JSX, incluindo elementos interativos, rótulos e atributos ARIA;
- variáveis e parâmetros não utilizados.

O lint procura possíveis erros e práticas inseguras. O oxfmt cuida apenas da apresentação do código, como indentação, aspas e quebras de linha. Antes de enviar alterações, execute:

```bash
npm run lint
npm run format:check
npm run build
```

## Estrutura

O código segue uma organização baseada em Feature-Sliced Design (FSD):

- `src/app`: inicialização, providers, rotas e estilos globais.
- `src/pages`: páginas associadas às rotas.
- `src/widgets`: blocos maiores de interface.
- `src/features`: ações e fluxos do usuário.
- `src/entities`: entidades e regras de negócio.
- `src/shared`: componentes, utilitários e infraestrutura compartilhada.

Consulte `docs/especificacao-arquitetura-fsd.md` para mais detalhes.

## Estado atual

O fluxo de agendamentos, clientes, serviços, formas de pagamento e bloqueios usa o backend Spring Boot. O login ainda é uma simulação local associada ao barbeiro de ID `2`.

O expediente e as despesas ainda utilizam dados locais. A API de expediente foi deliberadamente adiada; sua edição não é exibida enquanto não houver persistência no backend.
