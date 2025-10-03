import { useState } from 'react';
import {
  AppShell,
  Group,
  Text,
  Button,
  Avatar,
  Menu,
  UnstyledButton,
  rem,
  Loader
} from '@mantine/core';
import {
  IconDroplet,
  IconUser,
  IconLogout,
  IconSettings,
  IconChevronDown,
  IconLogin
} from '@tabler/icons-react';
import classes from './HeaderTabs.module.css'
import { useAuth } from '../../context/AuthContext';


export function HeaderMegaMenu() {
  const { user, isAuthenticated, isLoading, logout } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
    } catch (error) {
      console.error('Errore durante il logout:', error);
    }
  };

  const handleLogin = () => {

    
  };

  return (
    <Group h="100%" px="md" justify="space-between" mb={80} >
      <header className={classes.header}>
        {/* Logo e Nome App a Sinistra */}
        <Group gap="sm" w="100%" pl={30}>
          <Avatar
            size={40}
            radius="md"
            variant="gradient"
            gradient={{ from: 'blue', to: 'cyan', deg: 90 }}
          >
            <IconDroplet size={24} />
          </Avatar>
          <div>
            <Text size="lg" fw={700} c="blue">
              GlycoTracker
            </Text>
            <Text size="xs" c="dimmed">
              Monitoraggio Glicemico
            </Text>
          </div>
        </Group>

        <Group gap="sm" justify='flex-end' w='100%'>
          {isLoading ? (
            <Loader size="sm" />
          ) : !isAuthenticated ? (
            <Button
              variant="filled"
              size="sm"
              leftSection={<IconLogin size={16} />}
              onClick={handleLogin}
            >
              Accedi
            </Button>
          ) : (
            <Menu shadow="md" width={200} position="bottom-end">
              <Menu.Target>
                <UnstyledButton>
                  <Group gap="xs">
                    <Avatar size={32} radius="xl" color="blue" variant="light">
                      <IconUser size={18} />
                    </Avatar>
                    <div style={{ flex: 1, textAlign: 'left' }}>
                      <Text size="sm" fw={500}>
                        {user?.email || 'Utente'}
                      </Text>
                    </div>
                    <IconChevronDown size={16} />
                  </Group>
                </UnstyledButton>
              </Menu.Target>

              <Menu.Dropdown>



                <Menu.Divider />

                <Menu.Item
                  color="red"
                  leftSection={<IconLogout style={{ width: rem(14), height: rem(14) }} />}
                  onClick={handleLogout}
                >
                  Disconnetti
                </Menu.Item>
              </Menu.Dropdown>
            </Menu>
          )}
        </Group>
      </header >
    </Group >);
}
