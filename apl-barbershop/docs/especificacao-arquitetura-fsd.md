# JR Barbershop — Especificação de Arquitetura Front-end (React + FSD)

> Complementa `JR-Barbershop-especificacao-design.md`. Define a organização de pastas, as camadas, as regras de dependência e o mapeamento das telas TL01–TL10 para a metodologia **Feature-Sliced Design (FSD)**.

## 1. Stack

| Item | Definição |
| --- | --- |
| Linguagem | React 19 + JavaScript (`.jsx` / `.js`) |
| Build | Vite |
| Estilo | Tailwind CSS v4 com tokens da paleta em `@theme` (RNF03) |
| Roteamento | React Router (rotas protegidas — RNF06) |
| Dados remotos | TanStack Query (cache, loading, retry — RNF04, RNF07) |
| Formulários | Estado local + validação manual por formulário (RNF05) |
| HTTP | Cliente `fetch`/axios único em `shared/api`, back-end Java Spring Boot |
| Autenticação | Google Auth (OAuth/GIS) → token de sessão do back-end |
| Formatação | `Intl` pt-BR: moeda `R$ 1.234,50`, data `dd/mm/aaaa`, hora 24 h (RNF08) |
| Alias | `@/` → `src/` |

## 2. Camadas FSD

Ordem (de cima para baixo). **Uma camada só importa camadas abaixo dela.**

```
app → pages → widgets → features → entities → shared
```

| Camada | Responsabilidade | Tem slices? |
| --- | --- | --- |
| `app` | Inicialização: providers, roteador, estilos globais, guarda de sessão | Não |
| `pages` | Uma tela (TL01–TL10) por slice; compõe widgets e features | Sim |
| `widgets` | Blocos grandes e autônomos reaproveitáveis (layout, grade da agenda, KPIs) | Sim |
| `features` | Ações do usuário com valor de negócio (agendar, cancelar, marcar como pago) | Sim |
| `entities` | Modelos de negócio: tipos, API, consultas, UI de exibição (card, selo) | Sim |
| `shared` | Código sem regra de negócio: UI kit, API base, libs, config, tokens | Não (segmentos) |

### Segmentos padrão dentro de cada slice

| Segmento | Conteúdo |
| --- | --- |
| `ui/` | Componentes React |
| `model/` | Tipos, schemas Zod, hooks de estado, stores |
| `api/` | Requisições e hooks do TanStack Query |
| `lib/` | Funções auxiliares do slice |
| `config/` | Constantes do slice |
| `index.ts` | **API pública** — único ponto de importação externo |

## 3. Árvore de pastas

```
src/
├── app/
│   ├── providers/
│   │   ├── QueryProvider.tsx
│   │   ├── AuthProvider.tsx
│   │   ├── ToastProvider.tsx
│   │   └── index.tsx              # compõe todos os providers
│   ├── router/
│   │   ├── routes.tsx             # mapa de rotas (seção 5)
│   │   ├── ProtectedRoute.tsx     # sem sessão → /login (RNF06)
│   │   └── index.tsx
│   ├── styles/
│   │   └── index.css              # @import tailwind + @theme com tokens
│   └── App.tsx
│
├── pages/
│   ├── login/                     # TL01
│   ├── home/                      # TL02
│   ├── schedule/                  # TL03
│   ├── appointment-form/          # TL04 (novo / editar)
│   ├── appointment-details/       # TL05
│   ├── clients/                   # TL06
│   ├── client/                    # TL07 (novo / editar + histórico)
│   ├── services/                  # TL08
│   ├── finance/                   # TL09
│   ├── expenses/                  # TL10
│   └── not-found/
│       (cada uma: ui/<Nome>Page.tsx + index.ts)
│
├── widgets/
│   ├── app-layout/                # header + bottom nav (mobile) / sidebar (desktop)
│   ├── app-header/                # título, voltar, menu do avatar com "Sair"
│   ├── bottom-nav/                # Início, Agenda, Clientes, Financeiro, Serviços
│   ├── sidebar/
│   ├── next-appointment/          # card destaque "Próximo corte" (TL02)
│   ├── today-agenda/              # lista de hoje (TL02)
│   ├── day-summary/               # recebido / a receber do dia (TL02)
│   ├── schedule-grid/             # grade Dia/Semana: livre, ocupado, bloqueado (TL03)
│   ├── client-history/            # histórico de atendimentos (TL07)
│   ├── finance-kpis/              # lucro bruto, despesas, lucro líquido, a receber (TL09)
│   ├── revenue-by-service-chart/  # gráfico de barras (TL09)
│   ├── pending-payments-list/     # pendências + "Marcar como pago" (TL09)
│   └── expenses-list/             # (TL10)
│
├── features/
│   ├── auth/
│   │   ├── sign-in-google/        # botão "Entrar com Google"
│   │   └── sign-out/
│   ├── appointment/
│   │   ├── create-appointment/    # formulário TL04 + conflito de horário
│   │   ├── reschedule-appointment/
│   │   ├── cancel-appointment/    # modal de confirmação
│   │   ├── complete-appointment/  # concluir + forma de pagamento + Pago/Pendente
│   │   └── mark-as-paid/
│   ├── schedule/
│   │   ├── block-time-slot/       # bottom sheet "Bloquear horário"
│   │   ├── configure-working-hours/ # bottom sheet "Expediente"
│   │   └── switch-period-view/    # seletor Dia/Semana + anterior/próximo
│   ├── client/
│   │   ├── upsert-client/         # criar/editar cliente
│   │   └── search-clients/
│   ├── service/
│   │   └── upsert-service/        # bottom sheet de serviço
│   ├── expense/
│   │   └── create-expense/        # bottom sheet "Nova despesa"
│   └── finance/
│       └── select-finance-period/
│
├── entities/
│   ├── barber/                    # usuário autenticado (sessão)
│   ├── appointment/
│   │   ├── model/types.ts         # Appointment, AppointmentStatus, PaymentStatus
│   │   ├── api/                   # appointmentApi, useAppointments, useAppointment
│   │   ├── ui/                    # AppointmentCard, AppointmentStatusBadge
│   │   └── index.ts
│   ├── client/                    # Client, ClientCard, PendingBadge
│   ├── service/                   # Service, ServiceCard
│   ├── time-slot/                 # TimeSlot (livre | ocupado | bloqueado), WorkingHours
│   ├── payment/                   # PaymentMethod, PaymentBadge (Pago/Pendente)
│   ├── expense/                   # Expense, ExpenseCard
│   └── finance/                   # FinanceSummary, RevenueByService
│
└── shared/
    ├── api/
    │   ├── httpClient.ts          # baseURL, token, tratamento de erro
    │   ├── errors.ts              # mapeia 401 → logout, 409 → conflito, rede → RNF07
    │   └── queryClient.ts
    ├── config/
    │   ├── env.ts                 # VITE_API_URL, VITE_GOOGLE_CLIENT_ID
    │   └── routes.ts              # constantes de caminho
    ├── lib/
    │   ├── format/                # formatCurrency, formatDate, formatTime (pt-BR)
    │   ├── date/                  # helpers de agenda (dias da semana, intervalos)
    │   └── a11y/                  # useFocusTrap, useAnnouncer (aria-live)
    ├── ui/                        # UI kit — só tokens, sem regra de negócio
    │   ├── button/                # primário, secundário, perigo, link, flutuante, ícone
    │   ├── input/  textarea/  select/  segmented-control/  chip/
    │   ├── card/  badge/  modal/  bottom-sheet/  toast/
    │   ├── skeleton/  empty-state/  error-state/  # "Tentar novamente"
    │   ├── tabs/  period-selector/  avatar/  spinner/
    │   └── index.ts
    └── assets/
        └── icons/
```

## 4. Regras de dependência

1. Importar **somente pela API pública** do slice: `import { AppointmentCard } from '@/entities/appointment'` — nunca `@/entities/appointment/ui/AppointmentCard`.
2. Slices da **mesma camada não se importam** entre si (ex.: `features/cancel-appointment` não importa `features/complete-appointment`). A composição acontece em `widgets` ou `pages`.
3. Relações entre entidades usam a notação cruzada `@x` quando inevitável: `entities/appointment/@x/client.ts`.
4. `shared/ui` recebe só props; não chama API nem conhece entidades.
5. Cores, espaços, raios e tipografia vêm **apenas** de tokens do `@theme` (RNF03). Nenhum HEX dentro de componentes.
6. Verificação automatizada: `eslint-plugin-boundaries` ou `@feature-sliced/steiger` no CI.

## 5. Rotas

| Rota | Página (slice) | Tela | Protegida |
| --- | --- | --- | --- |
| `/login` | `pages/login` | TL01 | Não |
| `/` | `pages/home` | TL02 | Sim |
| `/agenda?vista=dia\|semana&data=` | `pages/schedule` | TL03 | Sim |
| `/agendamentos/novo?data=&hora=&clienteId=` | `pages/appointment-form` | TL04 | Sim |
| `/agendamentos/:id/editar` | `pages/appointment-form` | TL04 (Editar) | Sim |
| `/agendamentos/:id` | `pages/appointment-details` | TL05 | Sim |
| `/clientes` | `pages/clients` | TL06 | Sim |
| `/clientes/novo?retorno=` | `pages/client` | TL07 | Sim |
| `/clientes/:id` | `pages/client` | TL07 | Sim |
| `/servicos` | `pages/services` | TL08 | Sim |
| `/financeiro?periodo=` | `pages/finance` | TL09 | Sim |
| `/financeiro/despesas` | `pages/expenses` | TL10 | Sim |

Bottom sheets (bloqueio, expediente, serviço, despesa) e modais (cancelar, concluir) **não** têm rota própria: são estado local da página, abertos pela feature correspondente.

## 6. Mapeamento tela → composição

| Tela | Widgets | Features | Entities |
| --- | --- | --- | --- |
| TL01 Login | — | `sign-in-google` | `barber` |
| TL02 Início | `app-layout`, `next-appointment`, `today-agenda`, `day-summary` | `sign-out` | `appointment`, `payment` |
| TL03 Agenda | `app-layout`, `schedule-grid` | `switch-period-view`, `block-time-slot`, `configure-working-hours` | `time-slot`, `appointment` |
| TL04 Agendamento | `app-layout` | `create-appointment`, `reschedule-appointment`, `search-clients` | `client`, `service`, `time-slot` |
| TL05 Detalhe | `app-layout` | `complete-appointment`, `cancel-appointment`, `mark-as-paid` | `appointment`, `payment` |
| TL06 Clientes | `app-layout` | `search-clients` | `client` |
| TL07 Cliente | `app-layout`, `client-history` | `upsert-client` | `client`, `appointment` |
| TL08 Serviços | `app-layout` | `upsert-service` | `service` |
| TL09 Financeiro | `app-layout`, `finance-kpis`, `revenue-by-service-chart`, `pending-payments-list` | `select-finance-period`, `mark-as-paid` | `finance`, `payment` |
| TL10 Despesas | `app-layout`, `expenses-list` | `create-expense` | `expense` |

## 7. Modelos (formato dos objetos — referência em notação TS, código em JS)

```ts
type AppointmentStatus = 'agendado' | 'concluido' | 'cancelado'
type PaymentStatus = 'pago' | 'pendente'
type PaymentMethod = 'dinheiro' | 'pix' | 'debito' | 'credito'

interface Appointment {
  id: string; clientId: string; serviceId: string
  startsAt: string            // ISO 8601
  durationMin: number; priceCents: number
  status: AppointmentStatus
  paymentStatus?: PaymentStatus; paymentMethod?: PaymentMethod
  notes?: string
}
interface Client { id: string; name: string; phone: string; email?: string; birthDate?: string; lastVisitAt?: string; pendingCents: number }
interface Service { id: string; name: string; priceCents: number; durationMin: number }
interface TimeSlot { start: string; end: string; state: 'livre' | 'ocupado' | 'bloqueado'; appointmentId?: string; reason?: string }
interface WorkingHours { days: number[]; opensAt: string; closesAt: string; breakStart?: string; breakEnd?: string }
interface Expense { id: string; description: string; amountCents: number; date: string }
interface FinanceSummary { grossCents: number; expensesCents: number; netCents: number; receivableCents: number; byService: { serviceId: string; name: string; totalCents: number }[] }
```

Valores monetários trafegam em **centavos (inteiro)** e só são formatados em `shared/lib/format`.

## 8. Requisitos não funcionais → onde vivem

| RNF | Implementação |
| --- | --- |
| RNF01 Mobile-first / toque 44 px | `shared/ui` com `min-h-11 min-w-11`; `widgets/app-layout` com nav inferior fixa |
| RNF02 WCAG AA | `shared/ui` (foco visível, `aria-*`), `shared/lib/a11y` |
| RNF03 Identidade | Tokens em `app/styles/index.css` (`@theme`) |
| RNF04 Desempenho | `shared/ui/skeleton` + `isLoading` do TanStack Query; rotas com `React.lazy` por página |
| RNF05 Feedback | `shared/ui/toast` + estado `isPending` nas mutations das features |
| RNF06 Sessão | `app/router/ProtectedRoute`, `entities/barber`, interceptor 401 em `shared/api` |
| RNF07 Conexão | `shared/ui/error-state` com "Tentar novamente" → `refetch()` |
| RNF08 pt-BR | `shared/lib/format` |

## 9. Convenções

- Pastas em `kebab-case`; componentes em `PascalCase.tsx`; hooks `useXxx.ts`.
- Componentes exportados como `default` no arquivo e reexportados nomeados no `index.ts`.
- Chaves do TanStack Query centralizadas por entidade: `appointmentKeys.list(date)`, `appointmentKeys.detail(id)`.
- Mutations invalidam as chaves afetadas (ex.: `mark-as-paid` invalida `appointment`, `client` e `finance`).
- Textos de interface vêm da aba Conteúdo das Páginas; podem ficar em `pages/*/config/copy.ts` para revisão.
