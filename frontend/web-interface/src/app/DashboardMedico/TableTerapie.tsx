import {
  Group,
  Modal,
  Paper,
  Text,
  ScrollArea,
  Table,
  ActionIcon,
} from "@mantine/core";
import { Button } from "@mantine/core";
import { modals } from "@mantine/modals";

import { useDisclosure } from "@mantine/hooks";
import { RegisterTerapia } from "./RegisterTerapia";
import type { Paziente, Terapia } from "../type/DataType";
import { IconTrash } from "@tabler/icons-react";
import axios from "axios";
import DetailsTerapia from "./DetailsTerapia";



type Props = {
  terapie: Terapia[] | null;
  pazienti: Paziente[] | null;
  fetchTerapie: () => void;
  fetchPazienti: () => void;
};
/**
 * Component that displays a table of therapies.
 * Provides functionalities to view therapy details, add new therapies, and delete therapies.
 */
function TableTerapie({ pazienti, terapie, fetchTerapie }: Props) {
  const [openedTerapie, { open: openTerapie, close: closeTerapie }] =
    useDisclosure(false);

  const handleDelete = async (idTerapia: string) => {
    await axios({
      method: "DELETE",
      url: `${import.meta.env.VITE_API_KEY}api/terapie/delete/${idTerapia}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then(() => {
        fetchTerapie();
      })
      .catch((err) => {
        console.error(err);
      });
  };

  const openDeleteModal = (idTerapia: string) =>
    modals.openConfirmModal({
      title: "Elimina Terapia",
      centered: true,
      children: (
        <Text size="sm">
          Sei sicuro di voler eliminare questo utente? Quest'azione è
          distruttiva e nel caso di eliminazione accidentale dovrai contattare
          gli amministratori del sistema per ripristinare i dati.
        </Text>
      ),
      labels: {
        confirm: "Elimina terapia",
        cancel: "Cancella operazione",
      },
      confirmProps: { color: "red" },
      onCancel: () => modals.closeAll(),
      onConfirm: () => {
        handleDelete(idTerapia);
        modals.closeAll();
      },
    });

  return (
    <>
      <Paper radius="md" style={{ overflow: "hidden", height: "38vh" }}>
        <ScrollArea style={{ height: "100%" }}>
          <Table
            stickyHeader
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
                <Table.Th style={{ textAlign: "left" }}>Paziente</Table.Th>
                <Table.Th style={{ textAlign: "left" }}>Terapia</Table.Th>
                <Table.Th style={{ textAlign: "left" }}>Dosaggio</Table.Th>

                <Table.Th style={{ textAlign: "left" }}>
                  Assunzioni Giornaliere
                </Table.Th>
                <Table.Th style={{ textAlign: "right" }}>
                  <Modal
                    opened={openedTerapie}
                    centered
                    onClose={closeTerapie}
                    radius={"md"}
                    size="auto"
                  >
                    <RegisterTerapia
                      pazienti={pazienti}
                      fetchTerapie={fetchTerapie}
                      onSuccess={closeTerapie}
                    />
                  </Modal>

                  <Button variant="filled" w="100%" onClick={openTerapie}>
                    <Text fw={700} truncate="end">
                      Nuova Terapia
                    </Text>
                  </Button>
                </Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {terapie?.map((item) => (
                <Table.Tr key={item.id}>
                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap="sm">
                      <Text fz="sm" fw={500}>
                        {pazienti?.find((i) => i.id === item.idPaziente)?.nome +
                          " " +
                          pazienti?.find((i) => i.id === item.idPaziente)
                            ?.cognome}
                      </Text>
                    </Group>
                  </Table.Td>
                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap="sm">
                      <Text fz="sm" fw={500}>
                        {item.farmaco}
                      </Text>
                    </Group>
                  </Table.Td>
                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap="sm">
                      <Text fz="sm" fw={500}>
                        {item.dosaggio}
                      </Text>
                    </Group>
                  </Table.Td>

                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap="sm">
                      <Text fz="sm" fw={500}>
                        {item.numAssunzioni}
                      </Text>
                    </Group>
                  </Table.Td>
                  <Table.Td style={{ textAlign: "left" }}>
                    <Group gap={0} justify="flex-end">
                      <DetailsTerapia
                        terapia={item}
                        fetchTerapie={fetchTerapie}
                      />
                      <div>
                        <ActionIcon
                          variant="subtle"
                          color="red"
                          onClick={() => openDeleteModal(item.id)}
                        >
                          <IconTrash size={16} stroke={1.5} />
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
    </>
  );
}

export { TableTerapie };
