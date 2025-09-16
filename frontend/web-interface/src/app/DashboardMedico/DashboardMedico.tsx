import { Grid, Card, Skeleton, Title, useMantineTheme } from '@mantine/core';

import { HeaderMegaMenu } from '../Components/Header';

import { useMediaQuery } from "@mantine/hooks";
import { TableTerapie } from './TableTerapie.tsx';
import TablePazienti from '../Components/TablePazienti';
import type { Paziente, Terapia, User } from '../type/DataType';
import { useEffect, useState } from 'react';
import axios from 'axios';
const PRIMARY_COL_HEIGHT = '40vh';


function MedicPage() {
  const [user, setUser] = useState<User | null>(null)
  const theme = useMantineTheme();
  const isMobile = useMediaQuery(`(max-width: ${theme.breakpoints.sm})`);
  const [pazienti, setPazienti] = useState<Paziente[] | null>(null);
  const [didFetch, setDidFetch] = useState(false);
  const [terapie, setTerapie] = useState<Terapia[] | null>(null)

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
        console.log(res.data)
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
      console.log(res.data)
    }).catch((err) => {
      console.error(err)
    })
  }


  useEffect(() => {
    if (user === null) {

      setUser(JSON.parse(localStorage.getItem("user") ?? "{}"));
    }
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
          <Card p="40" radius={"md"} h="40vh" shadow='sm' w={"auto"}>
            <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>Gestione Terapie</Title>
            <TableTerapie pazienti={pazienti} fetchPazienti={fetchPazienti} terapie={terapie} fetchTerapie={fetchTerapie} />
          </Card>
        </Grid.Col>
        <Grid.Col mb={'5'} span={{ base: 12, lg: 4 }}><Skeleton height={PRIMARY_COL_HEIGHT} radius="md" animate={false} /></Grid.Col>

        <Grid.Col span={{ base: 12, lg: 8 }}>

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
        <Grid.Col span={{ base: 12, md: 4 }}><Skeleton height={PRIMARY_COL_HEIGHT} radius="md" animate={false} /></Grid.Col>
      </Grid>
    </div>
  );
}

export default MedicPage;
