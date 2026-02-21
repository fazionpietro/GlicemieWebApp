import React from 'react';
import { useRole } from '../hook/useRole';
import { Alert, Container } from '@mantine/core';
import { IconAlertTriangle } from '@tabler/icons-react';

interface RoleGuardProps {
  children: React.ReactNode;
  allowedRoles: string | string[];
  fallback?: React.ReactNode;
}

/**
 * Component to restricts access to part of the UI based on user roles.
 * Displays a fallback or an alert if the user does not have the required role.
 */
export const RoleGuard: React.FC<RoleGuardProps> = ({
  children,
  allowedRoles,
  fallback
}) => {
  const { hasRole } = useRole();

  if (!hasRole(allowedRoles)) {
    return fallback || (
      <Container>
        <Alert
          icon={<IconAlertTriangle size="1rem" />}
          title="Accesso negato"
          color="red"
          variant="filled"
        >
          Non hai i permessi necessari per accedere a questa sezione.
        </Alert>
      </Container>
    );
  }

  return <>{children}</>;
};