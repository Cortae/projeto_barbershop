export const filterClients = (clients, query) => {
  const q = query.trim().toLowerCase()
  if (!q) return clients
  const digits = q.replace(/\D/g, '')
  return clients.filter((client) =>
    client.nome.toLowerCase().includes(q) ||
    (digits && client.telefone.replace(/\D/g, '').includes(digits)),
  )
}
