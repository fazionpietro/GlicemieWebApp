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
import type { Medico, Paziente } from "../type/DataType";
import { useEffect, useState } from "react";
import axios from "axios";

import { modals, ModalsProvider } from "@mantine/modals";
import RegisterPaziente from "./RegisterPaziente";
import { useDisclosure } from "@mantine/hooks";
import DetailsPaziente from "./DetailsPaziente";
import { RegisterMedico } from "./RegisterMedico";
import DetailsMedico from "./DetailsMedico";

type Props = {
  medici: Medico[] | null;
  fetchMedici: () => void;
};

export function TableMedici({ medici, fetchMedici }: Props) {
  const [opened, { open, close }] = useDisclosure(false);
  const [openedDel, { open: openDel, close: closeDel }] =
    useDisclosure(false);

  console.log(medici);
  console.log(fetchMedici);

  const handleDelete = async (id: string) => {
    console.log("Deleting patient with ID:", id);

    await axios({
      method: "DELETE",
      url: `${import.meta.env.VITE_API_KEY}api/utenti/delete/${id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {
        console.log(res);
        fetchMedici()
      })
      .catch((err) => {
        console.error(err);
      });
  };

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
      labels: {
        confirm: "Elimina paziente",
        cancel: "Cancella operazione",
      },
      confirmProps: { color: "red" },
      onCancel: () => modals.closeAll(),
      onConfirm: () => {
        handleDelete(id);
        modals.closeAll();
      },
    });



  return (
    <ModalsProvider>
      <Paper
        radius="md"
        style={{ overflow: "hidden", height: "30vh" }}
      >
        <ScrollArea style={{ height: "100%" }}>
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
                  Medico
                </Table.Th>
                <Table.Th style={{ textAlign: "left" }}>
                  Email
                </Table.Th>
                <Table.Th style={{ textAlign: "left" }}>
                  Data di nascita
                </Table.Th>
                <Table.Th style={{ textAlign: "right" }}>
                  <Modal
                    opened={opened}
                    centered
                    onClose={() => {
                      fetchMedici();
                      close();
                    }}
                    radius={"md"}
                    size="auto"
                  >
                    <RegisterMedico
                      onSuccess={() => {
                        fetchMedici();
                        close();
                      }}
                    />
                  </Modal>

                  <Button variant="filled" onClick={open} w="80%">
                    Aggiungi medico
                  </Button>
                </Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {medici?.map((item) => (
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
                        {`${item.email}`}
                      </Text>
                    </Group>
                  </Table.Td>
                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap="sm">
                      <Text fz="sm" fw={500}>
                        {`${item.dataNascita}`}
                      </Text>
                    </Group>
                  </Table.Td>
                  <Table.Td>
                    <Group gap={0} justify="flex-end">
                      <DetailsMedico
                        medico={item}
                        fetchMedici={fetchMedici}
                      />

                      <div>
                        <Modal
                          opened={openedDel}
                          onClose={closeDel}
                          radius={"md"}
                          centered
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
                      </div>


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
