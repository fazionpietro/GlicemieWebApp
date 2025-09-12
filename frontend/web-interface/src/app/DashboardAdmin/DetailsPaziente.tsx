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
    paziente: Paziente;
    medici: Medico[] | null;
    fetchMedici: () => void;
    fetchPazienti: () => void;
};

export default function DetailsPaziente({
    paziente,
    medici,
    fetchMedici,
    fetchPazienti,
}: Props) {
    const [email, setEmail] = useState(paziente.email);
    const [nome, setNome] = useState(paziente.nome);
    const [cognome, setCognome] = useState(paziente.cognome);
    const [password, setPassword] = useState("");
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
    const [medicoDisplay, setMedicoDisplay] = useState(
        paziente.idMedico
            ? `${paziente.cognomeMedico} ${paziente.nomeMedico}`
            : ""
    );
    const [idMedico, setIdMedico] = useState(paziente.idMedico ?? "");

    const [openedEdit, { open: openEdit, close: closeEdit }] =
        useDisclosure(false);

    const [isError, setIsError] = useState("");
    const isValidEmail = (v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
    const isInvalid = email.length > 0 && !isValidEmail(email);

    async function handleSave() {

        
        await axios({
            method: "PUT",
            url: `${import.meta.env.VITE_API_KEY}api/pazienti/update`,

            headers: {
                "Content-Type": "application/json",
                withCredentials: true,
            },
            data: {
                id: paziente.id,
                email,
                nome,
                cognome,
                dataNascita,
                ruolo: "ROLE_PAZIENTE",
                fattoriRischio,
                comorbita,
                patologiePregresse,
                idMedico,
                "passwordHash":password
            },
        })
            .then((res) => {
                console.log(res);
                closeEdit();
                fetchPazienti();
            })
            .catch((err) => {
                console.error(err);
            });
    }

    function handleClose() {
        setEmail(paziente.email);
        setNome(paziente.nome);
        setCognome(paziente.cognome);
        setPassword("");
        setDataNascita(paziente.dataNascita);
        setFattoriRischio(paziente.fattoriRischio ?? "");
        setComorbita(paziente.comorbita ?? "");
        setPatologiePregresse(paziente.patologiePregresse ?? "");
        setMedicoDisplay(`${paziente.cognome} ${paziente.nome}`);

        closeEdit();
    }

    useEffect(() => {
        setIdMedico(paziente.idMedico ?? "");
        setMedicoDisplay(
            paziente.idMedico
                ? `${paziente.cognomeMedico} ${paziente.nomeMedico}`
                : ""
        );
    }, [paziente]);

    useEffect(() => {
        if (medicoDisplay == null) {
            fetchMedici();
            console.log("fetchiammo");
        }
    }, []);

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

                    <Textarea
                        size="md"
                        radius="md"
                        mb={20}
                        label="Fattori di rischio"
                        value={fattoriRischio}
                        onChange={(e) =>
                            setFattoriRischio(e.currentTarget.value)
                        }
                    />

                    <Textarea
                        size="md"
                        radius="md"
                        mb={20}
                        label="Comorbità"
                        value={comorbita}
                        onChange={(e) => setComorbita(e.currentTarget.value)}
                    />

                    <Textarea
                        size="md"
                        radius="md"
                        mb={20}
                        label="Patologie pregresse"
                        value={patologiePregresse}
                        onChange={(e) =>
                            setPatologiePregresse(e.currentTarget.value)
                        }
                    />

                    <Select
                        label="Medico curante"
                        placeholder="Seleziona Medico"
                        searchable
                        size="md"
                        mb={60}
                        value={idMedico}
                        data={
                            medici?.map((m) => ({
                                value: m.id,
                                label: `${m.cognome} ${m.nome}`,
                            })) ?? []
                        }
                        onChange={(val) => {
                            if (val) {
                                setIdMedico(val);
                                const selected = medici?.find(
                                    (m) => m.id === val
                                );
                                setMedicoDisplay(
                                    selected
                                        ? `${selected.cognome} ${selected.nome}`
                                        : ""
                                );
                            }
                        }}
                    />

                    <PasswordInput
                        size="md"
                        radius="md"
                        mt={10}
                        mb={20}
                        placeholder="Nuova Password"
                        rightSection={false}
                        value={password}
                        onChange={(e) => setPassword(e.currentTarget.value)}
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
