import { Button, Container, Group, Text, Title } from '@mantine/core';
import classes from './Error.module.css';
import { Navigate, useNavigate } from 'react-router';

export function Unhautorized() {
  const navigate = useNavigate()
  return (
    <div className={classes.root}>
      <Container>
        <div className={classes.label}>401</div>
        <Title className={classes.title}>You dont have the permission to see this page</Title>
        <Text size="lg" ta="center" className={classes.description}>
          Return to the log in page and log in with the correct accout
        </Text>
        <Group justify="center" >
          <Button variant="white" size="md" onClick={() => navigate('/login')}>
            Login
          </Button>
        </Group>
      </Container>
    </div>
  );
}
