// LoginPage/Login.tsx - Versione aggiornata
import { useState } from "react";
import axios, { AxiosError, type AxiosResponse } from "axios";
import {
    Anchor,
    Button,
    Container,
    Group,
    Paper,
    PasswordInput,
    TextInput,
    Title,
    Alert
} from "@mantine/core";
import classes from "../CommonFile/AuthenticationTitle.module.css";
import emailcss from "../CommonFile/InvalidEmail.module.css";
import { IconAlertTriangle, IconAt } from "@tabler/icons-react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import type { User } from "../type/DataType";

function Login() {
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isPasswordInvalid, setIsPasswordInvalid] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    // Ottieni la pagina da cui l'utente è stato reindirizzato
    const from = location.state?.from?.pathname || "/user";

    const isValidEmail = (value: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

    const isEmailInvalid = email.length > 0 && !isValidEmail(email);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setIsLoading(true);
        setErrorMessage("");
        setIsPasswordInvalid(false);

        try {
            const response: AxiosResponse = await axios({
                method: "post",
                url: `${import.meta.env.VITE_API_KEY}api/auth/signin`,
                headers: {
                    "Content-Type": "application/json",
                },
                data: {
                    email: email,
                    password: password,
                },
                withCredentials: true,
            });

            const user: User = {
                id: response.data.id,
                email: email,
                role: response.data.role,
            };

            // Salva l'utente nel contesto
            login(user);

            console.log("Login successful:", response.data);
            
            // Reindirizza alla pagina originale o alla dashboard
            navigate(from, { replace: true });

        } catch (error) {
            const axiosError = error as AxiosError<string>;
            console.error("Login error:", axiosError);
            
            setPassword("");
            setIsPasswordInvalid(true);
            
            // Gestisci diversi tipi di errore
            if (axiosError.response?.status === 401) {
                setErrorMessage("Email o password non corretti");
            } else if (axiosError.response?.status === 500) {
                setErrorMessage("Errore del server. Riprova più tardi");
            } else {
                setErrorMessage("Si è verificato un errore durante il login");
            }
        } finally {
            setIsLoading(false);
        }
    }

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
                

                <form onSubmit={handleSubmit} className="inputForm">
                    <div style={{ textAlign: "left" }}>
                        <TextInput
                            label="Email"
                            value={email}
                            onChange={(e) => setEmail(e.currentTarget.value)}
                            required
                            error={isEmailInvalid ? "Email non valida" : null}
                            placeholder="Email"
                            classNames={
                                isEmailInvalid
                                    ? { input: emailcss.invalid }
                                    : {}
                            }
                            size="md"
                            mb={40}
                            rightSection={
                                isEmailInvalid ? (
                                    <IconAlertTriangle
                                        stroke={1.5}
                                        size={18}
                                        className={emailcss.icon}
                                    />
                                ) : (
                                    <IconAt stroke={1.5} size={18} />
                                )
                            }
                        />
                    </div>

                    <div style={{ textAlign: "left" }}>
                        <PasswordInput
                            mb={40}
                            value={password}
                            onChange={(event) =>
                                setPassword(event.currentTarget.value)
                            }
                            placeholder="Your password"
                            label="Password"
                            error={
                                isPasswordInvalid && password.length <= 0
                                    ? "Password non valida"
                                    : null
                            }
                            required
                            mt="md"
                            size="md"
                        />
                    </div>
                    {errorMessage && (
                    <Alert
                        icon={<IconAlertTriangle size="1rem" />}
                        title="Errore"
                        color="red"
                        variant="light"
                        mb="md"
                        withCloseButton
                        onClose={() => setErrorMessage("")}
                    >
                        {errorMessage}
                    </Alert>
                )}

                    <Group justify="space-between" mt="lg">
                        <Anchor component="button" size="sm">
                            Forgot password?
                        </Anchor>
                    </Group>


                    <Button
                        size="md"
                        fullWidth
                        mt="xl"
                        radius="md"
                        mb={60}
                        type="submit"
                        loading={isLoading}
                        disabled={isEmailInvalid || !email || !password}
                    >
                        {isLoading ? "Accesso in corso..." : "Sign in"}
                    </Button>
                </form>
            </Paper>
        </Container>
    );
}

export default Login;