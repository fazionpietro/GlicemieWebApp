import { useState } from "react";
import {
    Button,
    Container,
    Group,
    Paper,
    PasswordInput,
    Progress,
    TextInput,
    Title,
    Text,
    Alert,
    Box,
    Textarea,
    Select,
} from "@mantine/core";
import "../CommonFile/App.css";
import emailcss from "../CommonFile/InvalidEmail.module.css";
import { IconAlertTriangle } from "@tabler/icons-react";
import type { Paziente } from "../type/DataType";
import { DateInput, type DateValue } from "@mantine/dates";

type Props = {
    paziente: Paziente;
};

export default function DetailsPaziente({ paziente }: Props) {
    const [email, setEmail] = useState(paziente.email);
    const [nome, setNome] = useState(paziente.nome);
    const [cognome, setCognome] = useState(paziente.cognome);
    const [dataNascita, setDataNascita] = useState<DateValue>(
        paziente.dataNascita
    );
    const [fattoriRischio, setFattoriRischio] = useState(
        paziente.fattoriRischio ?? ""
    );
    const [comorbita, setComorbita] = useState(paziente.comorbita ?? "");
    const [patologiePregresse, setPatologiePregresse] = useState(
        paziente.patologiePregresse ?? ""
    );
    const [isError, setIsError] = useState("");
    const isValidEmail = (v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
    const isInvalid = email.length > 0 && !isValidEmail(email);

    return (
        <Container fluid w={600} my={40} pl={40} pr={40}>
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
                classNames={isInvalid ? { input: emailcss.invalid } : {}}
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
                />
                <TextInput
                    size="md"
                    radius="md"
                    mb={20}
                    label="Cognome"
                    value={cognome}
                />
            </Group>

            <DateInput
                mb={20}
                size="md"
                radius="md"
                label="Data di nascita"
                placeholder="YYYY-MM-DD"
                valueFormat="YYYY-MM-DD"
                withAsterisk
                value={dataNascita}
                onChange={(d) => {
                    setDataNascita(d);
                    setIsError("");
                }}
            />
            <Textarea
                size="md"
                radius="md"
                mb={20}
                label="Fattori di rischio"
                value={fattoriRischio}
            />
            <Textarea size="md" radius="md" mb={20} label="Comorbità" />
            <Textarea
                size="md"
                radius="md"
                mb={20}
                label="Patologie pregresse"
            />
            <Select
                label="Medico curante"
                placeholder="Seleziona Medico"
                searchable
                mb={70}
                size="md"
            />

            <PasswordInput
                size="md"
                radius="md"
                mt={10}
                mb={20}
                placeholder="Nuova Password"
                rightSection={false}
                value={password}
            />
            <Group justify="flex-end">
                <Button color="gray">Annulla</Button>
                <Button color="blue">Salva</Button>
            </Group>
        </Container>
    );
}
