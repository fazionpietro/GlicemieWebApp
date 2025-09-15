import { Table } from '@mantine/core';
import {useAuth} from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Pill } from '@mantine/core';
import '@mantine/core/styles.css';


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
    if(!user){
      console.log("nessun utente loggato");
      return;
    }

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
    .then((res)=>{
      console.log("risposta: ", res);
      console.log("dati: ", res.data);
      console.log("tipo di dati: ", Array.isArray(res.data) ? "array" : typeof res.data);
      res.data.forEach((item: Rilevazione, index: number) => {
        console.log(`Rilevazione ${index}: livello = "${item.livello}", tipo = ${typeof item.livello}`);
      });
      setRilevazioni(res.data.sort((a:Rilevazione,b:Rilevazione)=>new Date(b.timestamp).getTime()-new Date(a.timestamp).getTime()));
    })
    .catch((err)=>{
      console.error("Errore nel caricamento rilevazioni:", err);
    });
  },[user]);

  const getColor= (livello: string) => {
    switch(livello.toLowerCase().trim()){
      case 'alto':
        return 'red';
      case 'basso':
        return 'yellow';
      case 'normale':
        return 'green';
    }
  }

  const rows = rilevazioni.slice(0, 8).map((r) => (
    <Table.Tr key={r.id}>
      <Table.Td style={{textAlign: 'left'}}> {new Date(r.timestamp).toLocaleDateString()}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}> {new Date(r.timestamp).toLocaleTimeString('it-IT',{hour:'2-digit', minute:'2-digit'})}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}>{r.valore}</Table.Td>
      <Table.Td style={{textAlign: 'left'}}><Pill size="sm" bg={getColor(r.livello)}>{r.livello}</Pill></Table.Td>
    </Table.Tr>
  ));


  return (
    <Table>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Data</Table.Th>
          <Table.Th>Ora</Table.Th>
          <Table.Th>Rilevazioni</Table.Th>
          <Table.Th>Livello</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
}

export default TableGlicemia;