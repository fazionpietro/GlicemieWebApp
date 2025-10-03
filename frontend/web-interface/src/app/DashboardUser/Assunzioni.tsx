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
import {useState, useEffect } from 'react';

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


}


function Assunzioni({ terapie, assunzioni }: Props) {

  const [selezionaTerapia , setSelezionaTerapia] = useState<string[]>([]);
  const [refresh, setRefresh] = useState(0);

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
        setRefresh(c => c+1);
      }else{
        console.error("Errore HTTP", response.status);
      }
    }catch (error) {
      console.error("errore durante l'invio: ", error)
    }
  };

  return (
    <Box p="md" w="100%">
      <Title order={1} mb="lg">Terapia Attuale</Title>

      <Title order={2} mb="md">Farmaci in Terapia</Title>

      <Stack gap="lg" mb="xl">


        {terapie.map((terapia) => (
          

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
                  <Chip color="green" size="lg" radius="md" variant='light' icon={<LiaPillsSolid />} onClick={() => handleCheck(terapia.id)} >Assunto</Chip>

                </Flex>
                <Flex
                  mt={10} h="30%"
                  align={"self-end"}
                  justify="flex-end"
                >

                  {/*{console.log((assunzioni.filter(assunzioni => assunzioni.idTerapia === terapia.id).map(assunzione => assunzione.giaAssunto)))};*/}
                  {console.log((assunzioni.find(assunzione => assunzione.idTerapia === terapia.id)?.giaAssunte))}
                  {console.log("assunzioni: ",assunzioni)}
                  {console.log("assunzione.idTerapia: ", assunzioni.map(a=>a.idTerapia))}
                  {console.log("assunzione.giaAssunte: ", assunzioni.map(a=>a.giaAssunte))}
                  <Text>{(assunzioni.find(assunzione => assunzione.idTerapia === terapia.id)?.giaAssunte??0)}/{terapia.numAssunzioni}</Text>

                </Flex>


              </Grid.Col>
            </Grid>

          </Card>

        ))}
      </Stack>

      <Divider my="xl" />
      <Alert
        icon={<IconAlertTriangle size="1rem" />}
        title="Errore"
        color="red"
        variant="light"
        mb="md"
      >
        <Text mb={20} fw={500}>Non assumi farmaci da 2 giorni
        </Text>
      </Alert>

      <Button
        mt={20}
        leftSection={<IconReportMedical size={16} />}
        variant="filled"
        onClick={handleInviaReport}
      >
        Invia Report al Medico
      </Button>
    </Box >
  );
}

export default Assunzioni;
