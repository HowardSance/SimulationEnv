/**
 * API基础配置
 * axios实例配置、拦截器等
 */

import axios, { AxiosInstance, AxiosError, AxiosResponse } from 'axios';
import { message } from 'antd';
import { ApiResponse } from '../types/api.types';

// API基础URL
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

// 创建axios实例
const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加token等认证信息
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 添加时间戳防止缓存
    if (config.method === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now(),
      };
    }
    
    return config;
  },
  (error: AxiosError) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<any>>) => {
    // 处理成功响应
    const { data } = response;
    
    // 如果后端返回标准格式
    if (data && typeof data === 'object' && 'code' in data) {
      if (data.code === 200) {
        return data.data;
      } else {
        message.error(data.message || '请求失败');
        return Promise.reject(new Error(data.message));
      }
    }
    
    // 如果后端直接返回数据（非标准格式）
    return response.data;
  },
  (error: AxiosError<ApiResponse<any>>) => {
    // 处理错误响应
    if (error.response) {
      const { status, data } = error.response;
      
      switch (status) {
        case 400:
          message.error(data?.message || '请求参数错误');
          break;
        case 401:
          message.error('未授权，请重新登录');
          // 可以在这里处理跳转到登录页
          break;
        case 403:
          message.error('没有权限访问该资源');
          break;
        case 404:
          message.error('请求的资源不存在');
          break;
        case 500:
          message.error('服务器内部错误');
          break;
        default:
          message.error(data?.message || `请求失败: ${status}`);
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接');
    } else {
      message.error('请求配置错误');
    }
    
    return Promise.reject(error);
  }
);

// 导出api实例
export default api;

// 导出常用的HTTP方法封装
export const http = {
  get: <T = any>(url: string, params?: any) => 
    api.get<T, T>(url, { params }),
    
  post: <T = any>(url: string, data?: any) => 
    api.post<T, T>(url, data),
    
  put: <T = any>(url: string, data?: any) => 
    api.put<T, T>(url, data),
    
  delete: <T = any>(url: string, params?: any) => 
    api.delete<T, T>(url, { params }),
    
  patch: <T = any>(url: string, data?: any) => 
    api.patch<T, T>(url, data),
}; 