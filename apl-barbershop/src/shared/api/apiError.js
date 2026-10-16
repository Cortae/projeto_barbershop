export class ApiError extends Error {
  constructor(code, message, details = []) {
    super(message)
    this.name = 'ApiError'
    this.code = code
    this.details = details
  }
}
