import { createApi } from './api.js'

const API_BASE_URL = 'http://localhost:3000'
const SESSION_DURATION_MS = 5 * 60 * 1000
const THEME_STORAGE_KEY = 'jr-theme'
const state = { clients: [], services: [], barbers: [], selectedClient: null, authSession: null }
const byId = (id) => document.getElementById(id)
const elements = {
  warning: byId('connection-warning'), form: byId('appointment-form'), barber: byId('barber'),
  clientSearch: byId('client-search'), clientId: byId('client-id'), selectedClient: byId('selected-client'),
  clientResults: byId('client-results'), services: byId('services'), date: byId('date'), time: byId('time'),
  summary: byId('summary'), summaryDuration: byId('summary-duration'), summaryPrice: byId('summary-price'),
  feedback: byId('form-feedback'), submit: byId('submit'),
  loginPanel: byId('login-panel'), loginForm: byId('login-form'), loginEmail: byId('login-email'),
  loginPassword: byId('login-password'), loginFeedback: byId('login-feedback'), loginSubmit: byId('login-submit'),
  appointmentContent: byId('appointment-content'), sessionUser: byId('session-user'),
  sessionTimer: byId('session-timer'), logout: byId('logout'), themeToggle: byId('theme-toggle'),
  successDialog: byId('success-dialog'), successMessage: byId('success-message'),
}

const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const digits = (value = '') => String(value).replace(/\D/g, '')
const formatPhone = (value) => {
  const phone = digits(value)
  return phone.length === 11 ? `(${phone.slice(0, 2)}) ${phone.slice(2, 7)}-${phone.slice(7)}` : value || 'Sem telefone'
}

function setFeedback(element, message = '', type = '') {
  element.textContent = message
  element.className = `feedback${type ? ` ${type}` : ''}`
}

function showSuccessDialog(clientName, start) {
  const date = new Date(start)
  const formatted = new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit', month: 'long', hour: '2-digit', minute: '2-digit',
  }).format(date)
  elements.successMessage.textContent = `${clientName} foi agendado para ${formatted}.`
  elements.successDialog.showModal()
}

function applyTheme(theme) {
  const dark = theme === 'dark'
  document.documentElement.dataset.theme = theme
  document.documentElement.style.colorScheme = theme
  const label = dark ? 'Ativar tema claro' : 'Ativar tema escuro'
  elements.themeToggle.setAttribute('aria-label', label)
  elements.themeToggle.title = label
}

async function initializeTheme() {
  const stored = await storageGet(THEME_STORAGE_KEY)
  const theme = ['light', 'dark'].includes(stored[THEME_STORAGE_KEY])
    ? stored[THEME_STORAGE_KEY]
    : window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
  applyTheme(theme)
}

async function toggleTheme() {
  const theme = document.documentElement.dataset.theme === 'dark' ? 'light' : 'dark'
  applyTheme(theme)
  await chrome.storage.local.set({ [THEME_STORAGE_KEY]: theme })
}

function setMinimumDate() {
  const now = new Date()
  const localToday = new Date(now.getTime() - now.getTimezoneOffset() * 60_000).toISOString().slice(0, 10)
  elements.date.min = localToday
}

function serviceValues(service) {
  return {
    id: Number(service.id),
    name: service.titulo,
    price: Number(service.valorAtual ?? 0),
    duration: Number(service.duracaoAtual ?? 0),
  }
}

function renderServices() {
  elements.services.replaceChildren()
  if (!state.services.length) {
    const empty = document.createElement('p')
    empty.className = 'muted'
    empty.textContent = 'Nenhum serviço ativo encontrado.'
    elements.services.append(empty)
    return
  }
  state.services.forEach((rawService) => {
    const service = serviceValues(rawService)
    const label = document.createElement('label')
    label.className = 'service-option'
    label.innerHTML = `<input type="checkbox" name="service" value="${service.id}"><span class="service-name"></span><span class="service-duration"></span><span class="service-price"></span>`
    label.querySelector('.service-name').textContent = service.name
    label.querySelector('.service-duration').textContent = `${service.duration} min`
    label.querySelector('.service-price').textContent = currency.format(service.price)
    label.querySelector('input').addEventListener('change', updateSummary)
    elements.services.append(label)
  })
}

function selectedServiceIds() {
  return [...document.querySelectorAll('input[name="service"]:checked')].map((input) => Number(input.value))
}

function updateSummary() {
  const selected = new Set(selectedServiceIds())
  const totals = state.services.filter((service) => selected.has(Number(service.id))).map(serviceValues)
  elements.summary.hidden = totals.length === 0
  elements.summaryDuration.textContent = `${totals.reduce((sum, item) => sum + item.duration, 0)} minutos`
  elements.summaryPrice.textContent = currency.format(totals.reduce((sum, item) => sum + item.price, 0))
  saveDraft()
}

function matchingClients(query) {
  const normalized = query.trim().toLocaleLowerCase('pt-BR')
  if (!normalized) return state.clients.slice(0, 8)
  const queryDigits = digits(query)
  return state.clients.filter((client) =>
    client.nome.toLocaleLowerCase('pt-BR').includes(normalized) || (queryDigits && digits(client.celular).includes(queryDigits)),
  ).slice(0, 8)
}

function renderClientResults() {
  if (state.selectedClient) return
  const clients = matchingClients(elements.clientSearch.value)
  elements.clientResults.replaceChildren()
  clients.forEach((client) => {
    const item = document.createElement('li')
    const button = document.createElement('button')
    button.type = 'button'
    button.setAttribute('role', 'option')
    button.innerHTML = '<span class="result-name"></span><span class="result-phone"></span>'
    button.querySelector('.result-name').textContent = client.nome
    button.querySelector('.result-phone').textContent = formatPhone(client.celular)
    button.addEventListener('click', () => selectClient(client))
    item.append(button)
    elements.clientResults.append(item)
  })
  if (!clients.length) {
    const item = document.createElement('li')
    item.className = 'muted'
    item.textContent = 'Nenhum cliente encontrado.'
    elements.clientResults.append(item)
  }
  elements.clientResults.hidden = false
}

function selectClient(client) {
  state.selectedClient = client
  elements.clientId.value = client.id
  elements.clientSearch.value = ''
  elements.clientSearch.hidden = true
  elements.clientResults.hidden = true
  elements.selectedClient.replaceChildren()
  const text = document.createElement('span')
  text.textContent = `${client.nome} · ${formatPhone(client.celular)}`
  const remove = document.createElement('button')
  remove.type = 'button'
  remove.textContent = 'Trocar'
  remove.addEventListener('click', clearClient)
  elements.selectedClient.append(text, remove)
  elements.selectedClient.hidden = false
  byId('client-error').textContent = ''
  saveDraft()
}

function clearClient() {
  state.selectedClient = null
  elements.clientId.value = ''
  elements.selectedClient.hidden = true
  elements.clientSearch.hidden = false
  elements.clientSearch.focus()
  renderClientResults()
  saveDraft()
}

async function storageGet(keys) {
  return chrome.storage.local.get(keys)
}

async function sessionGet() {
  const { authSession = null } = await chrome.storage.session.get('authSession')
  return authSession
}

function remainingSessionTime() {
  return Math.max(0, Number(state.authSession?.expiresAt ?? 0) - Date.now())
}

function updateSessionTimer() {
  if (!state.authSession) return false
  const remaining = remainingSessionTime()
  if (remaining <= 0) {
    expireSession('Sua sessão de cinco minutos expirou. Entre novamente.')
    return false
  }
  const totalSeconds = Math.ceil(remaining / 1000)
  const minutes = Math.floor(totalSeconds / 60)
  const seconds = String(totalSeconds % 60).padStart(2, '0')
  elements.sessionTimer.textContent = `Novo login em ${minutes}:${seconds}`
  return true
}

function showAuthenticatedSession() {
  elements.loginPanel.hidden = true
  elements.appointmentContent.hidden = false
  elements.sessionUser.textContent = state.authSession.user.nome
  setFeedback(elements.loginFeedback)
  updateSessionTimer()
}

async function expireSession(message = '') {
  state.authSession = null
  await chrome.storage.session.remove('authSession')
  elements.appointmentContent.hidden = true
  elements.loginPanel.hidden = false
  elements.loginPassword.value = ''
  setFeedback(elements.loginFeedback, message, message ? 'error' : '')
  elements.loginEmail.focus()
}

async function authenticate(event) {
  event.preventDefault()
  const email = elements.loginEmail.value.trim()
  const password = elements.loginPassword.value
  setFeedback(elements.loginFeedback)
  if (!email || !password) {
    setFeedback(elements.loginFeedback, 'Informe o e-mail e a senha.', 'error')
    return
  }
  elements.loginSubmit.disabled = true
  elements.loginSubmit.textContent = 'Entrando…'
  try {
    const user = await createApi(API_BASE_URL).authenticate(email, password)
    state.authSession = { user, expiresAt: Date.now() + SESSION_DURATION_MS }
    await Promise.all([
      chrome.storage.session.set({ authSession: state.authSession }),
      chrome.storage.local.set({ lastLoginEmail: email }),
    ])
    elements.loginPassword.value = ''
    showAuthenticatedSession()
    if (await loadData()) await restoreDraft()
  } catch (error) {
    setFeedback(elements.loginFeedback, error.message, 'error')
    elements.loginPassword.select()
  } finally {
    elements.loginSubmit.disabled = false
    elements.loginSubmit.textContent = 'Entrar'
  }
}

function saveDraft() {
  chrome.storage.local.set({
    appointmentDraft: {
      barberId: Number(elements.barber.value) || null,
      clientId: state.selectedClient?.id ?? null,
      serviceIds: selectedServiceIds(),
      date: elements.date.value,
      time: elements.time.value,
    },
  })
}

async function restoreDraft() {
  const { appointmentDraft = {} } = await storageGet('appointmentDraft')
  if (state.barbers.some((barber) => Number(barber.id) === Number(appointmentDraft.barberId))) {
    elements.barber.value = String(appointmentDraft.barberId)
  }
  const client = state.clients.find((item) => Number(item.id) === Number(appointmentDraft.clientId))
  if (client) selectClient(client)
  const ids = new Set((appointmentDraft.serviceIds ?? []).map(Number))
  document.querySelectorAll('input[name="service"]').forEach((input) => { input.checked = ids.has(Number(input.value)) })
  elements.date.value = appointmentDraft.date ?? ''
  elements.time.value = appointmentDraft.time ?? ''
  updateSummary()
}

async function loadData({ showLoading = true } = {}) {
  if (showLoading) {
    elements.submit.disabled = true
    elements.warning.hidden = true
  }
  try {
    const api = createApi(API_BASE_URL)
    const [clients, services, barbers] = await Promise.all([api.clients(), api.services(), api.barbers()])
    state.clients = clients
    state.services = services.filter((service) => service.ativo)
    state.barbers = barbers
    elements.barber.replaceChildren(new Option('Selecione o barbeiro', ''))
    state.barbers.forEach((barber) => elements.barber.add(new Option(barber.nome, String(barber.id))))
    renderServices()
    elements.submit.disabled = false
    elements.warning.hidden = true
    return true
  } catch (error) {
    elements.warning.textContent = error.message
    elements.warning.hidden = false
    elements.services.innerHTML = '<p class="muted">Serviços indisponíveis.</p>'
    elements.submit.disabled = true
    return false
  }
}

function clearErrors() {
  ;['barber-error', 'client-error', 'services-error', 'date-error', 'time-error'].forEach((id) => { byId(id).textContent = '' })
  setFeedback(elements.feedback)
}

function validate() {
  clearErrors()
  let valid = true
  if (!elements.barber.value) { byId('barber-error').textContent = 'Selecione o barbeiro.'; valid = false }
  if (!state.selectedClient) { byId('client-error').textContent = 'Selecione um cliente.'; valid = false }
  if (!selectedServiceIds().length) { byId('services-error').textContent = 'Selecione ao menos um serviço.'; valid = false }
  if (!elements.date.value) { byId('date-error').textContent = 'Informe a data.'; valid = false }
  if (!elements.time.value) { byId('time-error').textContent = 'Informe o horário.'; valid = false }
  if (elements.date.value && elements.time.value && new Date(`${elements.date.value}T${elements.time.value}`) <= new Date()) {
    byId('time-error').textContent = 'Escolha um horário futuro.'
    valid = false
  }
  return valid
}

async function submitAppointment(event) {
  event.preventDefault()
  if (!updateSessionTimer()) return
  if (!validate()) return
  elements.submit.disabled = true
  elements.submit.textContent = 'Salvando…'
  try {
    const appointment = await createApi(API_BASE_URL).createAppointment({
      barbeiro: Number(elements.barber.value),
      cliente: Number(state.selectedClient.id),
      servicos: selectedServiceIds(),
      inicio: `${elements.date.value}T${elements.time.value}:00`,
    })
    const clientName = appointment?.cliente?.nome ?? state.selectedClient.nome
    setFeedback(elements.feedback, `Agendamento de ${clientName} salvo com sucesso.`, 'success')
    showSuccessDialog(clientName, appointment?.horarioInicio ?? `${elements.date.value}T${elements.time.value}:00`)
    clearClient()
    elements.barber.value = ''
    document.querySelectorAll('input[name="service"]').forEach((input) => { input.checked = false })
    elements.date.value = ''
    elements.time.value = ''
    updateSummary()
    await chrome.storage.local.remove('appointmentDraft')
  } catch (error) {
    setFeedback(elements.feedback, error.message, 'error')
  } finally {
    elements.submit.disabled = false
    elements.submit.textContent = 'Salvar agendamento'
  }
}

async function init() {
  await initializeTheme()
  setMinimumDate()
  const stored = await storageGet('lastLoginEmail')
  elements.loginEmail.value = stored.lastLoginEmail ?? ''
  state.authSession = await sessionGet()
  if (remainingSessionTime() > 0) {
    showAuthenticatedSession()
    if (await loadData()) await restoreDraft()
  } else {
    await expireSession(state.authSession ? 'Sua sessão expirou. Entre novamente.' : '')
  }
}

elements.clientSearch.addEventListener('input', renderClientResults)
elements.clientSearch.addEventListener('focus', renderClientResults)
document.addEventListener('click', (event) => {
  if (!elements.clientResults.contains(event.target) && event.target !== elements.clientSearch) elements.clientResults.hidden = true
})
elements.date.addEventListener('change', saveDraft)
elements.time.addEventListener('change', saveDraft)
elements.barber.addEventListener('change', saveDraft)
elements.form.addEventListener('submit', submitAppointment)
elements.loginForm.addEventListener('submit', authenticate)
elements.logout.addEventListener('click', () => expireSession('Você saiu da extensão.'))
elements.themeToggle.addEventListener('click', toggleTheme)

setInterval(updateSessionTimer, 1000)

init()
