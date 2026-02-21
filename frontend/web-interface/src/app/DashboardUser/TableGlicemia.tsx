import { useAuth } from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Box, Text, Paper } from '@mantine/core';
import '@mantine/core/styles.css';



type Rilevazione = {
  id: string;
  valore: number;
  timestamp: string;
  livello: string;
  dopoPasto: boolean;
}

/**
 * Component that lists recent glucose readings.
 * Displays values with color-coded status indicators.
 */
function TableGlicemia() {
  const { user } = useAuth();
  const [rilevazioni, setRilevazioni] = useState<Rilevazione[]>([]);

  useEffect(() => {
    if (!user) {
      return;
    }

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
      .then((res) => {

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
        return 'red';
      case 'normale':
        return 'green';
    }
  }
  return (
    <div>
      {rilevazioni.length === 0 ? (
        <Paper withBorder shadow="md" p="xl" style={{
          height: "100%", display: "flex", flexDirection: "column", justifyContent: "center"
          , alignItems: "center", textAlign: "center"
        }} >
          <Text>
            Non sono state trovate delle rilevazioni per il tuo account
            <br />
            Aggiungi la tua prima rilevazione con il pulsante "Nuova Rilevazione"
          </Text>
        </Paper>
      ) : (
        rilevazioni.slice(0, 5).map((r) => {
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
                </Text> - {r.valore} mg/dL{r.dopoPasto ? ' (dopo pasto)' : ''}
              </Text>
            </Box>
          );
        })
      )}
    </div>
  )
}

export default TableGlicemia;
