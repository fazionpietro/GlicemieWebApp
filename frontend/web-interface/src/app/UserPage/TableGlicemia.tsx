import { Table } from '@mantine/core';

const elements=[
  {date: '01-01', Rilevazioni:60, livello:"basso"},
  {date: '01-02', Rilevazioni:150, livello:"normale"},
  {date: '01-03', Rilevazioni:170, livello:"normale"},
  {date: '01-04', Rilevazioni:90, livello:"normale"},
  {date: '01-05', Rilevazioni:70, livello:"basso"}
];

function TableGlicemia() {
  const rows = elements.map((element) => (
    <Table.Tr key={element.date}>
      <Table.Td style={{textAlign: 'left'}}>{element.date}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}>{element.Rilevazioni}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}>{element.livello}</Table.Td>
    </Table.Tr>
  ));

  return (
    <Table>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Data</Table.Th>
          <Table.Th>Rilevazioni</Table.Th>
          <Table.Th>Livello</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}

export default TableGlicemia;