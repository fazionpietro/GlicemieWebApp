import { Grid, Card} from '@mantine/core';
import LineC from './LineChart';
import ContactMedic from './Contact';
import {ModalSintomi, ModalMedicinali, ModalGlicemia} from './Modal';
import TableGlicemia from './TableGlicemia';
import { GiMedicines } from "react-icons/gi";
import { TbActivityHeartbeat } from "react-icons/tb";
import { IoIosAlert } from "react-icons/io";
import { PiPercentFill } from "react-icons/pi";

export function LeadGrid() {
  return (
    <>
      <Grid>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Card shadow="sm" padding="lg" radius="md" withBorder className='stat-card'>
            <GiMedicines size={40} />
            <p>NUMERO RILEVAZIONI</p>
            <p>150</p>
          </Card>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Card shadow="sm" padding="lg" radius="md" withBorder className='stat-card'>
            <TbActivityHeartbeat size={40} />
            <p>MEDIA GLICEMIA</p>
            <p>130</p>
          </Card>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Card shadow="sm" padding="lg" radius="md" withBorder className='stat-card'>
            <PiPercentFill  size={40}/>
            <p>VALORI TARGET</p>
            <p>70%</p>
          </Card>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Card shadow="sm" padding="lg" radius="md" withBorder className='stat-card'>
            <IoIosAlert  size={40}/>
            <p>NUMERO ALERT</p>
            <p>5</p>
          </Card>
        </Grid.Col>
      </Grid>

      <Grid columns={24}>
        <Grid.Col span={12}>
          <LineC />
        </Grid.Col>

        <Grid.Col span={12}>
          <Grid>
            {/*blocco grande*/}
            <Grid.Col span={12}>
              <div style={{height: "300px"}}>
                <TableGlicemia />
              </div>
            </Grid.Col>

            {/*due blocchi piccoli*/}
            <Grid.Col span={6}>
              <div style={{height: "300px"}}>
                <ModalGlicemia/>
                <ModalMedicinali/>
                <ModalSintomi/>
              </div>
            </Grid.Col>
            <Grid.Col span={6}>
              <div style={{height: "300px"}}>
                <ContactMedic />
              </div>
            </Grid.Col>

          </Grid>
        </Grid.Col>
      </Grid>
    </>
  );
}