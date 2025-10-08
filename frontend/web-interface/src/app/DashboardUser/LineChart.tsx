
import { LineChart } from '@mantine/charts';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';
import ModalGlicemia from './Modal';
import { useAuth } from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Badge, Paper, Text } from '@mantine/core';

interface Props{
  onRilevazione?:() => void;
}

type Rilevazione = {
  id: string;
  valore: number;
  timestamp: string;
  livello: string;
}

type ChartData = {
  date: string;
  glicemia: number;
  livello: string;
  fullTimeStamp: string
}

function LineC({onRilevazione}: Props) {

  const { user } = useAuth();
  const [rilevazioni, setRilevazioni] = useState<Rilevazione[]>([]);
  const [forChartData, setForCharData] = useState<ChartData[]>([])

  useEffect(() => {
    if (!user) {
      return;
    }

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
      .then((res) => {
        const datiOrdinati = res.data.sort((a: Rilevazione, b: Rilevazione) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());


        setRilevazioni(datiOrdinati);

        const dataChart: ChartData[] = datiOrdinati.map((r: Rilevazione) => ({
          date: `${new Date(r.timestamp).toLocaleDateString('it-IT')} ${new Date(r.timestamp).toLocaleTimeString('it-IT')}`,
          glicemia: r.valore,
          livello: r.livello,
          fullTimeStamp: r.timestamp
        }))

        setForCharData(dataChart);

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
      case 'loading':
        return ''
    }
  }


  return (
    <div style={{ height: "100%", width: "100%" }}>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '20px',
      }}>
        <ModalGlicemia onRilevazione={onRilevazione}/>
      </div>

      {forChartData.length === 0 ? (
        <Paper withBorder shadow="md" p="x1" style={{height: "100%",display: "flex", flexDirection: "column",justifyContent:"center"
          , alignItems: "center", textAlign: "center"}} >
          <Text>
            Nessuna rilevazione trovata per il tuo account
            <br/>
            Aggiungi la tua prima rilevazione con il pulsante "Nuova Rilevazione"
          </Text>
        </Paper>
      ):(
        <LineChart
        h="100%"
        w="100%"
        data={forChartData.slice(0, 7).reverse()}
        dataKey="date"
        withPointLabels
        series={[
          { name: 'glicemia', color: 'blue' },
        ]}
        curveType="monotone"
        tickLine="x"
        gridAxis="x"
        gridProps={{ yAxisId: "left" }}
        referenceLines={[
          { y: 180, color: "yellow", label: 'Soglia massima', strokeDasharray: '5 5' },
          { y: 70, color: "yellow", label: 'Soglia minima', strokeDasharray: '5 5' }
        ]}
        yAxisProps={{
          domain: [50, 200],
        }}
        tooltipProps={{
          content: ({ payload, label }) => {
            if (payload && payload.length) {
              const data = payload[0].payload;
              return (
                <>
                  {payload.map((item: any) => (
                    <Paper px="md" py="sm" withBorder shadow="md" radius="md" ta="left">
                      <Text mb={5} fw={700}>
                        {label}:{new Date(data.fullTimeStamp).toLocaleTimeString('it-IT')}
                      </Text>
                      <Text mb={8} key={item.name}>
                        {item.name}: {item.value}
                      </Text>
                      <Badge size="sm" variant="filled" bg={getColor(data.livello)}>{data.livello} </Badge>
                    </Paper>
                  ))}
                </>
              );
            }
            return null;
          }
        }}
      />
      )}
    </div>
  );
}

export default LineC;

