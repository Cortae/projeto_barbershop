export const filterClients = (clients, query) => {
  const q = query.trim().toLowerCase()
  if (!q) return clients
  const digits = q.replace(/\D/g, '')
  return clients.filter((client) =>
    client.name.toLowerCase().includes(q) ||
    (digits && client.phone.replace(/\D/g, '').includes(digits)),
  )
}
