import { useState } from "react";

import axios, { AxiosError, type AxiosResponse } from "axios";
import "./../AppPage/App.css";
import "./../CommonFile/App.css";

import {
    Anchor,
    Button,
    Container,
    Group,
    Paper,
    PasswordInput,
    TextInput,
    Title,
} from "@mantine/core";

import classes from "./AuthenticationTitle.module.css";
import emailcss from "./InvalidEmail.module.css";
import { IconAlertTriangle, IconAt } from "@tabler/icons-react";
import { useNavigate } from "react-router";
import { useAuth } from "../../context/Authentication";

interface User {
  id: string;
  email: string;
  role: string;
  token: string;
}



function Login() {
    const {login} = useAuth()
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isPasswordInvalid, setIsPasswordInvalid] = useState(false);

    // semplice validazione email
    const isValidEmail = (value: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

    const isEmailInvalid = email.length > 0 && !isValidEmail(email);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        await axios({
            method: "post",
            url: `${import.meta.env.VITE_API_KEY}api/auth/signin`,
            headers: {
                "Access-Control-Allow-Origin": "true",
                "Content-Type": "application/json",
            },
            data: {
                email: `${email}`,
                password: `${password}`,
            },
        }).then((res: AxiosResponse) => {
                let user: User = {
                    id: res.data.id,
                    email: email,
                    role: res.data.ruolo,
                    token: res.data.token
                }

                login(user)
                navigate("/");
                localStorage.setItem("access_token", res.data.token);
                console.log(res);
        }).catch((err: AxiosError<string>) => {
                setPassword("");
                setIsPasswordInvalid(true);
                console.error(err);
        });
    }

    return (
        <Container fluid w={600} my={400}>
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
                            error={isEmailInvalid ? "Invalid email" : null}
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
                                    ? "Invalid password"
                                    : null
                            }
                            required
                            mt="md"
                            size="md"
                        />
                    </div>

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
                        type={"submit"}
                    >
                        Sign in
                    </Button>
                </form>
            </Paper>
        </Container>
    );
}

export default Login;
