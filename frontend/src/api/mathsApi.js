import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

// â”€â”€ Problems â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const solveText = (question, grade = '', topic = '') =>
  api.post('/problems/solve', { question, grade, topic }).then(r => r.data)

export const getProblems = (params = {}) =>
  api.get('/problems', { params }).then(r => r.data)

export const getProblemById = id =>
  api.get(`/problems/${id}`).then(r => r.data)

export const searchProblems = q =>
  api.get('/problems/search', { params: { q } }).then(r => r.data)

export const getSimilarProblems = q =>
  api.get('/problems/similar', { params: { q } }).then(r => r.data)

export const deleteProblem = id =>
  api.delete(`/problems/${id}`).then(r => r.data)

export const solveImage = formData =>
  api.post('/problems/solve/image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }).then(r => r.data)

// â”€â”€ Users â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const getUsers = () =>
  api.get('/users').then(r => r.data)

export const createUser = user =>
  api.post('/users', user).then(r => r.data)

export const updateUser = (id, user) =>
  api.put(`/users/${id}`, user).then(r => r.data)

// â”€â”€ Curriculum â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export const getCurriculum = grade =>
  api.get('/curriculum', { params: grade ? { grade } : {} }).then(r => r.data)

export const getCurriculumTopic = topic =>
  api.get(`/curriculum/${topic}`).then(r => r.data)

export const searchCurriculum = q =>
  api.get('/curriculum/search', { params: { q } }).then(r => r.data)

export default api
