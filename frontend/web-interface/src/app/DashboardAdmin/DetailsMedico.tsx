import { useEffect, useState } from "react";
import {
  Button,
  Container,
  Group,
  PasswordInput,
  TextInput,
  Title,
  Textarea,
  Select,
  Modal,
  ActionIcon,
  Alert,
} from "@mantine/core";
import "../CommonFile/App.css";
import emailcss from "../CommonFile/InvalidEmail.module.css";
import { IconAlertTriangle, IconPencil } from "@tabler/icons-react";
import type { Medico, Paziente } from "../type/DataType";
import { DateInput, type DateValue } from "@mantine/dates";
import { useDisclosure } from "@mantine/hooks";
import axios from "axios";

type Props = {
  medico: Medico;
  fetchMedici: () => void;
};

export default function DetailsMedico({
  medico,
  fetchMedici
}: Props) {
  const [email, setEmail] = useState(medico.email);
  const [nome, setNome] = useState(medico.nome);
  const [cognome, setCognome] = useState(medico.cognome);
  const [password, setPassword] = useState("");
  const [dataNascita, setDataNascita] = useState<DateValue>(
    medico.dataNascita
  );


  const [openedEdit, { open: openEdit, close: closeEdit }] =
    useDisclosure(false);

  const [isError, setIsError] = useState("");
  const isValidEmail = (v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
  const isInvalid = email.length > 0 && !isValidEmail(email);

  async function handleSave() {

    await axios({
      method: "PUT",
      url: `${import.meta.env.VITE_API_KEY}api/utenti/update`,

      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
      data: {
        id: medico.id,
        email,
        nome,
        "passwordHash": password,
        cognome,
        dataNascita,


      },
    })
      .then((res) => {
        console.log(res);
        closeEdit();
        fetchMedici();
      })
      .catch((err) => {
        console.error(err);
      });
  }

  function handleClose() {
    setEmail(medico.email);
    setNome(medico.nome);
    setCognome(medico.cognome);
    setPassword("");
    setDataNascita(medico.dataNascita);


    closeEdit();
  }





  return (
    <div>
      <Modal
        opened={openedEdit}
        onClose={handleClose}
        radius={"md"}
        size={"auto"}
        centered
      >
        <Container fluid w={600} my={10} pl={40} pr={40}>
          <Title size="xl" pb={30}>
            Dettagli paziente
          </Title>

          <TextInput
            size="md"
            radius="md"
            mb={20}
            label="Email"
            value={email}
            onChange={(e) => {
              setEmail(e.currentTarget.value);
              setIsError("");
            }}
            error={isInvalid ? "Invalid email" : null}
            placeholder="Email"
            classNames={
              isInvalid ? { input: emailcss.invalid } : {}
            }
            rightSection={
              isInvalid ? (
                <IconAlertTriangle size={18} stroke={1.5} />
              ) : null
            }
          />

          <Group>
            <TextInput
              size="md"
              radius="md"
              mb={20}
              label="Nome"
              value={nome}
              onChange={(e) => setNome(e.currentTarget.value)}
            />
            <TextInput
              size="md"
              radius="md"
              mb={20}
              label="Cognome"
              value={cognome}
              onChange={(e) => setCognome(e.currentTarget.value)}
            />
          </Group>

          <DateInput
            mb={"40"}
            size="md"
            radius="md"
            label="Data di nascita"
            placeholder="DD-MM-YYYY"
            valueFormat="DD-MM-YYYY"
            withAsterisk
            value={dataNascita}
            onChange={(d) => {
              setDataNascita(d);
              console.log(d);
              setIsError("");
            }}
          />

          <PasswordInput
            size="md"
            radius="md"
            mb={20}
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
            placeholder="Password"
            label="Nuova Password"
          />
          {isError && (
            <Alert
              variant="light"
              color="red"
              title="Errore"
              ta={"left"}
              withCloseButton
              onClose={() => setIsError("")}
              icon={<IconAlertTriangle size={18} stroke={1.5} />}
              mb="md"
            >
              {isError}
            </Alert>
          )}

          <Group justify="flex-end" mt="md" mb={60}>
            <Button
              variant="light"
              color="gray"
              onClick={handleClose}
            >
              Annulla
            </Button>
            <Button onClick={handleSave}>Salva</Button>
          </Group>
        </Container>
      </Modal>

      <ActionIcon
        variant="subtle"
        color="gray"
        onClick={() => {
          openEdit();
        }}
      >
        <IconPencil size={16} stroke={1.5} />
      </ActionIcon>
    </div>
  );
}
