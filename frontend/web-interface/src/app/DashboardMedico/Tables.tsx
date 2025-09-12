import { Table } from '@mantine/core';
import { Menu, Button, Text } from '@mantine/core';
import { IconSettings } from '@tabler/icons-react';

const elements = [
    { name: "Franco", data: "12-09", ora: "12:30", valore: "250 mg/dl" },
    { name: "Luca", data: "11-09", ora: "14:30", valore: "180 mg/dl" },
    { name: "Marco", data: "10-09", ora: "16:30", valore: "90 mg/dl" },
]

function AlertTable() {
  const rows = elements.map((element) => (
    <Table.Tr key={element.name}>
      <Table.Td>{element.name}</Table.Td>
      <Table.Td>{element.data}</Table.Td>
      <Table.Td>{element.ora}</Table.Td>
      <Table.Td>{element.valore}</Table.Td>
      <Table.Td><ButtonMenu/></Table.Td>
    </Table.Tr>
  ));

  return (
    <Table>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Nome</Table.Th>
          <Table.Th>Data</Table.Th>
          <Table.Th>Ora</Table.Th>
          <Table.Th>Valore</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}

function ActivityTable() {
  const rows = elements.map((element) => (
    <Table.Tr key={element.name}>
      <Table.Td>{element.name}</Table.Td>
      <Table.Td>{element.data}</Table.Td>
      <Table.Td>{element.ora}</Table.Td>
      <Table.Td>{element.valore}</Table.Td>
    </Table.Tr>
  ));

  return (
    <Table>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>nome</Table.Th>
          <Table.Th>data</Table.Th>
          <Table.Th>ora</Table.Th>
          <Table.Th>valore</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}

function ButtonMenu(){
  return(
    <Menu shadow="md" width={200}>
      <Menu.Target>
        <Button>Gestisci</Button>
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Item leftSection={<IconSettings size={14} />}>Gestisci</Menu.Item>
        <Menu.Item leftSection={<IconSettings size={14} />}>Elimina</Menu.Item>
      </Menu.Dropdown>
    </Menu>
  )
}

export { AlertTable, ActivityTable };