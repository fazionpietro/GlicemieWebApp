import { useEffect, useState } from "react";
import {
  Button,
  Container,
  Group,
  TextInput,
  Title,
  Alert,
  NumberInput,
  Textarea,
  Select,
  Modal,
  ActionIcon,
} from "@mantine/core";
import { IconAlertTriangle, IconPencil } from "@tabler/icons-react";
import "../Components/App.css";
import type { Terapia } from "../type/DataType";
import { useDisclosure } from "@mantine/hooks";
import axios from "axios";

type Props = {
  terapia: Terapia;
  fetchTerapie: () => void;
};

export default function DetailsTerapia({
  terapia,
  fetchTerapie,
}: Props) {
  const [farmaco, setFarmaco] = useState(terapia.farmaco);
  const [numAssunzioni, setNumAssunzioni] = useState<string | number>(
    terapia.numAssunzioni
  );
  const [dosaggio, setDosaggio] = useState(terapia.dosaggio);
  const [indicazioni, setIndicazioni] = useState(terapia.indicazioni);


  const [openedEdit, { open: openEdit, close: closeEdit }] =
    useDisclosure(false);
  const [isError, setIsError] = useState("");

  async function handleSave() {
    await axios({
      method: "PUT",
      url: `${import.meta.env.VITE_API_KEY}api/terapie/update/${terapia.id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
      data: {
        id: terapia.id,
        farmaco,
        numAssunzioni,
        dosaggio,
        indicazioni,
      },
    })
      .then((res) => {
        console.log(res);
        closeEdit();
        fetchTerapie();
      })
      .catch((err) => {
        console.error(err);
        setIsError("Errore durante l'aggiornamento della terapia");
      });
  }

  function handleClose() {
    setFarmaco(terapia.farmaco);
    setNumAssunzioni(terapia.numAssunzioni);
    setDosaggio(terapia.dosaggio);
    setIndicazioni(terapia.indicazioni);

    setIsError("");
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
            Dettagli terapia
          </Title>

          <Group mb={20}>
            <TextInput
              size="md"
              radius="md"
              label="Farmaco"
              withAsterisk
              placeholder="Farmaco"
              value={farmaco}
              onChange={(e) => {
                setFarmaco(e.currentTarget.value);
                setIsError("");
              }}
              w="70%"
            />
            <NumberInput
              size="md"
              radius="md"
              label="num. Assunzioni"
              value={numAssunzioni}
              onChange={setNumAssunzioni}
              w="26%"
            />
          </Group>

          <TextInput
            mb={20}
            size="md"
            radius="md"
            label="Dosaggio"
            withAsterisk
            placeholder="Dosaggio"
            value={dosaggio}
            onChange={(e) => {
              setDosaggio(e.currentTarget.value);
              setIsError("");
            }}
          />

          <Textarea
            size="md"
            radius="md"
            mb={20}
            label="Indicazioni"
            placeholder="Indicazioni"
            value={indicazioni}
            onChange={(e) => setIndicazioni(e.currentTarget.value)}
          />



          {isError && (
            <Alert
              variant="light"
              color="red"
              title="Errore"
              ta="left"
              withCloseButton
              onClose={() => setIsError("")}
              icon={<IconAlertTriangle size={18} stroke={1.5} />}
              mb="md"
            >
              {isError}
            </Alert>
          )}

          <Group justify="flex-end" mt="md" mb={60}>
            <Button variant="light" color="gray" onClick={handleClose}>
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
