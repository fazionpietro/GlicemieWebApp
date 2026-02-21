/* eslint-disable react-refresh/only-export-components */
import React, { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import axios from 'axios';
import type { User, AuthContextType } from '../app/type/DataType';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

/**
 * Provider component for the authentication context.
 * Manages user state, login, logout, and session verification.
 */
export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // Funzione per verificare se l'utente è autenticato tramite cookie
  const checkAuth = async (): Promise<void> => {
    try {
      setIsLoading(true);

      // Chiamata al backend per verificare il token nel cookie
      const response = await axios.get(
        `${import.meta.env.VITE_API_KEY}api/auth/verify`,
        {
          withCredentials: true,
          timeout: 5000
        }
      );

      if (response.status === 200) {
        setUser({
          id: response.data.id,
          email: response.data.email,
          role: response.data.role
        });
      }
    } catch {
      console.error('Auth check failed');
      setUser(null);
      // Rimuovi eventuali dati obsoleti dal localStorage
      localStorage.removeItem('user');
    } finally {
      setIsLoading(false);
    }
  };

  const login = (userData: User) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const logout = async () => {
    try {
      // Chiamata al backend per invalidare il cookie
      await axios.post(
        `${import.meta.env.VITE_API_KEY}api/auth/logout`,
        {},
        { withCredentials: true }
      );
    } catch {
      console.error('Logout error');
    } finally {
      setUser(null);
      localStorage.removeItem('user');
    }
  };


  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        const userData = JSON.parse(storedUser);
        setUser(userData);
        // Verifica comunque con il backend
        checkAuth();
      } catch {
        localStorage.removeItem('user');
        checkAuth();
      }
    } else {
      checkAuth();
    }
  }, []);

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
    checkAuth
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
