import { useCallback, useState } from "react";
import { Button, Container, Group, TextInput, Title, Alert, NumberInput, Textarea, Select } from "@mantine/core";
import { IconAlertTriangle } from "@tabler/icons-react";
import type { Paziente } from "../type/DataType";
import axios from "axios";

type Props = {
  pazienti: Paziente[] | null;
  fetchTerapie: () => void;
  onSuccess: () => void;
};

export function RegisterTerapia({ pazienti, fetchTerapie, onSuccess }: Props) {
  const [numAssunzioni, setNumAssunzioni] = useState("");
  const [farmaco, setFarmaco] = useState("");
  const [dosaggio, setDosaggio] = useState("");
  const [indicazioni, setIndicazioni] = useState("");
  const [idPaziente, setIdPaziente] = useState("");
  const [isError, setIsError] = useState("");

  const createTerapia = useCallback(async () => {
    try {
      const raw = localStorage.getItem("user");
      if (!raw) throw new Error("Utente non presente in sessione");
      const user = JSON.parse(raw);
      await axios.post(
        `${import.meta.env.VITE_API_KEY}api/terapie/new`,
        {
          farmaco,
          numAssunzioni,
          dosaggio,
          indicazioni,
          idPaziente,
          idMedico: `${user.id}`,
        },
        { headers: { "Content-Type": "application/json", withCredentials: true as any } }
      );
      fetchTerapie();
      onSuccess();
    } catch (err: any) {
      console.error(err);
      setIsError(err?.message ?? "Errore durante la creazione");
    }
  }, [farmaco, numAssunzioni, dosaggio, indicazioni, idPaziente, fetchTerapie, onSuccess]);

  return (
    <Container>
      <Title order={4} mb="sm">Registra terapia</Title>
      <TextInput
        label="Farmaco"
        value={farmaco}
        onChange={(e) => { setFarmaco(e.currentTarget.value); setIsError(""); }}
      />
      <TextInput
        label="Dosaggio"
        value={dosaggio}
        onChange={(e) => { setDosaggio(e.currentTarget.value); setIsError(""); }}
      />
      <Textarea
        label="Indicazioni"
        value={indicazioni}
        onChange={(e) => setIndicazioni(e.currentTarget.value)}
      />
      <Select
        label="Paziente"
        data={(pazienti ?? []).map(m => ({ value: m.id, label: `${m.nome} ${m.cognome}` }))}
        value={idPaziente}
        onChange={(val) => { if (val) setIdPaziente(val); }}
        searchable
      />
      {isError && (
        <Alert color="red" icon={<IconAlertTriangle />} mb="md" withCloseButton onClose={() => setIsError("")}>
          {isError}
        </Alert>
      )}
      <Group justify="flex-end" mt="md">
        <Button onClick={createTerapia}>Salva</Button>
      </Group>
    </Container>
  );
}
