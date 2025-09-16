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
  useMantineTheme,
} from "@mantine/core";
import { HeaderMegaMenu } from "../Components/Header";
import classes from "../Components/StatsCard.module.css";
import { FiUsers, FiActivity, FiAlignRight } from "react-icons/fi";
import { FaUserMd } from "react-icons/fa";
import { useEffect, useState } from "react";
import axios from "axios";
import type { Medico, Paziente } from "../type/DataType";
import TablePazienti from "../Components/TablePazienti";
import { TableMedici } from "./TableMedici";
import floatingcss from "./AdminFloatingIndicator.module.css";
import { useMediaQuery } from "@mantine/hooks";


type Log = {
  id: string;        // UUID
  tipo: string;
  descrizione: string;
  timestamp: string; // ISO-8601
};



const data = ["Gestione pazienti", "Gestione medici"];



function DashboardAdmin() {
  const theme = useMantineTheme();
  const isMobile = useMediaQuery(`(max-width: ${theme.breakpoints.sm})`);
  const [logs, setLogs] = useState<Log[]>([])

  const [rootRef, setRootRef] = useState<HTMLDivElement | null>(null);
  const [active, setActive] = useState(0);
  const [controlsRefs, setControlsRefs] = useState<
    Record<string, HTMLButtonElement | null>
  >({});
  const [didFetch, setDidFetch] = useState(false);
  const [_ws, setWs] = useState<WebSocket | null>(null);

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
    const websocket = new WebSocket('ws://localhost:8080/ws/logs');
    setWs(websocket);

    websocket.onopen = () => console.log('Connected to WebSocket server');

    websocket.onmessage = (event) => {
      try {
        const newLogs: Log[] = JSON.parse(event.data);

        setLogs(prevLogs => {
          const logMap = new Map();

          prevLogs.forEach(log => logMap.set(log.id, log));

          newLogs.forEach(log => logMap.set(log.id, log));

          const uniqueLogs = Array.from(logMap.values()).sort((a, b) =>
            new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
          );

          return uniqueLogs;
        });
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    };
    websocket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    websocket.onclose = () => console.log('Disconnected from WebSocket server');

    // Gestione della chiusura della pagina/componente
    const handleBeforeUnload = () => {
      if (websocket.readyState === WebSocket.OPEN) {
        websocket.close();
      }
    };

    window.addEventListener('beforeunload', handleBeforeUnload);

    // Cleanup function
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);

      if (websocket.readyState === WebSocket.OPEN) {
        websocket.close();
      }
    };
  }, []);










  return (
    <div>
      <HeaderMegaMenu />
      <Title ta={"left"} mb="8vh" fz="4rem">Dashboard Admin</Title>
      <Container fluid p={isMobile ? "xs" : "md"} my={{ base: 20, md: 40 }}>

        <Grid gutter={isMobile ? "md" : "xl"} align="flex-start" ta={"left"}>

          {/* Sezione sinistra - Gestione Utenti e Tables */}
          <Grid.Col span={{ base: 12, lg: 8 }}
          >
            <Card shadow="sm" padding={"40"} radius="md" withBorder mb="md" h={isMobile ? "400px" : "55vh"}>
              <Title size={"2rem"} order={isMobile ? 4 : 3} mb="md">Gestione Utenti</Title>


              <Box
                className={floatingcss.root}
                ref={setRootRef}
                style={{ marginBottom: "20px" }}
                mt={isMobile ? "md" : 45}
              >
                {controls}
                <FloatingIndicator
                  target={controlsRefs[active]}
                  parent={rootRef}
                  className={floatingcss.indicator}
                />
              </Box>

              {active == 0 ? (
                <TablePazienti
                  pazienti={pazienti}
                  fetchPazienti={fetchPazienti}
                  medici={medici}
                  fetchMedici={fetchMedici}
                />
              ) : (
                <TableMedici
                  medici={medici}
                  fetchMedici={fetchMedici}
                />
              )}
            </Card>
          </Grid.Col>

          {/* Sezione destra - Logs */}
          <Grid.Col span={{ base: 12, lg: 4 }}>
            <Card
              shadow="sm"
              padding={isMobile ? "md" : "lg"}
              radius="md"
              withBorder
              h={isMobile ? "400px" : "55vh"}
            >
              <Title order={isMobile ? 4 : 3} mb="md">Logs</Title>
              <ScrollArea h="100%">
                {logs.length === 0 ? (
                  <Text key="no-logs" size="sm" c="dimmed" style={{ textAlign: 'center', padding: '20px' }}>
                    Nessun log disponibile
                  </Text>
                ) : (
                  logs.map((item) => {
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
    </div>
  );
}

export default DashboardAdmin;
