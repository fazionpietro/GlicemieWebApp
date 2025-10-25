import {
  Badge,
  Modal,
  ScrollArea,
  Text,
  Box,
  Title,
  ActionIcon,
  Paper,
  Group,
  useMantineTheme,
  Pill,
  Container,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { FaRegEye } from "react-icons/fa";
import { ChartTooltip, LineChart } from "@mantine/charts";
import {
  IconArrowUpRight,
  IconArrowDownRight,
  IconMinus,
} from "@tabler/icons-react";
import type { Rilevazione } from "../type/DataType";

type Props = {
  id: string;
  rilevazioni: Rilevazione[] | null;
  fetchRilevazioni?: () => void;
};

export function StatusBadge({ id, rilevazioni }: Props) {
  const getColor = (livello: string) => {
    switch (livello.toLowerCase().trim()) {
      case "alto":
        return "red";
      case "basso":
        return "yellow";
      case "normale":
        return "green";
      case "loading":
        return "";
    }
  };

  function getLevel() {
    const myRilevazioni = rilevazioni?.filter((item) => item.idPaziente == id);
    const sum = myRilevazioni?.reduce((a, c) => a + c.valore, 0);
    let avg = -1;
    console.log(`${id}: ${myRilevazioni?.length}`);
    if (myRilevazioni && sum) avg = sum / myRilevazioni?.length;
    if (avg > 180) return "alto";
    else if (avg < 70 && avg > 0) return "basso";
    else if (avg <= 180 && avg >= 70) return "normale";
    else return "loading";
  }

  return (
    <>
      {rilevazioni?.find((i) => i.idPaziente == id) ? (
        <Badge size="sm" variant="filled" w="70%" bg={getColor(getLevel())}>
          {getLevel()}
        </Badge>
      ) : (
        <Badge size="sm" variant="filled" w="70%" bg={"gray"}>
          no rilevazioni
        </Badge>
      )}
    </>
  );
}

export function RilevazioniModal({ id, rilevazioni, fetchRilevazioni }: Props) {
  const theme = useMantineTheme();
  const [opened, { open, close }] = useDisclosure(false);

  // Filtra e ordina le rilevazioni per il paziente specifico
  const patientRilevazioni =
    rilevazioni
      ?.filter((r) => r.idPaziente === id)
      .sort(
        (a, b) =>
          new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime(),
      ) // Ordine cronologico
      .map((i) => ({
        id: i.id,
        date: `${new Date(i.timestamp).toLocaleDateString("it-IT")} ${new Date(i.timestamp).toLocaleTimeString("it-IT")}`,
        valore: i.valore,
      })) || [];

  // Funzione per determinare lo stato e il colore di una rilevazione
  const getRilevazioneStatus = (value: number) => {
    if (value > 180)
      return {
        status: "Alto",
        color: theme.colors.red[6],
        icon: <IconArrowUpRight size={14} />,
      };
    if (value < 70)
      return {
        status: "Basso",
        color: theme.colors.yellow[6],
        icon: <IconArrowDownRight size={14} />,
      };
    return {
      status: "Normale",
      color: theme.colors.green[6],
      icon: <IconMinus size={14} />,
    };
  };

  // Calcola statistiche
  const latestValue = patientRilevazioni[0]?.valore || 0;
  const avgValue =
    patientRilevazioni.reduce((sum, r) => sum + r.valore, 0) /
      patientRilevazioni.length || 0;
  const maxValue = Math.max(...patientRilevazioni.map((r) => r.valore), 0);
  const minValue =
    Math.min(...patientRilevazioni.map((r) => r.valore), Infinity) || 0;

  return (
    <>
      <ActionIcon
        variant="subtle"
        color="gray"
        onClick={() => {
          open();

          if (fetchRilevazioni) fetchRilevazioni();
        }}
        size="sm"
      >
        <FaRegEye />
      </ActionIcon>

      <Modal
        centered
        opened={opened}
        onClose={close}
        title={`Rilevazioni Paziente`}
        size="xl"
        radius="md"
      >
        {/* Statistiche */}
        <Group justify="space-between" mb="lg">
          <Box>
            <Text size="sm" c="dimmed">
              Ultimo valore
            </Text>
            <Text size="xl" fw={700}>
              {latestValue} mg/dL
            </Text>
          </Box>
          <Box>
            <Text size="sm" c="dimmed">
              Media
            </Text>
            <Text size="xl" fw={700}>
              {avgValue.toFixed(1)} mg/dL
            </Text>
          </Box>
          <Box>
            <Text size="sm" c="dimmed">
              Massimo
            </Text>
            <Text size="xl" fw={700}>
              {maxValue} mg/dL
            </Text>
          </Box>
          <Box>
            <Text size="sm" c="dimmed">
              Minimo
            </Text>
            <Text size="xl" fw={700}>
              {minValue === Infinity ? 0 : minValue} mg/dL
            </Text>
          </Box>
        </Group>

        <Box mb="lg">
          <Title order={4} mb="sm">
            Andamento delle misurazioni
          </Title>
          <Paper withBorder p="md" radius="md">
            <LineChart
              h={300}
              data={patientRilevazioni}
              dataKey="date"
              series={[
                { name: "valore", color: "blue", label: "Glicemia (mg/dL)" },
              ]}
              curveType="monotone"
              tickLine="x"
              gridAxis="x"
              gridProps={{ yAxisId: "left" }}
              referenceLines={[
                {
                  y: 180,
                  color: "yellow",
                  label: "Soglia massima",
                  strokeDasharray: "5 5",
                },
                {
                  y: 70,
                  color: "yellow",
                  label: "Soglia minima",
                  strokeDasharray: "5 5",
                },
              ]}
              yAxisProps={{
                domain: [50, 220],
              }}
            />
          </Paper>
        </Box>

        {/* Lista Rilevazioni */}
        <Title order={4} mb="sm">
          Storico Rilevazioni
        </Title>
        <ScrollArea h={300}>
          {patientRilevazioni.length === 0 ? (
            <Text size="sm" c="dimmed" ta="center" py="xl">
              Nessuna rilevazione disponibile
            </Text>
          ) : (
            <Box>
              {patientRilevazioni
                .map((item) => {
                  const formattedTime = item.date;
                  const { status, color, icon } = getRilevazioneStatus(
                    item.valore,
                  );

                  return (
                    <Paper
                      key={item.id}
                      withBorder
                      p="md"
                      mb="xs"
                      radius="md"
                      style={{
                        borderLeft: `4px solid ${color}`,
                      }}
                    >
                      <Group justify="space-between">
                        <Box>
                          <Text size="sm" c="dimmed">
                            {formattedTime}
                          </Text>
                          <Text size="lg" fw={700}>
                            {item.valore} mg/dL
                          </Text>
                        </Box>
                        <Group gap="xs">
                          {icon}
                          <Pill
                            w="100%"
                            bg={color}
                            c="white"
                            fw={600}
                            size="sm"
                          >
                            {status}
                          </Pill>
                        </Group>
                      </Group>
                    </Paper>
                  );
                })
                .reverse()}
            </Box>
          )}
        </ScrollArea>
      </Modal>
    </>
  );
}
