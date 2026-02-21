import { useAuth } from '../context/AuthContext';

/**
 * Custom hook to check user roles.
 * Provides functions to verify if the current user has specific roles (admin, medico, paziente).
 */
export const useRole = () => {
  const { user } = useAuth();

  const hasRole = (role: string | string[]): boolean => {
    if (!user) return false;

    const userRole = user.role.replace('ROLE_', '').toLowerCase();
    const roles = Array.isArray(role) ? role : [role];

    return roles.some(r => userRole === r.toLowerCase());
  };

  const isAdmin = (): boolean => hasRole('admin');
  const isMedico = (): boolean => hasRole('medico');
  const isPaziente = (): boolean => hasRole('paziente');

  return {
    userRole: user?.role,
    hasRole,
    isAdmin,
    isMedico,
    isPaziente
  };
};