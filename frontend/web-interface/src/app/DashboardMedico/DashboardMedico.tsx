import { Grid, Card, Title, useMantineTheme } from "@mantine/core";

import { HeaderMegaMenu } from "../Components/Header";

import { useMediaQuery } from "@mantine/hooks";
import { TableTerapie } from "./TableTerapie.tsx";
import TablePazienti from "../Components/TablePazienti";
import type { Comunicazione, Paziente, Terapia } from "../type/DataType";
import { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { useAuth } from "../../context/AuthContext.tsx";
import { LogMessaggi } from "./LogMessaggi.tsx";

/**
 * Main dashboard for Doctors.
 * Provides views for managing therapies, patients, and viewing communications.
 */
function MedicPage() {
  const theme = useMantineTheme();
  const isMobile = useMediaQuery(`(max-width: ${theme.breakpoints.sm})`);
  const [pazienti, setPazienti] = useState<Paziente[] | null>(null);
  const [didFetch, setDidFetch] = useState(false);
  const [terapie, setTerapie] = useState<Terapia[] | null>(null);
  const [comunicazioni, setComunicazioni] = useState<Comunicazione[]>([]);
  const { user } = useAuth();

  const fetchPazienti = useCallback(async () => {
    await axios({
      method: "GET",
      url: `${import.meta.env.VITE_API_KEY}api/pazienti/my/${user?.id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {
        setPazienti(res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  }, [user?.id]);

  const fetchTerapie = useCallback(async () => {
    await axios({
      method: "GEt",
      url: `${import.meta.env.VITE_API_KEY}api/terapie/medico/${user?.id}`,
      headers: {
        "Content-Type": "application/json",
        withCredentials: true,
      },
    })
      .then((res) => {
        setTerapie(res.data);
      })
      .catch((err) => {
        console.error(err);
      });
  }, [user?.id]);

  useEffect(() => {
    if (!didFetch && user !== null) {
      fetchTerapie();
      fetchPazienti();
      setDidFetch(true);
    }
  }, [user, didFetch, fetchTerapie, fetchPazienti]);

  function handleMessageRead(id: string) {
    setComunicazioni((prevMessages) =>
      prevMessages.filter((msg) => msg.id !== id),
    );
  }

  useEffect(() => {
    if (user === null) {
      return;
    }

    const websocket = new WebSocket(
      `ws://localhost:8080/ws/comunicazioni?id=${user.id}`,
    );

    websocket.onopen = () => console.log("Connected to WebSocket server");

    websocket.onmessage = (event) => {
      try {
        const parsedData = JSON.parse(event.data);

        const newComs: Comunicazione[] = Array.isArray(parsedData)
          ? parsedData
          : [parsedData];

        setComunicazioni((prevComs) => {
          const comsMap = new Map();
          prevComs.forEach((com) => comsMap.set(com.id, com));
          newComs.forEach((com) => comsMap.set(com.id, com));

          const uniqueComs = Array.from(comsMap.values()).sort(
            (a, b) =>
              new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime(),
          );
          return uniqueComs;
        });
      } catch (error) {
        console.error("Error parsing WebSocket message:", error);
      }
    };

    websocket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    websocket.onclose = () => console.log("Disconnected from WebSocket server");

    const handleBeforeUnload = () => {
      if (websocket.readyState === WebSocket.OPEN) {
        websocket.close();
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
      if (websocket.readyState === WebSocket.OPEN) {
        websocket.close();
      }
    };
  }, [user]);

  return (
    <div>
      <HeaderMegaMenu />

      <Grid gutter={isMobile ? "md" : "xl"} ta={"left"} align="flex-start">
        <Grid.Col span={{ base: 12, lg: 8 }}>
          <Grid.Col span={12}>
            <Card p="40" radius={"md"} h="40vh" shadow="sm" w={"auto"}>
              <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>
                Gestione Terapie
              </Title>
              <TableTerapie
                pazienti={pazienti}
                fetchPazienti={fetchPazienti}
                terapie={terapie}
                fetchTerapie={fetchTerapie}
              />
            </Card>
          </Grid.Col>

          <Grid.Col span={12}>
            <Card p="40" radius={"md"} h="40vh" shadow="sm" w={"auto"}>
              <Title mb={"30"} size="2rem" order={isMobile ? 4 : 3}>
                Pazienti
              </Title>
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
          <Card radius={"md"} h={"82vh"}>
            <LogMessaggi
              messages={comunicazioni}
              onMessageRead={handleMessageRead}
            />
          </Card>
        </Grid.Col>
      </Grid>
    </div>
  );
}

export default MedicPage;
