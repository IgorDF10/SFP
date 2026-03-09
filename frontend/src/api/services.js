import api from './axios';

// Auth Services
export const authService = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (name, email, password) => api.post('/auth/register', { name, email, password }),
};

// Category Services
export const categoryService = {
  getAll: (userId) => api.get(`/categories?userId=${userId}`),
  getActive: (userId) => api.get(`/categories/active?userId=${userId}`),
  getByType: (userId, type) => api.get(`/categories/type/${type}?userId=${userId}`),
  create: (userId, data) => api.post(`/categories?userId=${userId}`, data),
  update: (userId, id, data) => api.put(`/categories/${id}?userId=${userId}`, data),
  delete: (userId, id) => api.delete(`/categories/${id}?userId=${userId}`),
};

// Transaction Services
export const transactionService = {
  getAll: (userId) => api.get(`/transactions?userId=${userId}`),
  getById: (userId, id) => api.get(`/transactions/${id}?userId=${userId}`),
  getByType: (userId, type) => api.get(`/transactions/type/${type}?userId=${userId}`),
  getByPeriod: (userId, startDate, endDate) =>
    api.get(`/transactions/period?userId=${userId}&startDate=${startDate}&endDate=${endDate}`),
  create: (userId, data) => api.post(`/transactions?userId=${userId}`, data),
  update: (userId, id, data) => api.put(`/transactions/${id}?userId=${userId}`, data),
  delete: (userId, id) => api.delete(`/transactions/${id}?userId=${userId}`),
  getTotalIncome: (userId) => api.get(`/transactions/totals/income?userId=${userId}`),
  getTotalExpense: (userId) => api.get(`/transactions/totals/expense?userId=${userId}`),
  getBalance: (userId) => api.get(`/transactions/totals/balance?userId=${userId}`),
};

// Dashboard Services
export const dashboardService = {
  getSummary: (userId) => api.get(`/dashboard?userId=${userId}`),
  getSummaryByPeriod: (userId, startDate, endDate) =>
    api.get(`/dashboard/period?userId=${userId}&startDate=${startDate}&endDate=${endDate}`),
};