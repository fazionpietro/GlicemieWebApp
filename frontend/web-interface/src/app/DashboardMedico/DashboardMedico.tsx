import { Grid, Card, Skeleton, Title, useMantineTheme } from '@mantine/core';
import classes from "../CommonFile/StatsCard.module.css";
import { Paper, Text } from "@mantine/core";
import { IoIosAlert } from "react-icons/io";
import { TbActivityHeartbeat } from "react-icons/tb";
import { HiMiniArrowTrendingUp } from "react-icons/hi2";
import { FiUsers } from "react-icons/fi";
import { HeaderMegaMenu } from '../CommonFile/Header';

import { useMediaQuery } from "@mantine/hooks";
import { TableTerapie } from './Tables';
import TablePazienti from '../DashboardAdmin/TablePazienti';
import type { Paziente, PazienteMedico, Terapia, User } from '../type/DataType';
import { useEffect, useState } from 'react';
import axios from 'axios';
const PRIMARY_COL_HEIGHT = '45vh';


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
      <Grid gutter="md" mb={100}>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <FiUsers size={48} color="#4A90E2" />
            </div>
            <div>
              <Text className={classes.label}>
                Pazienti Totali
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>150</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <IoIosAlert size={48} color="#4ae293ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Alert Attivi
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>130</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <TbActivityHeartbeat size={48} color="#e2b74aff" />
            </div>
            <div>
              <Text className={classes.label}>
                Rilevazioni Oggi
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>70%</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <HiMiniArrowTrendingUp size={48} color="#704ae2ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Media Controlli
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>5</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
      </Grid>

      <Grid gutter={isMobile ? "md" : "xl"} ta={"left"} align="flex-start">

        <Grid.Col span={{ base: 12, md: 8 }}>
          <Card p="40" radius={"md"} h="45vh" shadow='sm' w={"auto"}>
            <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>Gestione Terapie</Title>
            <TableTerapie pazienti={pazienti} fetchPazienti={fetchPazienti} terapie={terapie} fetchTerapie={fetchTerapie} />
          </Card>
        </Grid.Col>
        <Grid.Col mb={'5'} span={{ base: 12, md: 4 }}><Skeleton height={PRIMARY_COL_HEIGHT} radius="md" animate={false} /></Grid.Col>

        <Grid.Col span={{ base: 12, md: 8 }}>

          <Card p="40" radius={"md"} h="45vh" shadow='sm' w={"auto"}>
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
