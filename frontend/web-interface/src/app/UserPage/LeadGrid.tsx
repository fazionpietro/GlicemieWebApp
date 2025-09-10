import { Container, Grid, SimpleGrid, Skeleton } from '@mantine/core';
import LineC from './LineChart';



const PRIMARY_COL_HEIGHT = '300px';

export function LeadGrid() {
  const SECONDARY_COL_HEIGHT = `calc(${PRIMARY_COL_HEIGHT} / 2 - var(--mantine-spacing-md) / 2)`;


  /*sosituire skeleton con oggetti */
  return (
    <Container my="md">
      <SimpleGrid cols={{ base: 1, sm: 2 }} spacing="md">
          <div>
            <LineC/>
          </div>
        <Grid gutter="md">
          <Grid.Col>
            <div>
              <p>ultimi inserimenti</p>
            </div>
          </Grid.Col>
          <Grid.Col span={6}>
            <div>
              <p>aggiugi segnalazione</p>
            </div>
          </Grid.Col>
          <Grid.Col span={6}>
            <div>
              <p>contatta medico</p>
            </div>
          </Grid.Col>
        </Grid>
      </SimpleGrid>
    </Container>
  );
}