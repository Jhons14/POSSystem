import { addSecurityHeaders, apiRateLimiter, secureStorage } from './security.js';

// Types
export interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  categoryId: number;
  image?: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  image?: string;
}

export interface AuthResponse {
  jwt: string;
}

export interface ErrorResponse {
  status: number;
  error: string;
  message: string;
  fieldErrors?: Record<string, string>;
}

export interface CustomerRegistrationData {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  phone?: string;
  address?: string;
  birthDate?: string;
  gender?: string;
}

export interface CustomerRegistrationResponse {
  id: number;
  message: string;
}

// Configuration
const SERVER_URL = import.meta.env.VITE_SERVER_URL || 'http://localhost:8080/pos/server/api';

// API Endpoints
const endpoints = {
  auth: `${SERVER_URL}/auth/authenticate`,
  products: {
    all: `${SERVER_URL}/products/all`,
    update: (id: number) => `${SERVER_URL}/products/update/${id}`,
  },
  categories: {
    all: `${SERVER_URL}/category/all`,
  },
  customers: {
    register: `${SERVER_URL}/customer/save`,
  },
  files: {
    uploadProductImage: (id: number) => `${SERVER_URL}/files/upload/image/product/${id}`,
  },
} as const;

// Error handling
class ApiError extends Error {
  constructor(
    public status: number,
    public code: string,
    message: string,
    public fieldErrors?: Record<string, string>
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

// Rate limiting check
function checkRateLimit(): void {
  if (!apiRateLimiter.isAllowed()) {
    const timeUntilReset = Math.ceil(apiRateLimiter.getTimeUntilReset() / 1000);
    throw new ApiError(
      429,
      'RATE_LIMITED',
      `Too many requests. Please wait ${timeUntilReset} seconds.`
    );
  }
}

// Generic API request function
async function apiRequest<T>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  checkRateLimit();

  try {
    const response = await fetch(url, addSecurityHeaders(options));

    if (!response.ok) {
      const errorData: ErrorResponse = await response.json().catch(() => ({
        status: response.status,
        error: 'UNKNOWN_ERROR',
        message: 'An unexpected error occurred',
      }));

      throw new ApiError(
        errorData.status || response.status,
        errorData.error || 'API_ERROR',
        errorData.message || `HTTP ${response.status}`,
        errorData.fieldErrors
      );
    }

    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }

    // Network or other errors
    throw new ApiError(
      0,
      'NETWORK_ERROR',
      'Failed to connect to server. Please check your connection.'
    );
  }
}

// Authentication
export async function authenticate(
  username: string,
  password: string
): Promise<AuthResponse> {
  const response = await apiRequest<AuthResponse>(endpoints.auth, {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });

  // Store token securely
  secureStorage.setToken(response.jwt);
  return response;
}

export function logout(): void {
  secureStorage.removeToken();
}

// Products
export async function getAllProducts(): Promise<Product[]> {
  return apiRequest<Product[]>(endpoints.products.all);
}

export async function updateProduct(id: number, product: Partial<Product>): Promise<Product> {
  return apiRequest<Product>(endpoints.products.update(id), {
    method: 'PUT',
    body: JSON.stringify(product),
  });
}

// Categories
export async function getAllCategories(): Promise<Category[]> {
  return apiRequest<Category[]>(endpoints.categories.all);
}

// File uploads
export async function uploadProductImage(
  productId: number,
  file: File
): Promise<{ imageUrl: string }> {
  checkRateLimit();

  const formData = new FormData();
  formData.append('image', file);

  const token = secureStorage.getToken();
  const response = await fetch(endpoints.files.uploadProductImage(productId), {
    method: 'POST',
    headers: {
      'X-Requested-With': 'XMLHttpRequest',
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: formData,
  });

  if (!response.ok) {
    const errorData: ErrorResponse = await response.json().catch(() => ({
      status: response.status,
      error: 'UPLOAD_ERROR',
      message: 'Failed to upload image',
    }));

    throw new ApiError(
      errorData.status || response.status,
      errorData.error || 'UPLOAD_ERROR',
      errorData.message || 'Failed to upload image'
    );
  }

  return await response.json();
}

// Utility function to get product by ID
export async function getProductById(id: number): Promise<Product | null> {
  try {
    const products = await getAllProducts();
    return products.find(product => product.id === id) || null;
  } catch (error) {
    console.error('Error fetching product:', error);
    return null;
  }
}

// Customer registration
export async function registerCustomer(
  customerData: CustomerRegistrationData
): Promise<CustomerRegistrationResponse> {
  return apiRequest<CustomerRegistrationResponse>(endpoints.customers.register, {
    method: 'POST',
    body: JSON.stringify(customerData),
  });
}

// Export the ApiError class for error handling in components
export { ApiError };