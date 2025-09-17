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


export function Assunzioni({ terapie, assunzioni }: Props) {


  return (
    <Box p="md" w={600}>
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
                  <Chip color="green" size="lg" radius="md" variant='light' icon={<LiaPillsSolid />}>Assunto</Chip>

                </Flex>
                <Flex
                  mt={10} h="30%"
                  align={"self-end"}
                  justify="flex-end"
                >
                  <Text>0/3</Text>

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
        <Text mb={20} fw={500}>Non assumi Stagisti da 2 giorni
        </Text>
      </Alert>

      <Button
        mt={20}
        leftSection={<IconReportMedical size={16} />}
        variant="filled"
      >
        Invia Report al Medico
      </Button>
    </Box >
  );
}
