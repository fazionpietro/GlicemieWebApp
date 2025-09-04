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
import { IconCheck, IconX } from "@tabler/icons-react";
import { useInputState } from "@mantine/hooks";

function Login() {
    const [email, setEmail] = useState("");

    // semplice validazione email
    const isValidEmail = (value: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);

    const isInvalid = email.length > 0 && !isValidEmail(email);

    function PasswordRequirement({
    meets,
    label,
}: {
    meets: boolean;
    label: string;
}) {
    return (
        <Text component="div" c={meets ? "teal" : "red"} mt={5} size="sm">
            <Group justify="flex-start" align="center"> 
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

    const requirements = [
        { re: /[0-9]/, label: "Includes number" },
        { re: /[a-z]/, label: "Includes lowercase letter" },
        { re: /[A-Z]/, label: "Includes uppercase letter" },
        { re: /[$&+,:;=?@#|'<>.^*()%!-]/, label: "Includes special symbol" },
    ];

    function getStrength(password: string) {
        let multiplier = password.length > 5 ? 0 : 1;

        requirements.forEach((requirement) => {
            if (!requirement.re.test(password)) {
                multiplier += 1;
            }
        });

        return Math.max(
            100 - (100 / (requirements.length + 1)) * multiplier,
            0
        );
    }

    const [value, setValue] = useInputState("");
    const strength = getStrength(value);
    const checks = requirements.map((requirement, index) => (
        <PasswordRequirement
        
            key={index}
            label={requirement.label}
            meets={requirement.re.test(value)}
        />
    ));
    const bars = Array(4)
        .fill(0)
        .map((_, index) => (
            <Progress
                styles={{ section: { transitionDuration: "0ms" } }}
                value={
                    value.length > 0 && index === 0
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

    return (
        <Container size={420} my={40}>
            <Title ta="center" className={classes.title}>
                Welcome back!
            </Title>
            <Text className={classes.subtitle}>
                Do not have an account yet? <Anchor>Create account</Anchor>
            </Text>

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
                        
                        value={value}
                        onChange={setValue}
                        placeholder="Your password"
                        label="Password"
                        required
                    />

                    <Group gap={5} grow mt="xs" mb="md">
                        {bars}
                    </Group>

                    <PasswordRequirement
                        
                        label="Has at least 6 characters"
                        meets={value.length > 5}
                    />
                    {checks}
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

export default Login;
