import { LineChart} from '@mantine/charts';
import { ReferenceLine } from 'recharts';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';
import { ModalGlicemia } from './Modal';
import {useAuth} from "../../context/AuthContext";
import { useState, useEffect } from 'react';
import axios from 'axios';

type Rilevazione = {
  id:string;
  valore:number;
  timestamp: string;
  livello: string;
}

type ChartData = {
  date: string;
  glicemia: number;
  livello: string;
  fullTimeStamp: string
}

function LineC() {
  
  const {user}=useAuth();
  const [rilevazioni, setRilevazioni]= useState<Rilevazione[]>([]);
  const [forChartData, setForCharData] = useState<ChartData[]>([])

  useEffect(() => {
    if(!user){
      console.log("nessun utente loggato");
      return;
    }

    axios.get(`${import.meta.env.VITE_API_KEY}api/rilevazioni/dto/${user.id}`, { withCredentials: true })
    .then((res)=>{
      const datiOrdinati = res.data.sort((a:Rilevazione,b:Rilevazione)=>new Date(b.timestamp).getTime()-new Date(a.timestamp).getTime());


      setRilevazioni(datiOrdinati);
      
      const dataChart: ChartData[] = datiOrdinati.map((r:Rilevazione)=>({
        date: new Date(r.timestamp).toLocaleDateString('it-IT'),
        glicemia: r.valore,
        livello: r.livello,
        fullTimeStamp:r.timestamp
      }))

      setForCharData(dataChart);

    })
    .catch((err)=>{
      console.error("Errore nel caricamento rilevazioni:", err);
    });
  },[user]);

  return (
    <div style={{ height: "100%", width: "100%" }}>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '20px',
        padding: '0 10px'
      }}>
        <ModalGlicemia />
      </div>
      <LineChart
        h="100%"
        w="100%"
        data={forChartData.slice(0, 7).reverse()}
        dataKey="date"
        withPointLabels
        series={[
          { name: 'glicemia', color: 'indigo.6' },
        ]}
        curveType="monotone"
        tickLine="x"
        gridAxis="x"
        gridProps={{ yAxisId:"left" }}
        referenceLines={[{ y: 180, color: "red", label: 'soglia massima' }, { y: 70, color: "red", label: 'soglia minima' }]}
        yAxisProps={{
          domain: [50, 200],
        }}
        tooltipProps={{
          content: ({payload, label})=>{
              if(payload && payload.length){
                const data = payload[0].payload;
                return (
                <div style={{padding: '10px', border: '1px solid #ccc', borderRadius: '4px' }}>
                  <p><strong>Data:</strong>{label}</p>
                  <p><strong>Ora:</strong>{new Date(data.fullTimeStamp).toLocaleTimeString('it-IT')}</p>
                  <p><strong>Livello:</strong>{data.livello}</p>
                </div>
                );
              }
            return null;
            }
        }}
      />
    </div>
  );
}

export default LineC;
