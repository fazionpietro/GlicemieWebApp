import { Container, Grid, SimpleGrid, Skeleton } from '@mantine/core';
import LineC from './LineChart';
import ContactMedic from './Contact';



const PRIMARY_COL_HEIGHT = '300px';

export function LeadGrid() {
  const SECONDARY_COL_HEIGHT = `calc(${PRIMARY_COL_HEIGHT} / 2 - var(--mantine-spacing-md) / 2)`;


  /*sosituire skeleton con oggetti */
  return (
    <Container my="md">
      <SimpleGrid cols={{ base: 1, sm: 2 }} spacing="md">
          <div style={{ height: PRIMARY_COL_HEIGHT }}>
            <LineC/>
          </div>
        <Grid gutter="md">
          <Grid.Col>
            <div style={{ height: SECONDARY_COL_HEIGHT }}>
              <p>ultimi inserimenti</p>
            </div>
          </Grid.Col>
          <Grid.Col span={6}>
            <div style={{ height: SECONDARY_COL_HEIGHT }}>
              <p>aggiugi segnalazione</p>
            </div>
          </Grid.Col>
          <Grid.Col span={6}>
            <div style={{ height: SECONDARY_COL_HEIGHT }}>
              <ContactMedic/>
            </div>
          </Grid.Col>
        </Grid>
      </SimpleGrid>
    </Container>
  );
}