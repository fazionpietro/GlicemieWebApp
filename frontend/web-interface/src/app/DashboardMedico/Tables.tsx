import { ActionIcon, Group, Modal, Paper, ScrollArea, Table } from '@mantine/core';
import { Menu, Button, Text } from '@mantine/core';
import { ModalsProvider } from '@mantine/modals';
import { IconSettings, IconTrash } from '@tabler/icons-react';
import DetailsMedico from '../DashboardAdmin/DetailsMedico';
import { RegisterMedico } from '../DashboardAdmin/RegisterMedico';

const elements = [
  { name: "Franco", data: "12-09", ora: "12:30", valore: "250 mg/dl" },
  { name: "Luca", data: "11-09", ora: "14:30", valore: "180 mg/dl" },
  { name: "Marco", data: "10-09", ora: "16:30", valore: "90 mg/dl" },
]

function TableTerapie() {
  return (

    <>
      <ModalsProvider>

        <Paper
          radius="md"
          style={{ overflow: "hidden", height: "38vh" }} >
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

                    <Button variant="filled" w="80%">
                      Aggiungi Terapia
                    </Button>
                  </Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
              </Table.Tbody>
            </Table>
          </ScrollArea>
        </Paper>

      </ModalsProvider>




    </>

  )
}

export { TableTerapie };
