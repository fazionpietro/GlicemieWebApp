import { useState } from "react";
import "./../AppPage/App.css";
import {
    Anchor,
    Button,
    Container,
    Group,
    Paper,
    PasswordInput,
    Text,
    Box,
    Center,
    Progress,
    TextInput,
    Title,
} from "@mantine/core";
import classes from "./AuthenticationTitle.module.css";
import emailcss from "./InvalidEmail.module.css";
import { IconAlertTriangle } from "@tabler/icons-react";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // semplice validazione email
    const isValidEmail = (value: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

    const isInvalid = email.length > 0 && !isValidEmail(email);
{

    return (
        <Container size={420} my={40}>
            <Title ta="center" className={classes.title}>
                Welcome!
            </Title>

            <Paper withBorder shadow="sm" p={22} mt={30} radius="md">
                <TextInput
                    label="Email"
                    value={email}
                    onChange={(e) => setEmail(e.currentTarget.value)}
                    required
                    error={isInvalid ? "Invalid email" : null}
                    placeholder="Email"
                    classNames={isInvalid ? { input: emailcss.invalid } : {}}
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

                <div style={{ textAlign: 'left' }}>
                    <PasswordInput
                        
                        value={password}
                        onChange={(event)=> setPassword(event.currentTarget.value)}
                        placeholder="Your password"
                        label="Password"
                        required
                        mt="md"
                    />
                </div>

                <Group justify="space-between" mt="lg">
                    <Anchor component="button" size="sm">
                        Forgot password?
                    </Anchor>
                </Group>

                <Button fullWidth mt="xl" radius="md">
                    Sign in
                </Button>
            </Paper>
        </Container>
    );
}
}

export default Login;
