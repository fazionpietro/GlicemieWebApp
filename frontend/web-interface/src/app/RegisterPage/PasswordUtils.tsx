import { Text, Group, Box } from "@mantine/core";
import { IconCheck, IconX } from "@tabler/icons-react";
import { requirements } from "./registerConstant";

export function PasswordRequirement({
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

export function getStrength(password: string) {
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
