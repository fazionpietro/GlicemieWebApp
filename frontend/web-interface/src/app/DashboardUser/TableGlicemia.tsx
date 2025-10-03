import { useAuth } from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Text } from '@mantine/core';
import '@mantine/core/styles.css';


type Rilevazione = {
  id: string;
  valore: number;
  timestamp: string;
  livello: string;
}

function TableGlicemia() {
  const { user } = useAuth();
  const [rilevazioni, setRilevazioni] = useState<Rilevazione[]>([]);

  useEffect(() => {
    if (!user) {
      return;
    }

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
      .then((res) => {
        res.data.forEach((item: Rilevazione, index: number) => {
        });
        setRilevazioni(res.data.sort((a: Rilevazione, b: Rilevazione) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()));
      })
      .catch((err) => {
        console.error("Errore nel caricamento rilevazioni:", err);
      });
  }, [user]);

  const getColor = (livello: string) => {
    switch (livello.toLowerCase().trim()) {
      case 'alto':
        return 'red';
      case 'basso':
        return 'yellow';
      case 'normale':
        return 'green';
    }
  }
  return (
    <div>
      {rilevazioni.slice(0, 5).map((r) => {
        const dataFormattata = new Date(r.timestamp).toLocaleDateString();
        const oraFormattata = new Date(r.timestamp).toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });

        return (
          <Box key={r.id} style={{
            marginBottom: '10px',
            textAlign: 'left',
            borderLeft: `3px solid ${getColor(r.livello)}`,
            paddingLeft: '10px',
            backgroundColor: 'rgba(0, 0, 0, 0.02)',
            borderRadius: '4px',
            padding: '8px'
          }}>
            <Text size="xs" c="dimmed">
              {dataFormattata}-{oraFormattata}
            </Text>
            <Text size="sm">
              <Text span fw={700} c={getColor(r.livello)}>
                {r.livello.toUpperCase()}
              </Text> - {r.valore} mg/dL;
            </Text>
          </Box>
        );
      })}
    </div>
  )
}

export default TableGlicemia;
