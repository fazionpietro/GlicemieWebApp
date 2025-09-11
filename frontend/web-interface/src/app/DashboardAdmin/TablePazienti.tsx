import {
    ActionIcon,
    Button,
    Group,
    Modal,
    Paper,
    ScrollArea,
    Table,
    Text,
} from "@mantine/core";
import { IconPencil, IconTrash } from "@tabler/icons-react";
import type { Paziente } from "../type/DataType";
import { useEffect, useState } from "react";
import axios from "axios";

import { modals, ModalsProvider } from "@mantine/modals";
import RegisterPaziente from "./RegisterPaziente";
import { useDisclosure } from "@mantine/hooks";

export default function TablePazienti() {
    const [elements, setElements] = useState<Paziente[] | null>(null);
    const [didFetch, setDidFetch] = useState(false);
    const [openedRegister, { open: openRegister, close: closeRegister }] =
        useDisclosure(false);
    const [openedDel, { open: openDel, close: closeDel }] =
        useDisclosure(false);

    async function fetchData() {
        await axios({
            method: "GET",
            url: `${import.meta.env.VITE_API_KEY}api/pazienti/all`,
            headers: {
                
                "Content-Type": "application/json",
                withCredentials: true,
            },
        })
            .then((res) => {
                setElements(res.data);
                console.log(elements);
                
            })
            .catch((err) => {
                console.error(err);
            });
    }


    




    const handleDelete = async (id: string) => {
        console.log("Deleting patient with ID:", id);
        
        await axios({
            method: "DELETE",
            url: `${import.meta.env.VITE_API_KEY}api/pazienti/delete`,
            headers: {
                "Content-Type": "application/json",
                withCredentials: true
            },
            data: {
                "id": `${id}`
            }
        }).then((res)=>{
            console.log(res);
            fetchData()
            

        }).catch((err)=>{
            console.error(err);
            
        })
    };

    useEffect(() => {
        if (!didFetch) {
            fetchData();
            setDidFetch(true);
        }
    }, []);

    const openDeleteModal = (id: string) =>
        modals.openConfirmModal({
            title: "Elimina Paziente",
            centered: true,
            children: (
                <Text size="sm">
                    Sei sicuro di voler eliminare questo utente? Quest'azione è
                    distruttiva e nel caso di eliminazione accidentale dovrai
                    contattare gli amministratori del sistema per ripristinare i
                    dati.
                </Text>
            ),
            labels: { confirm: "Elimina paziente", cancel: "Cancella operazione" },
            confirmProps: { color: "red" },
            onCancel: () => modals.closeAll(),
            onConfirm: () => {
                handleDelete(id);
                modals.closeAll();
            },
        });

    return (
        <ModalsProvider>
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
                                    Paziente
                                </Table.Th>
                                <Table.Th style={{ textAlign: "left" }}>
                                    Email
                                </Table.Th>
                                <Table.Th style={{ textAlign: "left" }}>
                                    Data di nascita
                                </Table.Th>
                                <Table.Th style={{ textAlign: "right" }}>
                                    <Modal
                                        opened={openedRegister}
                                        onClose={()=>{
                                            fetchData();
                                            closeRegister();
                                            
                                        }}
                                        radius={"md"}
                                        size="auto"
                                    >
                                        <RegisterPaziente onSuccess={() => {
                                            fetchData();
                                            closeRegister();
                                        }}/>
                                    </Modal>

                                    <Button
                                        variant="filled"
                                        onClick={openRegister}
                                    >
                                        Aggiungi paziente
                                    </Button>
                                </Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {elements?.map((item) => (
                                <Table.Tr key={item.id}>
                                    <Table.Td style={{ textAlign: "left" }}>
                                        <Group gap="sm">
                                            <Text fz="sm" fw={500}>
                                                {`${item.nome} ${item.cognome}`}
                                            </Text>
                                        </Group>
                                    </Table.Td>
                                    <Table.Td style={{ textAlign: "left" }}>
                                        <Group gap="sm">
                                            <Text fz="sm" fw={500}>
                                                {item.email}
                                            </Text>
                                        </Group>
                                    </Table.Td>
                                    <Table.Td style={{ textAlign: "left" }}>
                                        <Group gap="sm">
                                            <Text fz="sm" fw={500}>
                                                {item.dataNascita}
                                            </Text>
                                        </Group>
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

                                            <Modal
                                                opened={openedDel}
                                                onClose={closeDel}
                                                radius={"md"}
                                            ></Modal>

                                            <ActionIcon
                                                
                                                variant="subtle"
                                                color="red"
                                                onClick={() =>
                                                    openDeleteModal(item.id)
                                                }
                                            >
                                                <IconTrash
                                                    size={16}
                                                    stroke={1.5}
                                                />
                                            </ActionIcon>
                                        </Group>
                                    </Table.Td>
                                </Table.Tr>
                            ))}
                        </Table.Tbody>
                    </Table>
                </ScrollArea>
            </Paper>
        </ModalsProvider>
    );
}
