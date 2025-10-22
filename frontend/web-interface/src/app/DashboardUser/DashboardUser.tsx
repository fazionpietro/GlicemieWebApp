import { Grid, Card } from '@mantine/core';
import LineC from './LineChart';
import { ContactMedic, SegnalaSintomi } from './Contact';
import TableGlicemia from './TableGlicemia';
import Assunzioni from './Assunzioni';
import {useState, useEffect, useCallback} from 'react';
import { useAuth } from "../../context/AuthContext";
import axios, { AxiosError, type AxiosResponse } from 'axios';
import type { Terapia, Assunzione } from '../type/DataType';
import { HeaderMegaMenu } from "../Components/Header";

interface AssunzioneLogResponse{
  terapie?: Terapia[];
  assunzioni?: Assunzione[];
}

interface Assunzioni{
  id: string;
  idTerapia: string;
  latestTimeStamp: string;
  giaAssunte: number;
}

function DashboardUser() {
  const [terapie, setTerapie] = useState<Terapia[]>([]);
  const [assunzioni, setAssunzioni] = useState<Assunzione[]>([]);
  const { user } = useAuth();
  const [refresh, setRefresh]=useState(0);  

  const handleRilevazione=() =>{
    setRefresh(p=>p+ 1);
  }
  const refreshAssunzioni = useCallback(async ()=>{
    if(!user?.id)return;

    try{
        const response = await axios({
          method: "get",
          url: `${import.meta.env.VITE_API_KEY}api/assunzioni/log/${user.id}`,
          headers: {"Content-Type": "application/json"},
        });
        setAssunzioni(response.data);
    }catch(error){
      const axiosErrore = error as AxiosError<string>;
      console.error("errore", axiosErrore);
    }
  },[user?.id]);

  useEffect(() => {
    if(!user?.id) return;

    const fetchData = async() =>{
      try{
        const [terapieResponse, assunzioniResponse] = await Promise.all([
          axios({
          method: "get",
          url: `${import.meta.env.VITE_API_KEY}api/terapie/paziente/${user.id}`,
          headers: {"Content-Type": "application/json"},
        }),
        
        axios({
          method: "get",
          url: `${import.meta.env.VITE_API_KEY}api/assunzioni/log/${user.id}`,
          headers:{ "Content-type": "application/json"},
        })
      ]);
        
        setTerapie(terapieResponse.data);
        setAssunzioni(assunzioniResponse.data);
      }catch (error) {
        const axiosError = error as AxiosError<string>;
        console.error("Errore nell'inserimento della rilevazione:", axiosError);
      
        if (axiosError.response?.status === 400) {
          alert("dati non validi");
        } else if (axiosError.response?.status === 500) {
          alert("Errore del server. Riprova più tardi");
        } else {
          alert("Si è verificato un errore durante l'inserimento della rilevazione");
        }
      }
    }
    fetchData();
  },[user?.id]);

  return (
    <>
      <HeaderMegaMenu />
      <Grid columns={24} mb={100} align="stretch">
        <Grid.Col span={15}>
          <Card p="40" radius={"md"} h="100%" shadow='sm' w={"auto"}>
          <div style={{ height: "300px", maxHeight: "500px", marginBottom:"70px" }}>
            <LineC key={refresh} onRilevazione={handleRilevazione}/>
          </div>
          </Card>
        </Grid.Col>

        <Grid.Col span={9}>
          <Grid>
            <Grid.Col>
              <Card p="40" radius={"md"} h="100%" shadow='sm' w={"auto"}>
              <div style={{ height: "100%", maxHeight: "500px" }}>
                <TableGlicemia key={refresh} />
              </div>
              </Card>
            </Grid.Col>
          </Grid>
        </Grid.Col>
      </Grid>
      <Grid columns={24} mt={100}>
        <Grid.Col span={8}>
          <Card p="40" radius={"md"} shadow='sm' w={"auto"} style={{height: '450px', minHeight: '400px'}}>
          <SegnalaSintomi/>
          </Card>
        </Grid.Col>
        <Grid.Col span={8}>
          <Card p="40" radius={"md"} h="100%" shadow='sm' w={"auto"}>
          <Assunzioni terapie={terapie} assunzioni={assunzioni} refreshComponente={refreshAssunzioni}/>
          </Card>
        </Grid.Col>
        <Grid.Col span={8}>
          <Card p="40" radius={"md"} shadow='sm' w={"auto"} style={{height: '450px', minHeight: '400px'}}>
            <ContactMedic />
          </Card>
        </Grid.Col>
      </Grid>
    </>
  );
}

export default DashboardUser;
