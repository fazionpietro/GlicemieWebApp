import {
    ActionIcon,
    Anchor,
    Avatar,
    Badge,
    Button,
    Container,
    FloatingIndicator,
    Grid,
    Group,
    Paper,
    ScrollArea,
    Skeleton,
    Table,
    Text,
    UnstyledButton,
} from "@mantine/core";
import { IconPencil, IconTrash } from "@tabler/icons-react";
import type { Paziente } from "../type/DataType";
import { useState } from "react";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";

const element = [
    {
        avatar: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-1.png",
        name: "Robert Wolfkisser",
        job: "Engineer",
        email: "rob_wolf@gmail.com",
        phone: "+44 (452) 886 09 12",
    },
    {
        avatar: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-7.png",
        name: "Jill Jailbreaker",
        job: "Engineer",
        email: "jj@breaker.com",
        phone: "+44 (934) 777 12 76",
    },
    {
        avatar: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-2.png",
        name: "Henry Silkeater",
        job: "Designer",
        email: "henry@silkeater.io",
        phone: "+44 (901) 384 88 34",
    },
    {
        avatar: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-3.png",
        name: "Bill Horsefighter",
        job: "Designer",
        email: "bhorsefighter@gmail.com",
        phone: "+44 (667) 341 45 22",
    },
    {
        avatar: "https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-10.png",
        name: "Jeremy Footviewer",
        job: "Manager",
        email: "jeremy@foot.dev",
        phone: "+44 (881) 245 65 65",
    },
];

export default function TablePazienti() {
    const { user } = useAuth();
    const [data, setData] = useState<Paziente | null>(null);

    async function fetchData() {
        
        await axios({
            method: "GET",
            url: `${import.meta.env.VITE_API_KEY}api/pazienti/all`,
            headers: {
                "Access-Control-Allow-Origin": "true",
                "Content-Type": "application/json",
                withCredentials: true
            },
        })
            .then((res) => {
                console.log(res);
            })
            .catch((err) => {
                console.error(err);
            });
    }

    return (
        <Paper withBorder radius="md" style={{ overflow: "hidden" }}>
            <ScrollArea mah={"40vh"} type="always" offsetScrollbars>
                <Table
                    highlightOnHover
                    verticalSpacing="sm"
                    horizontalSpacing="md"
                    style={{
                        tableLayout: "fixed",
                        minWidth: "100%",
                    }}
                >
                    <Table.Thead>
                        <Table.Tr>
                            <Table.Th style={{ textAlign: "left" }}>
                                Employee
                            </Table.Th>
                            <Table.Th style={{ textAlign: "left" }}>
                                Email
                            </Table.Th>
                            <Table.Th style={{ textAlign: "left" }}>
                                Data di nascita
                            </Table.Th>
                            <Table.Th style={{ textAlign: "right" }}>
                                <Button variant="filled" onClick={fetchData}>
                                    Aggiungi paziente
                                </Button>
                            </Table.Th>
                        </Table.Tr>
                    </Table.Thead>
                    <Table.Tbody>
                        {element.map((item) => (
                            <Table.Tr key={item.name}>
                                <Table.Td style={{ textAlign: "left" }}>
                                    <Group gap="sm">
                                        <Text fz="sm" fw={500}>
                                            {item.name}
                                        </Text>
                                    </Group>
                                </Table.Td>

                                <Table.Td style={{ textAlign: "left" }}>
                                    <Anchor component="button" size="sm">
                                        {item.email}
                                    </Anchor>
                                </Table.Td>
                                <Table.Td style={{ textAlign: "left" }}>
                                    <Text fz="sm">{item.phone}</Text>
                                </Table.Td>
                                <Table.Td style={{ textAlign: "left" }}>
                                    <Group gap={0} justify="flex-end">
                                        <ActionIcon
                                            variant="subtle"
                                            color="gray"
                                        >
                                            <IconPencil
                                                size={16}
                                                stroke={1.5}
                                            />
                                        </ActionIcon>
                                        <ActionIcon
                                            variant="subtle"
                                            color="red"
                                        >
                                            <IconTrash size={16} stroke={1.5} />
                                        </ActionIcon>
                                    </Group>
                                </Table.Td>
                            </Table.Tr>
                        ))}
                    </Table.Tbody>
                </Table>
            </ScrollArea>
        </Paper>
    );
}
