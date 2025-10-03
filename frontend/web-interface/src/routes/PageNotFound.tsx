import { Button, Container, Group, Text, Title } from '@mantine/core';
import classes from './Error.module.css';
import { Navigate, useNavigate } from 'react-router';

export function PageNotFound() {
  const navigate = useNavigate()
  return (
    <div className={classes.root}>
      <Container>
        <div className={classes.label}>404</div>
        <Title className={classes.title}>Nothing to see here</Title>
        <Text size="lg" ta="center" className={classes.description}>
          Page you are trying to open does not exist. You may have mistyped the address, or the page has been moved to another URL. If you think this is an error contact support.
        </Text>
        <Group justify="center" >
          <Button variant="white" size="md" onClick={() => navigate(-1)}>
            Back
          </Button>
        </Group>
      </Container>
    </div>
  );
}
