
import { Button, Container, Title, Text } from '@mantine/core';
import { useNavigate } from 'react-router-dom';

const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <Container size="md" style={{ textAlign: 'center', paddingTop: '100px' }}>
      <Title order={1} style={{color : 'red'}} >Accesso Negato</Title>
      <Text size="lg" style={{ marginBottom: '20px' }}>
        Non hai i permessi necessari per accedere a questa pagina.
      </Text>
      <Button onClick={() => navigate(-1)}>
        Torna Indietro
      </Button>
    </Container>
  );
};

export default Unauthorized;