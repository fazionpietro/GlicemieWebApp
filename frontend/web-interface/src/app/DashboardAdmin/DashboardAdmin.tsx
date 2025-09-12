import {
  Container,
  Grid,
  Paper,
  Text,
  Title,
  ScrollArea,
  Card,
  Box,
  FloatingIndicator,
  UnstyledButton,
} from "@mantine/core";
import { HeaderMegaMenu } from "../CommonFile/Header";
import classes from "./StatsCard.module.css";
import { FiUsers, FiActivity, FiAlignRight } from "react-icons/fi";
import { FaUserMd } from "react-icons/fa";
import { useEffect, useState } from "react";
import axios from "axios";
import type { Medico, Paziente } from "../type/DataType";
import TablePazienti from "./TablePazienti";
import { TableMedici } from "./TableMedici";
import floatingcss from "./AdminFloatingIndicator.module.css";

type Log = {
  id: string;        // UUID
  tipo: string;
  descrizione: string;
  timestamp: string; // ISO-8601
};



const data = ["Gestione pazienti", "Gestione medici"];



function DashboardAdmin() {
  const [logs, setLogs] = useState<Log[]>([])

  const [rootRef, setRootRef] = useState<HTMLDivElement | null>(null);
  const [active, setActive] = useState(0);
  const [controlsRefs, setControlsRefs] = useState<
    Record<string, HTMLButtonElement | null>
  >({});
  const [didFetch, setDidFetch] = useState(false);
  const [ws, setWs] = useState<WebSocket | null>(null);

  const [pazienti, setPazienti] = useState<Paziente[] | null>(null);
  const [medici, setMedici] = useState<Medico[] | null>(null);

  const setControlRef = (index: number) => (node: HTMLButtonElement) => {
    controlsRefs[index] = node;
    setControlsRefs(controlsRefs);
  };

  const controls = data.map((item, index) => (
    <UnstyledButton
      key={item}
      className={floatingcss.control}
      ref={setControlRef(index)}
      onClick={() => setActive(index)}
      mod={{ active: active === index }}
    >
      <span className={floatingcss.controlLabel}>{item}</span>
    </UnstyledButton>
  ));

  async function fetchPazienti() {
    await axios({
      method: "GET",
      url: `${import.meta.env.VITE_API_KEY}api/pazienti/all`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {
        setPazienti(res.data);
        console.log(pazienti);
      })
      .catch((err) => {
        console.error(err);
      });
  }

  async function fetchMedici() {
    await axios({
      method: "GET",
      url: `${import.meta.env.VITE_API_KEY}api/utenti/medici/all`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {
        setMedici(res.data);
        console.log(medici);
      })
      .catch((err) => {
        console.error(err);
      });
  }

  useEffect(() => {
    if (!didFetch) {
      fetchPazienti();
      fetchMedici();
      setDidFetch(true);
    }
  }, []);




  useEffect(() => {
    const websocket = new WebSocket('ws://localhost:8080/ws/greet');
    setWs(websocket);

    websocket.onopen = () => console.log('Connected to WebSocket server');

    websocket.onmessage = (event) => {
      try {
        console.log('Received WebSocket message:', event.data);
        const newLog: Log[] = JSON.parse(event.data);
        setLogs(prevLogs => {
          if (JSON.stringify(prevLogs) !== JSON.stringify(newLog)) {
            return [...prevLogs, ...newLog].reverse();
          }
          return prevLogs;
        });
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    };
    websocket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    websocket.onclose = () => console.log('Disconnected from WebSocket server');

    return () => websocket.close();
  }, []);











  return (
    <div>
      <HeaderMegaMenu />
      <Container fluid my={40}>


        {/* Statistiche principali */}
        <Grid gutter="md" mb={70}>
          <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
            <Paper className={classes.stat} radius="md" shadow="md">
              <div className={classes.icon}>
                <FiUsers size={48} color="#4A90E2" />
              </div>
              <div>
                <Text className={classes.label}>
                  Utenti Totali
                </Text>
                <Box fz="lg" className={classes.count}>
                  <span className={classes.value}>127</span>
                  <Text size="sm" c="green" mt={5}>+12% dal mese scorso</Text>
                </Box>
              </div>
            </Paper>
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
            <Paper className={classes.stat} radius="md" shadow="md">
              <div className={classes.icon}>
                <FaUserMd size={48} color="#4ae293ff" />
              </div>
              <div>
                <Text className={classes.label}>
                  Medici Attivi
                </Text>
                <Box fz="lg" className={classes.count}>
                  <span className={classes.value}>23</span>
                  <Text size="sm" c="dimmed" mt={5}>Online negli ultimi 7 giorni</Text>
                </Box>
              </div>
            </Paper>
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
            <Paper className={classes.stat} radius="md" shadow="md">
              <div className={classes.icon}>
                <FiActivity size={48} color="#e2b74aff" />
              </div>
              <div>
                <Text className={classes.label}>
                  Rilevazioni/Giorno
                </Text>
                <Box fz="lg" className={classes.count}>
                  <span className={classes.value}>1,247</span>
                  <Text size="sm" c="dimmed" mt={5}>Media ultimi 30 giorni</Text>
                </Box>
              </div>
            </Paper>
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
            <Paper className={classes.stat} radius="md" shadow="md">
              <div className={classes.icon}>
                <FiAlignRight size={48} color="#704ae2ff" />
              </div>
              <div>
                <Text className={classes.label}>
                  Uptime Sistema
                </Text>
                <Box fz="lg" className={classes.count}>
                  <span className={classes.value}>99.9%</span>
                  <Text size="sm" c="dimmed" mt={5}>Ultimi 30 giorni</Text>
                </Box>
              </div>
            </Paper>
          </Grid.Col>
        </Grid>

        <Grid gutter="xl">
          {/* Sezione sinistra - Gestione Utenti e Tables */}
          <Grid.Col span={{ base: 12, lg: 8 }}>
            <Card shadow="sm" padding="lg" radius="md" withBorder mb="md">
              <Title order={3} mb="md">Gestione Utenti</Title>
              <Text size="sm" c="dimmed" mb="lg">
                Panoramica e gestione degli utenti del sistema
              </Text>

              <Grid>
                <Grid.Col span={4}>
                  <Box style={{ textAlign: 'center' }}>
                    <Text size="xl" fw={700}>89</Text>
                    <Text size="sm">Pazienti</Text>
                  </Box>
                </Grid.Col>
                <Grid.Col span={4}>
                  <Box style={{ textAlign: 'center' }}>
                    <Text size="xl" fw={700}>23</Text>
                    <Text size="sm">Medici</Text>
                  </Box>
                </Grid.Col>
                <Grid.Col span={4}>
                  <Box style={{ textAlign: 'center' }}>
                    <Text size="xl" fw={700}>3</Text>
                    <Text size="sm">Admin</Text>
                  </Box>
                </Grid.Col>
              </Grid>
              <Box
                className={floatingcss.root}
                ref={setRootRef}
                style={{ marginBottom: "20px" }}
                mt={45}
              >
                {controls}
                <FloatingIndicator
                  target={controlsRefs[active]}
                  parent={rootRef}
                  className={floatingcss.indicator}
                />
              </Box>
              {active == 0 ? (
                <Grid.Col span={12}>
                  <TablePazienti
                    pazienti={pazienti}
                    fetchPazienti={fetchPazienti}
                    medici={medici}
                    fetchMedici={fetchMedici}
                  ></TablePazienti>
                </Grid.Col>
              ) : (
                <Grid.Col span={12}>
                  <TableMedici
                    medici={medici}
                    fetchMedici={fetchMedici}
                  />
                </Grid.Col>
              )}

            </Card>


          </Grid.Col>

          {/* Sezione destra - Logs con testo allineato a sinistra */}
          <Grid.Col span={{ base: 12, lg: 4 }}>
            <Card shadow="sm" padding="lg" radius="md" withBorder style={{ height: '52vh' }}>
              <Title order={3} mb="md">Logs</Title>
              <ScrollArea h="100%">
                {logs.length === 0 ? (
                  <Text key="no-logs" size="sm" c="dimmed" style={{ textAlign: 'center', padding: '20px' }}>
                    Nessun log disponibile
                  </Text>
                ) : (
                  logs.map((item) => {
                    // Format the timestamp to a more readable format
                    const formattedTime = new Date(item.timestamp).toLocaleString();

                    return (
                      <Box key={item.id} style={{
                        marginBottom: '10px',
                        textAlign: 'left',
                        borderLeft: `3px solid ${item.tipo === 'INFO' ? '#4A90E2' :
                          item.tipo === 'WARN' ? '#e2b74aff' :
                            '#ff6b6b'
                          }`,
                        paddingLeft: '10px',
                        backgroundColor: 'rgba(0, 0, 0, 0.02)',
                        borderRadius: '4px',
                        padding: '8px'
                      }}>
                        <Text size="xs" c="dimmed">
                          {formattedTime}
                        </Text>
                        <Text size="sm">
                          <Text span fw={700} c={
                            item.tipo === 'INFO' ? 'blue' :
                              item.tipo === 'WARN' ? 'yellow' :
                                'red'
                          }>
                            {item.tipo}
                          </Text> - {item.descrizione}
                        </Text>
                      </Box>
                    );
                  })
                )}
              </ScrollArea>
            </Card>
          </Grid.Col>
        </Grid>
      </Container>
    </div >
  );
}

export default DashboardAdmin;
