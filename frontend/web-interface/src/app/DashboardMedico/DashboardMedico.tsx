import { Grid, Card, Skeleton, Title, useMantineTheme } from '@mantine/core';

import { HeaderMegaMenu } from '../Components/Header';

import { useMediaQuery } from "@mantine/hooks";
import { TableTerapie } from './TableTerapie.tsx';
import TablePazienti from '../Components/TablePazienti';
import type { Paziente, Terapia } from '../type/DataType';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext.tsx';
import { LogMessaggi } from './LogMessaggi.tsx';
const PRIMARY_COL_HEIGHT = '40vh';


function MedicPage() {
  const theme = useMantineTheme();
  const isMobile = useMediaQuery(`(max-width: ${theme.breakpoints.sm})`);
  const [pazienti, setPazienti] = useState<Paziente[] | null>(null);
  const [didFetch, setDidFetch] = useState(false);
  const [terapie, setTerapie] = useState<Terapia[] | null>(null)
  const { user } = useAuth()

  async function fetchPazienti() {
    await axios({
      method: "GET",
      url: `${import.meta.env.VITE_API_KEY}api/pazienti/my/${user?.id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {

        setPazienti(res.data)
      })
      .catch((err) => {
        console.error(err);
      });


  }

  async function fetchTerapie() {
    await axios({
      method: "GEt",
      url: `${import.meta.env.VITE_API_KEY}api/terapie/medico/${user?.id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true
      }
    }).then((res) => {

      setTerapie(res.data)
    }).catch((err) => {
      console.error(err)
    })
  }


  useEffect(() => {

    if (!didFetch && user !== null) {

      fetchTerapie()
      fetchPazienti();
      setDidFetch(true);
    }
  }, [user]);


  return (
    <div>
      <HeaderMegaMenu />

      <Grid gutter={isMobile ? "md" : "xl"} ta={"left"} align="flex-start">

        <Grid.Col span={{ base: 12, lg: 8 }}>
          <Grid.Col span={12}>
            <Card p="40" radius={"md"} h="40vh" shadow='sm' w={"auto"}>
              <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>Gestione Terapie</Title>
              <TableTerapie pazienti={pazienti} fetchPazienti={fetchPazienti} terapie={terapie} fetchTerapie={fetchTerapie} />
            </Card>
          </Grid.Col>

          <Grid.Col span={12}>

            <Card p="40" radius={"md"} h="40vh" shadow='sm' w={"auto"}>
              <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>Pazienti</Title>
              <TablePazienti
                pazienti={pazienti}
                medici={null}
                fetchMedici={() => console.log("")}
                fetchPazienti={fetchPazienti}


              ></TablePazienti>
            </Card>

          </Grid.Col>
        </Grid.Col>
        <Grid.Col mt={15} span={{ base: 12, md: 4 }}>

          <Card radius={'md'} h={'82vh'}>

            <LogMessaggi />
          </Card>


        </Grid.Col>
      </Grid>
    </div>
  );
}

export default MedicPage;
