import { useState } from "react";
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

} from "@mantine/core";
import { IconAlertTriangle } from "@tabler/icons-react";


import "../Components/App.css";
import type { Paziente } from "../type/DataType";
import axios from "axios";

type Props = {
  pazienti: Paziente[] | null,
  fetchTerapie: () => void,

  onSuccess: () => void
}

export function RegisterTerapia({ pazienti, fetchTerapie, onSuccess }: Props) {

  const [numAssunzioni, setNumAssunzioni] = useState<string | number>('');
  const [farmaco, setFarmaco] = useState("");
  const [dosaggio, setDosaggio] = useState("");
  const [indicazioni, setIndicazioni] = useState("")
  const [idPaziente, setIdPaziente] = useState("")

  const [isError, setIsError] = useState("");


  async function createTerapia() {


    const user = await JSON.parse(localStorage.getItem("user") ?? "")

    await axios({
      method: "POST",
      url: `${import.meta.env.VITE_API_KEY}api/terapie/new`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,

      },
      data: {
        farmaco,
        numAssunzioni,
        dosaggio,
        indicazioni,
        idPaziente,
        "idMedico": `${user.id}`
      }
    }).then((res) => {
      fetchTerapie();
      onSuccess();
    })
      .catch((err) => {
        console.error(err);
      });
  }



  return (
    <Container fluid w={600} my={10} pl={40} pr={40}>
      <Title size={"xl"} pb={30}>
        Registra terapia
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
          min={0}
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
        placeholder="Indiccazioni"
        value={indicazioni}
        onChange={(e) => setIndicazioni(e.currentTarget.value)}
      />

      <Select
        label="Paziente"
        placeholder="Paziente"
        searchable
        size="md"
        mb={60}
        value={idPaziente}
        data={pazienti?.map((m) => ({
          value: m.id,
          label: `${m.nome} ${m.cognome} `,
        }))}
        onChange={(val) => {
          if (val) {
            setIdPaziente(val);
          }
        }}
      />

      {
        isError && (
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
        )
      }

      <Button
        size="md"
        fullWidth
        mt="xl"
        radius="md"
        mb={80}
        onClick={createTerapia}
      >
        Salva
      </Button>
    </Container >
  );
}
