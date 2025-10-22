import {
  Card,
  Text,
  Title,
  Group,
  Stack,
  Button,
  Divider,
  Box,
  Badge,
  Alert,
  Grid,
  Flex,
  Chip
} from '@mantine/core';
import { IconAlertTriangle, IconCheck, IconClock, IconReportMedical } from '@tabler/icons-react';
import { LiaPillsSolid } from "react-icons/lia";
import type { Assunzione } from '../type/DataType';
import {useState, useEffect, useMemo} from 'react';

interface Terapia {
  id: string;
  farmaco: string;
  numAssunzioni: number;
  dosaggio: string;
  indicazioni: string;
  idPaziente: string;
  medicoCurante: string;
}

interface Props {
  terapie: Terapia[],
  assunzioni: Assunzione[],
  refreshComponente: () =>void;
}


function Assunzioni({ terapie, assunzioni, refreshComponente}: Props) {

  const [selezionaTerapia , setSelezionaTerapia] = useState<string[]>([]);

  const handleCheck = (terapiaId: string) =>{
    setSelezionaTerapia(pri => pri.includes(terapiaId)?pri.filter(id => id !== terapiaId): [...pri, terapiaId]);
  };

  const handleInviaReport = async() => {
    if(selezionaTerapia.length === 0){
      return;
    }

    try{
      const assunzioneDaInviare = selezionaTerapia.map(id => ({idTerapia: id}));

      const response= await fetch(`${import.meta.env.VITE_API_KEY}api/assunzioni/store`, {
        method: 'POST',
        headers:{'Content-Type': 'application/json',},
        credentials: 'include',
        body:JSON.stringify(assunzioneDaInviare)
      });

      if(response.ok){
        setSelezionaTerapia([]);
        setTimeout(()=>{
          refreshComponente();
        }, 500);
      }else{
        console.error("Errore HTTP", response.status);
      }
    }catch (error) {
      console.error("errore durante l'invio: ", error)
    }
  };

  const noAssunzioni= useMemo(()=>{
    if (terapie.length === 0) return false;

    const giorniSA = Date.now() - (2*24*60*60*1000);
    const assunzioniMappate =new Map(assunzioni.map(a=> [a.idTerapia, a]));



    const noAssunzioneDG = terapie.some(terapia=>{
      const assunzione= assunzioniMappate.get(terapia.id);
      return !assunzione?.latestTimestamp || new Date(assunzione.latestTimestamp).getTime() < giorniSA;
    });

    if(noAssunzioneDG){
      return 'non assumi farmaci da almeno 2 giorni'
    }

    const noAssunzioneOggi = terapie.some (terapia=>{
      const assunzione= assunzioniMappate.get(terapia.id);
      const giorniSA = Date.now() - (2*24*60*60*1000);
      const giorno = Date.now() -(24*60*60*1000);
      
      if(!assunzione?.latestTimestamp) return true;

      const ultimaAssunzione =(new Date(assunzione.latestTimestamp).getTime())
      return  ultimaAssunzione >giorniSA && ultimaAssunzione <giorno;
    });

    if(noAssunzioneOggi){
      return 'ricordati di assumere i farmaci';
    }

    return false;
  },[terapie,assunzioni]);

  return (
    <Box p="md" w="100%">
      <Title order={1} mb="lg">Terapia Attuale</Title>

      <Title order={2} mb="md" style={{textAlign: 'center'}}>Farmaci in Terapia</Title>

      <Stack gap="lg" mb="xl">


        {terapie.map((terapia) => {
          
          const assunzioneTerapia=assunzioni.find(assunzione => assunzione.idTerapia === terapia.id)?.giaAssunte??0;
          const booleanAssunzione = assunzioneTerapia >=  terapia.numAssunzioni;


          return(
          <Card key={terapia.id} bdrs={"md"} withBorder shadow="sm" p="lg">
            <Grid>
              <Grid.Col span={7}>
                <Group mb="xs">
                  <Text size='md' fw={700}>{terapia.farmaco}</Text>
                  <Badge size="md" >{terapia.dosaggio}</Badge>
                </Group>


                <Group gap="xs" mb="xs" style={{ flexWrap: 'wrap' }}>
                  <IconClock size={16} />
                  <Text c="dimmed" size="sm">
                    {terapia.numAssunzioni} volt{terapia.numAssunzioni > 1 ? 'e' : 'a'} al giorno
                  </Text>
                  {terapia.indicazioni && (
                    <Text size="sm" style={{ width: '100%', wordBreak: 'break-word' }}>
                      {terapia.indicazioni}
                    </Text>
                  )}
                </Group>

              </Grid.Col>
              <Grid.Col span={5}>
                <Flex
                  h="70%"
                  justify="flex-end"
                  align="center"
                  pr={20}
                >
                  <Chip color="green" size="lg" radius="md" variant='light' icon={<LiaPillsSolid />}
                  checked={selezionaTerapia.includes(terapia.id)}
                  onClick={() => handleCheck(terapia.id)} disabled={booleanAssunzione}>
                  Assunto
                  </Chip>

                </Flex>
                <Flex
                  mt={10} h="30%"
                  align={"self-end"}
                  justify="flex-end"
                >
                    {booleanAssunzione ? (<Badge bg="green" size="sm" variant='filled'>Assunto</Badge>):
                  (<Text>{assunzioneTerapia}/{terapia.numAssunzioni}</Text>)}

                </Flex>

              </Grid.Col>
            </Grid>

          </Card>
          );

        })}
      </Stack>

      <Divider my="xl" />
      { noAssunzioni === 'non assumi farmaci da almeno 2 giorni' && (
        <Alert
        icon={<IconAlertTriangle size="1rem" />}
        title="Errore"
        color="red"
        variant="light"
        mb="md"
      >
        <Text mb={20} fw={500}>Non assumi farmaci da almeno 2 giorni
        </Text>
      </Alert>
      )}

      { noAssunzioni === 'ricordati di assumere i farmaci' && (
        <Alert
        icon={<IconAlertTriangle size="1rem" />}
        title="Errore"
        color="yellow"
        variant="light"
        mb="md"
      >
        <Text mb={20} fw={500}>ricordati di assumere i farmaci
        </Text>
      </Alert>
      )}

      <Button mt={20} variant="filled" onClick={handleInviaReport} w="100%">
        Invia Report al Medico
      </Button>
    </Box >
  );
}

export default Assunzioni;
