import { useDisclosure } from '@mantine/hooks';
import { NumberInput, Modal, Button, Container, TextInput, Title } from '@mantine/core';
import { JsonInput } from '@mantine/core';
import { useState } from 'react';
import axios, { AxiosError, type AxiosResponse } from 'axios';
import { useAuth } from "../../context/AuthContext";


function ModalSintomi() {
  const [opened, { open, close }] = useDisclosure(false);

  return (
    <>
      <Modal opened={opened} onClose={close} title="Inserisci una descrizione dei sintomi" centered>
        <TextInput
          size="xs"
          radius="lg"
          placeholder="segnala i sintomi qui"
          w="100%"
        />
        <button style={{ marginTop: "20px" }}>invia</button>
      </Modal>

      <Button fullWidth style={{ height: '60px' }} type="submit" onClick={open} mt="30">
        Inserisci malattia o sintomo
      </Button>
    </>
  );
}

function ModalMedicinali() {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <>
      <Modal opened={opened} onClose={close} title="Inserisci i medicinali assunti" centered>
        <TextInput
          size="xs"
          radius="lg"
          placeholder="Inserisci nome e quantità delle assunzioni qui"
          w="100%"
        />
        <button style={{ marginTop: "20px" }}>invia</button>
      </Modal>

      <Button fullWidth style={{ height: '60px' }} type="submit" onClick={open} mt="30">
        Inserisci Medicinali Assunti
      </Button>
    </>
  );
}

function ModalGlicemia() {
  const [opened, { open, close }] = useDisclosure(false);
  const [valore, setValore] = useState<number | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);
  const { user } = useAuth();

  async function inserisciRilevazione() {
    if (valore === undefined) {
      alert("Inserisci un valore glicemico");
      return;
    }

    if (!user) {
      return null;
    }

    setIsLoading(true);
    console.log(user.id)
    try {
      const response: AxiosResponse = await axios({
        method: "post",
        url: `${import.meta.env.VITE_API_KEY}api/rilevazioni/${user.id}`,
        headers: { "Content-Type": "application/json", withCredentials: true },
        params: { valore: valore, },
      });

      console.log("Rilevazione inserita:", response.data);
      alert("Rilevazione inserita con successo");

      setValore(undefined);
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
    } finally {
      setIsLoading(false);
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
          <Button mt={20} mb={30} onClick={inserisciRilevazione}>invia</Button>
        </Container>
      </Modal>

      <Button style={{width: '30%',
        height: '40px',
        fontSize: '14px',
        right: '5px'
      }} type="submit" onClick={open} mt="30">
        Nuova Rilevazione
      </Button>
    </>
  );
}


export { ModalMedicinali, ModalSintomi, ModalGlicemia };
