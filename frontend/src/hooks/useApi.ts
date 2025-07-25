import { useState, useEffect, useCallback } from 'react';
import { ApiError } from '../utils/api';

// Generic hook for API calls with loading states
export function useApi<T>(
  apiFunction: () => Promise<T>,
  dependencies: React.DependencyList = []
) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await apiFunction();
      setData(result);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
      } else {
        setError('An unexpected error occurred');
      }
      console.error('API Error:', err);
    } finally {
      setLoading(false);
    }
  }, dependencies);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return { data, loading, error, refetch: fetchData };
}

// Hook for mutations (POST, PUT, DELETE)
export function useMutation<TInput, TOutput>(
  mutationFunction: (input: TInput) => Promise<TOutput>
) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const mutate = useCallback(
    async (input: TInput): Promise<TOutput | null> => {
      try {
        setLoading(true);
        setError(null);
        const result = await mutationFunction(input);
        return result;
      } catch (err) {
        if (err instanceof ApiError) {
          setError(err.message);
        } else {
          setError('An unexpected error occurred');
        }
        console.error('Mutation Error:', err);
        return null;
      } finally {
        setLoading(false);
      }
    },
    [mutationFunction]
  );

  return { mutate, loading, error, clearError: () => setError(null) };
}

// Hook for authentication state
export function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is authenticated on mount
    const token = sessionStorage.getItem('auth_token');
    setIsAuthenticated(!!token);
    setLoading(false);
  }, []);

  const login = useCallback((token: string) => {
    sessionStorage.setItem('auth_token', token);
    setIsAuthenticated(true);
  }, []);

  const logout = useCallback(() => {
    sessionStorage.removeItem('auth_token');
    setIsAuthenticated(false);
  }, []);

  return { isAuthenticated, loading, login, logout };
}

// Hook for form validation
export function useFormValidation<T extends Record<string, any>>(
  initialValues: T,
  validationRules: Partial<Record<keyof T, (value: any) => string | null>>
) {
  const [values, setValues] = useState<T>(initialValues);
  const [errors, setErrors] = useState<Partial<Record<keyof T, string>>>({});
  const [touched, setTouched] = useState<Partial<Record<keyof T, boolean>>>({});

  const setValue = useCallback((field: keyof T, value: any) => {
    setValues(prev => ({ ...prev, [field]: value }));
    
    // Validate on change if field was already touched
    if (touched[field] && validationRules[field]) {
      const error = validationRules[field]!(value);
      setErrors(prev => ({ ...prev, [field]: error || undefined }));
    }
  }, [validationRules, touched]);

  const setFieldTouched = useCallback((field: keyof T) => {
    setTouched(prev => ({ ...prev, [field]: true }));
    
    // Validate when field is touched
    if (validationRules[field]) {
      const error = validationRules[field]!(values[field]);
      setErrors(prev => ({ ...prev, [field]: error || undefined }));
    }
  }, [validationRules, values]);

  const validateAll = useCallback(() => {
    const newErrors: Partial<Record<keyof T, string>> = {};
    let isValid = true;

    Object.keys(validationRules).forEach((field) => {
      const fieldKey = field as keyof T;
      const validator = validationRules[fieldKey];
      if (validator) {
        const error = validator(values[fieldKey]);
        if (error) {
          newErrors[fieldKey] = error;
          isValid = false;
        }
      }
    });

    setErrors(newErrors);
    setTouched(
      Object.keys(validationRules).reduce(
        (acc, field) => ({ ...acc, [field]: true }),
        {}
      )
    );

    return isValid;
  }, [validationRules, values]);

  const reset = useCallback(() => {
    setValues(initialValues);
    setErrors({});
    setTouched({});
  }, [initialValues]);

  return {
    values,
    errors,
    touched,
    setValue,
    setFieldTouched,
    validateAll,
    reset,
    isValid: Object.keys(errors).length === 0,
  };
}

// Hook for debounced values (useful for search)
export function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}