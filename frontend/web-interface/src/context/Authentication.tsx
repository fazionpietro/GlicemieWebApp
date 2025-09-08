// AuthContext.tsx
import React, { createContext, useContext, useState, useEffect, type ReactNode } from 'react';

interface User {
  id: string;
  email: string;
  role: string;
  token: string;
}

interface AuthContextType {
  user: User | null;
  login: (userData: User) => void;
  logout: () => void;
  hasRole: (roles: string[]) => boolean;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Recupera i dati utente dal localStorage all'avvio
    const token = localStorage.getItem('access_token');
    const id = localStorage.getItem('id');
    const email = localStorage.getItem('email');
    const role = localStorage.getItem('role');
    
    if (token && id && role) {
      setUser({ token, id, role, email: email || '' })  ;
    }
    setLoading(false);
  }, []);

  const login = (userData: User) => {
    setUser(userData);
    localStorage.setItem('access_token', userData.token);
    localStorage.setItem('id', userData.id);
    localStorage.setItem('email', userData.email);
    localStorage.setItem('role', userData.role);
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('access_token');
    localStorage.removeItem('id');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
  };

  const hasRole = (roles: string[]): boolean => {
    if (!user) return false;
    return roles.includes(user.role);
  };

  const value = {
    user,
    login,
    logout,
    hasRole,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};