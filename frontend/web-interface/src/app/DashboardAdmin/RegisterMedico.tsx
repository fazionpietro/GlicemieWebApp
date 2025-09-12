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
} from "@mantine/core";
import { DateInput, type DateValue } from "@mantine/dates";
import { IconAlertTriangle, IconCheck, IconX } from "@tabler/icons-react";
import { useInputState } from "@mantine/hooks";
import { useAuth } from "../../context/AuthContext";
import axios, { AxiosError } from "axios";
import type { User } from "../type/DataType";

/* ---------- Stili CSS ---------- */
import "../CommonFile/App.css";
import emailcss from "../CommonFile/InvalidEmail.module.css";
import { useNavigate } from "react-router";

const requirements = [
    { re: /[0-9]/, label: "Includes number" },
    { re: /[a-z]/, label: "Includes lowercase letter" },
    { re: /[A-Z]/, label: "Includes uppercase letter" },
    { re: /[$&+,:;=?@#|'<>.^*()%!-]/, label: "Includes special symbol" },
];

function PasswordRequirement({
    meets,
    label,
}: {
    meets: boolean;
    label: string;
}) {
    return (
        <Text c={meets ? "teal" : "red"} mt={5} size="sm">
            <Group gap={4} align="center">
                {meets ? (
                    <IconCheck size={14} stroke={1.5} />
                ) : (
                    <IconX size={14} stroke={1.5} />
                )}
                <Box ml={7}>{label}</Box>
            </Group>
        </Text>
    );
}

function getStrength(password: string) {
    let multiplier = password.length > 5 ? 0 : 1;
    requirements.forEach((req) => {
        if (!req.re.test(password)) multiplier += 1;
    });
    return Math.max(100 - (100 / (requirements.length + 1)) * multiplier, 0);
}

type Props = {
  onSuccess: () => void; // <- nuova prop
};

export function RegisterMedico({onSuccess}:Props) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useInputState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
    const [dataNascita, setDataNascita] = useState<DateValue>(null);
    const [isError, setIsError] = useState("");
    const isValidEmail = (v: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
    const isInvalid = email.length > 0 && !isValidEmail(email);
    const strength = getStrength(password);
    const checks = requirements.map((r, i) => (
        <PasswordRequirement
            key={i}
            label={r.label}
            meets={r.re.test(password)}
        />
    ));

    /* --- UI password bars --- */
    const bars = Array(4)
        .fill(0)
        .map((_, i) => (
            <Progress
                styles={{ section: { transitionDuration: "0ms" } }}
                value={
                    password.length > 0 && i === 0
                        ? 100
                        : strength >= ((i + 1) / 4) * 100
                        ? 100
                        : 0
                }
                color={
                    strength > 80 ? "teal" : strength > 50 ? "yellow" : "red"
                }
                key={i}
                size={4}
            />
        ));


    async function handleRegister() {
        if (password !== confirmPassword) {
            setIsError("Le password non coincidono");
            return;
        }
        if (!dataNascita) {
            setIsError("Inserisci una data di nascita valida");
            return;
        }

        const body = {
            email,
            password,
            nome,
            cognome,
            dataNascita: dataNascita,
            
        };
        console.log(body);
        
        await axios
            .post(
                `${import.meta.env.VITE_API_KEY}api/auth/signup/medico`,
                body,
                { headers: { "Content-Type": "application/json" } }
            )
            .then((res) => {
                console.log(res);
                onSuccess();
            })
            .catch((err) => {
                setIsError(
                    err instanceof AxiosError
                        ? err.response?.data?.message || err.message
                        : err.toString()
                );
                setPassword("");
                setConfirmPassword("");
            });
    }    




    return (
        <Container fluid w={600} my={10} pl={40} pr={40}>
            <Title size={"xl"} pb={30}>
                Registra medico
            </Title>
            {/* --- Email --- */}
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
                required
                error={isInvalid ? "Invalid email" : null}
                placeholder="Email"
                classNames={isInvalid ? { input: emailcss.invalid } : {}}
                rightSection={
                    isInvalid ? (
                        <IconAlertTriangle size={18} stroke={1.5} />
                    ) : null
                }
            />

            {/* --- Nome / Cognome --- */}
            <Group grow mb={20}>
                <TextInput
                    size="md"
                    radius="md"
                    label="Nome"
                    withAsterisk
                    placeholder="Nome"
                    value={nome}
                    onChange={(e) => {
                        setNome(e.currentTarget.value);
                        setIsError("");
                    }}
                />
                <TextInput
                    size="md"
                    radius="md"
                    label="Cognome"
                    withAsterisk
                    placeholder="Cognome"
                    value={cognome}
                    onChange={(e) => {
                        setCognome(e.currentTarget.value);
                        setIsError("");
                    }}
                />
            </Group>

            {/* --- Data di nascita --- */}
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

            {/* --- Password --- */}
            <PasswordInput
                size="md"
                radius="md"
                mb={20}
                value={password}
                onChange={setPassword}
                placeholder="Password"
                label="Password"
                required
            />

            <Group gap={5} grow mt="xs" mb="md">
                {bars}
            </Group>

            <PasswordRequirement
                label="Has at least 6 characters"
                meets={password.length > 5}
            />
            {checks}

            <PasswordInput
                mt={20}
                size="md"
                radius="md"
                mb={35}
                value={confirmPassword}
                onChange={(e) => {
                    setConfirmPassword(e.currentTarget.value);
                    setIsError("");
                }}
                placeholder="Confirm password"
                label="Confirm Password"
                required
                error={
                    confirmPassword.length > 0 && confirmPassword !== password
                        ? "Passwords do not match"
                        : null
                }
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

            <Button
                size="md"
                fullWidth
                mt="xl"
                radius="md"
                mb={80}
                onClick={handleRegister}
                disabled={
                    password.length <= 0 ||
                    (password.length != 0 && confirmPassword !== password)
                }
            >
                Register
            </Button>
        </Container>
    );
}
