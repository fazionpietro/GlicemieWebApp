import { Container, Grid, SimpleGrid, Skeleton } from '@mantine/core';
import LineC from './LineChart';
import ContactMedic from './Contact';
import {ModalSintomi, ModalMedicinali, ModalGlicemia} from './Modal';

export function LeadGrid() {


  /*sosituire skeleton con oggetti */
  return (
    <Container my="md">
      <SimpleGrid cols={{ base: 1, sm: 2 }} spacing="md">
          <div style={{height: "100%"}}>
            <LineC/>
          </div>
        <Grid gutter="md">
          <Grid.Col span={12}>
              <p>ultimi inserimenti</p>
          </Grid.Col>
          <Grid.Col span={6}>
            <ModalGlicemia/>
            <ModalMedicinali/>
            <ModalSintomi/>
          </Grid.Col>
          <Grid.Col span={6}>
              <ContactMedic/>
          </Grid.Col>
        </Grid>
      </SimpleGrid>
    </Container>
  );
}