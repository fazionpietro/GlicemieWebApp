import { useState } from "react";
import {
    Button,
    Container,
    Group,
    Paper,
    PasswordInput,
    Text,
    Box,
    Progress,
    TextInput,
    Title,
    UnstyledButton,
    FloatingIndicator,
    Modal,
    Textarea,
} from "@mantine/core";
import { ModalsProvider } from "@mantine/modals";
import "../CommonFile/App.css";
import classes from "../CommonFile/AuthenticationTitle.module.css";
import emailcss from "../CommonFile/InvalidEmail.module.css";
import floatingcss from "./FloatingIndicator.module.css";
import { IconAlertTriangle } from "@tabler/icons-react";
import { IconCheck, IconX } from "@tabler/icons-react";
import { useDisclosure, useInputState } from "@mantine/hooks";
import { useAuth } from "../../context/Authentication";
import { DateInput, DatePickerInput, DatesProvider, type DateValue } from "@mantine/dates";
import "@mantine/dates/styles.css";
import axios from "axios";
import type { User } from "../type/User";
import { requirements, data } from "./registerConstant";
import { PasswordRequirement, getStrength } from "./PasswordUtils";
import { PatientInfoModal } from "./PatientInfoModal";

function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useInputState("");
    const [nome, setNome] = useState("");
    const [cognome, setCognome] = useState("");
    const [dataNascita, setDataNascita] = useState<DateValue>("");
    const [opened, { open, close }] = useDisclosure(false);
    const { login } = useAuth();
    
    // Variabili finali che vengono salvate
    const [fattoriRischio, setFattoriRischio] = useState("");
    const [comorbita, setComorbita] = useState("");
    const [patologiePregresse, setPatologiePregresse] = useState("");
    
    // Variabili temporanee per il modal
    const [tempFattoriRischio, setTempFattoriRischio] = useState("");
    const [tempComorbita, setTempComorbita] = useState("");
    const [tempPatologiePregresse, setTempPatologiePregresse] = useState("");

    const [rootRef, setRootRef] = useState<HTMLDivElement | null>(null);
    const [controlsRefs, setControlsRefs] = useState<
        Record<string, HTMLButtonElement | null>
    >({});
    const [active, setActive] = useState(0);
    const [confirmPassword, setConfirmPassword] = useState("");
    const strength = getStrength(password);
    
    const checks = requirements.map((requirement, index) => (
        <PasswordRequirement
            key={index}
            label={requirement.label}
            meets={requirement.re.test(password)}
        />
    ));
    
    const bars = Array(4)
        .fill(0)
        .map((_, index) => (
            <Progress
                styles={{ section: { transitionDuration: "0ms" } }}
                value={
                    password.length > 0 && index === 0
                        ? 100
                        : strength >= ((index + 1) / 4) * 100
                        ? 100
                        : 0
                }
                color={
                    strength > 80 ? "teal" : strength > 50 ? "yellow" : "red"
                }
                key={index}
                size={4}
            />
        ));

    // Funzione per aprire il modal e inizializzare i valori temporanei
    const handleOpenModal = () => {
        setTempFattoriRischio(fattoriRischio);
        setTempComorbita(comorbita);
        setTempPatologiePregresse(patologiePregresse);
        open();
    };

    // Funzione per chiudere il modal senza salvare
    const handleCloseModal = () => {
        // Reset dei valori temporanei
        setTempFattoriRischio("");
        setTempComorbita("");
        setTempPatologiePregresse("");
        close();
    };

    // Funzione per salvare e chiudere il modal
    const handleSaveAndClose = () => {
        setFattoriRischio(tempFattoriRischio);
        setComorbita(tempComorbita);
        setPatologiePregresse(tempPatologiePregresse);
        close();
    };

    async function handleRegister() {
        let body;
        if (active == 1) {
            body = {
                email: `${email}`,
                password: `${password}`,
                nome: `${nome}`,
                cognome: `${cognome}`,
                dataNascita: `${dataNascita?.toString()}`,
            };
        } else {
            body = {
                email: `${email}`,
                password: `${password}`,
                nome: `${nome}`,
                cognome: `${cognome}`,
                dataNascita: `${dataNascita?.toString()}`,
                // Includi i dati aggiuntivi se necessario
                fattoriRischio: `${fattoriRischio}`,
                comorbita: `${comorbita}`,
                patologiePregresse: `${patologiePregresse}`,
            };
        } 
        console.log(body);
        
        await axios
            .post(
                `${import.meta.env.VITE_API_KEY}api/auth/signup/${data[
                    active
                ].toLowerCase()}`,
                JSON.stringify(body),
                {
                    headers: {
                        "Access-Control-Allow-Origin": "true",
                        "Content-Type": "application/json",
                    },
                }
            )
            .then((res) => {
                let user: User = {
                    id: res.data.id,
                    email: email,
                    role: res.data.ruolo,
                    token: res.data.token,
                };
                login(user);
            })
            .catch((err) => {
                setPassword("");
                setConfirmPassword("");
                console.error(err);
            });
    }

    const setControlRef = (index: number) => (node: HTMLButtonElement) => {
        controlsRefs[index] = node;
        setControlsRefs(controlsRefs);
    };

    const controls = data.map((item, index) => (
        <UnstyledButton
            bdrs={7}
            key={item}
            className={floatingcss.control}
            ref={setControlRef(index)}
            onClick={() => setActive(index)}
            mod={{ active: active === index }}
        >
            <span className={floatingcss.controlLabel}>{item}</span>
        </UnstyledButton>
    ));

    const isValidEmail = (value: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

    const isInvalid = email.length > 0 && !isValidEmail(email);

    return (
        <Container fluid w={600} my={40}>
            <Title ta="center" className={classes.title}>
                Welcome!
            </Title>

            <Paper
                withBorder
                shadow="md"
                p={60}
                mt={60}
                radius="lg"
                style={{ width: "100%", margin: "0 auto" }}
            >
                <div className={floatingcss.root} ref={setRootRef}>
                    {controls}

                    <FloatingIndicator
                        bdrs={6}
                        target={controlsRefs[active]}
                        parent={rootRef}
                        className={floatingcss.indicator}
                    />
                </div>
                <div style={{ textAlign: "left" }}>
                    <TextInput
                        size="md"
                        radius={"md"}
                        mb={20}
                        label="Email"
                        value={email}
                        onChange={(e) => setEmail(e.currentTarget.value)}
                        required
                        error={isInvalid ? "Invalid email" : null}
                        placeholder="Email"
                        classNames={
                            isInvalid ? { input: emailcss.invalid } : {}
                        }
                        rightSection={
                            isInvalid ? (
                                <IconAlertTriangle
                                    stroke={1.5}
                                    size={18}
                                    className={emailcss.icon}
                                />
                            ) : null
                        }
                    />
                    <div>
                        <TextInput
                            style={{ width: "48%", float: "left" }}
                            size="md"
                            radius={"md"}
                            label="Nome"
                            withAsterisk
                            placeholder="Nome"
                            mb={20}
                            onChange={(e) => setNome(e.currentTarget.value)}
                        />
                        <TextInput
                            style={{ width: "48%", float: "right" }}
                            size="md"
                            radius={"md"}
                            label="Cognome"
                            withAsterisk
                            placeholder="Cognome"
                            mb={20}
                            onChange={(e) => setCognome(e.currentTarget.value)}
                        />
                    </div>
                    <div>
                        <DatesProvider settings={{ consistentWeeks: true }}>
                            <DateInput
                                mb={20}
                                size="md"
                                radius={"md"}
                                label="Data di nascita"
                                placeholder="Data di nascita"
                                valueFormat="YYYY-MM-DD"
                                withAsterisk
                                value={dataNascita}
                                onChange={setDataNascita}
                            />
                        </DatesProvider>
                    </div>

                    <div style={{ textAlign: "left" }}>
                        <PasswordInput
                            size="md"
                            radius={"md"}
                            mb={20}
                            value={password}
                            onChange={(event) =>
                                setPassword(event.currentTarget.value)
                            }
                            placeholder="Your password"
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
                            size="md"
                            radius={"md"}
                            mb={35}
                            value={confirmPassword}
                            onChange={(event) =>
                                setConfirmPassword(event.currentTarget.value)
                            }
                            placeholder="Confirm password"
                            label="Confirm Password"
                            required
                            mt="md"
                            error={
                                confirmPassword.length > 0 &&
                                confirmPassword !== password
                                    ? "Passwords do not match"
                                    : null
                            }
                        />

                        <div>
                            <ModalsProvider>
                                <PatientInfoModal
                                    opened={opened}
                                    onClose={handleCloseModal}
                                    onSave={handleSaveAndClose}
                                    tempFattoriRischio={tempFattoriRischio}
                                    setTempFattoriRischio={setTempFattoriRischio}
                                    tempComorbita={tempComorbita}
                                    setTempComorbita={setTempComorbita}
                                    tempPatologiePregresse={tempPatologiePregresse}
                                    setTempPatologiePregresse={setTempPatologiePregresse}
                                />
                                
                                <Button
                                    disabled={active == 1}
                                    size="sm"
                                    radius={"mb"}
                                    mb={60}
                                    variant="filled"
                                    onClick={handleOpenModal}
                                >
                                    informazioni aggiuntive
                                </Button>
                            </ModalsProvider>
                        </div>
                    </div>

                    <Button
                        size="md"
                        fullWidth
                        mt="xl"
                        radius="md"
                        onClick={handleRegister}
                    >
                        Register
                    </Button>
                </div>
            </Paper>
        </Container>
    );
}

export default Register;