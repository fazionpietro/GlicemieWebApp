import { useDisclosure } from '@mantine/hooks';
import { NumberInput, Modal, Button, Container, Title, Checkbox } from '@mantine/core';
import { useState } from 'react';
import axios, { AxiosError } from 'axios';
import { useAuth } from "../../context/AuthContext";

interface Props {
  onRilevazione?: () => void;
}

/**
 * Modal component for entering a new glucose reading.
 * Allows specifying the value and whether it was taken after a meal.
 */
function ModalGlicemia({ onRilevazione }: Props) {
  const [opened, { open, close }] = useDisclosure(false);
  const [valore, setValore] = useState<number | undefined>(undefined);
  const { user } = useAuth();
  const [checked, setChecked] = useState(false);

  async function inserisciRilevazione() {
    if (valore === undefined) {
      alert("Inserisci un valore glicemico");
      return;
    }

    if (!user) {
      return;
    }

    try {
      await axios({
        method: "post",
        url: `${import.meta.env.VITE_API_KEY}api/rilevazioni/${user.id}`,
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
        params: { valore: valore, pasto: checked },
      });

      setValore(undefined);
      if (onRilevazione) {
        onRilevazione();
      }
      close();
    } catch (error) {
      const axiosError = error as AxiosError<string>;
      console.error("Errore nell'inserimento della rilevazione:", axiosError);

      if (axiosError.response?.status === 400) {
        alert("dati non validi");
      } else if (axiosError.response?.status === 500) {
        alert("Errore del server. Riprova più tardi");
      } else {
        alert("Si è verificato un errore durante l'inserimento della rilevazione");
      }
    }
  }

  return (
    <>
      <Modal opened={opened} size={"auto"} onClose={close} centered>
        <Container fluid w={600}>

          <Title size={"xl"} mb={30} >Inserisci Glicemia</Title>

          <NumberInput
            mb={20}
            size="md"
            radius="md"
            placeholder="Inserisci valore Glicemico qui"
            w="100%"
            min={0}
            value={valore}
            onChange={(v) => setValore(typeof v === "number" ? v : undefined)}
          />
          <Checkbox
            checked={checked}
            onChange={(event) => setChecked(event.currentTarget.checked)}
            label="dopo pasto"
          />
          <Button mt={20} mb={30} onClick={inserisciRilevazione}>invia</Button>
        </Container>
      </Modal>


      <Button style={{
        width: '30%',
        height: '40px',
        fontSize: '14px',
        right: '5px'
      }} type="submit" onClick={open} mt="30">
        Nuova Rilevazione
      </Button>
    </>
  );
}

export default ModalGlicemia;
