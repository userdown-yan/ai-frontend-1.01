import request from './request'

export const getModels = () => request.get('/product/models')
export const getTemplates = () => request.get('/product/templates')
export const getDashboardData = () => request.get('/product/dashboard')
export const getTaskData = () => request.get('/product/tasks')
export const getGrowthData = () => request.get('/product/growth')