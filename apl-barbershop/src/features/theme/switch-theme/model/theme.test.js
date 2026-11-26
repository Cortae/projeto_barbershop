import test from 'node:test'
import assert from 'node:assert/strict'
import { isTheme, resolveTheme } from './theme.js'

test('reconhece somente os temas suportados', () => {
  assert.equal(isTheme('light'), true)
  assert.equal(isTheme('dark'), true)
  assert.equal(isTheme('system'), false)
})

test('prioriza o tema salvo', () => {
  assert.equal(resolveTheme('light', true), 'light')
  assert.equal(resolveTheme('dark', false), 'dark')
})

test('usa a preferência do sistema sem tema salvo', () => {
  assert.equal(resolveTheme(null, true), 'dark')
  assert.equal(resolveTheme(null, false), 'light')
})
