import { Table } from '@mantine/core';
import {useAuth} from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';

type Rilevazione = {
  id:string;
  valore:number;
  timestamp: string;
  livello: string;
}

function TableGlicemia() {
  const {user}=useAuth();
  const [rilevazioni, setRilevazioni]= useState<Rilevazione[]>([]);

  useEffect(() => {
    if(!user) return;

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
    .then((res)=>{
      console.log(res.data)
      setRilevazioni(res.data.sort((a:Rilevazione,b:Rilevazione)=>new Date(b.timestamp).getTime()-new Date(a.timestamp).getTime()));
    })
    .catch((err)=>{
      console.error("Errore nel caricamento rilevazioni:", err);
    });
  },[user]);

  const rows = rilevazioni.map((r) => (
    <Table.Tr key={r.id}>
      <Table.Td style={{textAlign: 'left'}}>{new Date(r.timestamp).toLocaleDateString()}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}>{r.valore}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}>{r.livello}</Table.Td>
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