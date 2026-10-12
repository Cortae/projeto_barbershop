export const ROUTES = {
  login: '/login',
  home: '/',
  schedule: '/agenda',
  appointmentNew: '/agendamentos/novo',
  appointment: (id) => `/agendamentos/${id}`,
  appointmentEdit: (id) => `/agendamentos/${id}/editar`,
  clients: '/clientes',
  clientNew: '/clientes/novo',
  client: (id) => `/clientes/${id}`,
  services: '/servicos',
  finance: '/financeiro',
  expenses: '/financeiro/despesas',
}
