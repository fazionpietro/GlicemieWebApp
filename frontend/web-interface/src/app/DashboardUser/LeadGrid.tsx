import { Grid } from '@mantine/core';
import LineC from './LineChart';
import { ContactMedic, SegnalaSintomi } from './Contact';
import { ModalSintomi, ModalMedicinali, ModalGlicemia } from './Modal';
import TableGlicemia from './TableGlicemia';

export function LeadGrid() {
  return (
    <>

      <Grid columns={24} mb={100}>
        <Grid.Col span={15}>
          <div style={{ height: "300px" }}>
            <LineC />
          </div>
        </Grid.Col>

        <Grid.Col span={9}>
          <Grid>
            <Grid.Col mt={50}>
              <div style={{ height: "300px" }}>
                <TableGlicemia />
              </div>
            </Grid.Col>
          </Grid>
        </Grid.Col>
      </Grid>
      <Grid columns={24} mt={100}>
        <Grid.Col span={8}>
          <SegnalaSintomi/>
        </Grid.Col>
        <Grid.Col span={8}>
          <ModalMedicinali/>
        </Grid.Col>
        <Grid.Col span={8}>
          <div style={{ height: "300px" }}>
            <ContactMedic />
          </div>
        </Grid.Col>
      </Grid>
    </>
  );
}
