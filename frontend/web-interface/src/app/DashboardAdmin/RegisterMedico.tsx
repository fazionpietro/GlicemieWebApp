import { useState } from "react";
import {
  Button,
  Container,
  Group,

  PasswordInput,
  Progress,
  TextInput,
  Title,
  Text,
  Alert,
  Box,

} from "@mantine/core";
import { DateInput, type DateValue } from "@mantine/dates";
import { IconAlertTriangle, IconCheck, IconX } from "@tabler/icons-react";
import { useInputState } from "@mantine/hooks";

import axios, { AxiosError } from "axios";

import "../Components/App.css";
import emailcss from "../Components/InvalidEmail.module.css";


const requirements = [
  { re: /[0-9]/, label: "Include numeri" },
  { re: /[a-z]/, label: "Include lettere minuscole" },
  { re: /[A-Z]/, label: "Include lettere maiuscole" },
  { re: /[$&+,:;=?@#|'<>.^*()%!-]/, label: "Include simboli speciali" },
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

/**
 * Component for registering a new doctor.
 * Includes form validation for email, password strength, and required fields.
 */
export function RegisterMedico({ onSuccess }: Props) {
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
    if (nome == "" || cognome == "") {
      setIsError("Inserisci nome e cognome");
      return
    }

    const body = {
      email,
      password,
      nome,
      cognome,
      dataNascita: dataNascita,

    };


    await axios
      .post(
        `${import.meta.env.VITE_API_KEY}api/auth/signup/medico`,
        body,
        { headers: { "Content-Type": "application/json" } }
      )
      .then(() => {

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
        error={isInvalid ? "email invalida" : null}
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
        placeholder="DD-MM-YYYY"
        valueFormat="DD-MM-YYYY"
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
        label="Contiene almeno 6 caratteri"
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
        placeholder="Conferma password"
        label="Conferma Password"
        required
        error={
          confirmPassword.length > 0 && confirmPassword !== password
            ? "le password non coincidono"
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
        Registra
      </Button>
    </Container>
  );
}
