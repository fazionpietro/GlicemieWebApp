import { Grid } from '@mantine/core';
import LineC from './LineChart';
import ContactMedic from './Contact';
import { ModalSintomi, ModalMedicinali, ModalGlicemia } from './Modal';
import TableGlicemia from './TableGlicemia';
import { GiMedicines } from "react-icons/gi";
import { TbActivityHeartbeat } from "react-icons/tb";
import { IoIosAlert } from "react-icons/io";
import { PiPercentFill } from "react-icons/pi";
import classes from "../Components/StatsCard.module.css";
import { Paper, Text } from "@mantine/core";

export function LeadGrid() {
  return (
    <>
      <Grid gutter="md" mb={100}>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <GiMedicines size={48} color='#4A90E2' />
            </div>
            <div>
              <Text className={classes.label}>
                Numero Rilevazioni
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>150</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <TbActivityHeartbeat size={48} color="#4ae293ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Media Glicemia
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>130</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <PiPercentFill size={48} color="#e2b74aff" />
            </div>
            <div>
              <Text className={classes.label}>
                Valori Target
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>70%</span>
              </Text>
            </div>
          </Paper>
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <IoIosAlert size={48} color="#704ae2ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Numero Alert
              </Text>
              <Text fz="lg" className={classes.count}>
                <span className={classes.value}>5</span>
              </Text>
            </div>
          </Paper>
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
              <div style={{ height: "300px" }}>
                <TableGlicemia />
              </div>
            </Grid.Col>


            {/*due blocchi piccoli*/}
            <Grid.Col span={6}>
              <div style={{ height: "300px" }}>
                <ModalGlicemia />
                <ModalMedicinali />
                <ModalSintomi />
              </div>
            </Grid.Col>
            <Grid.Col span={6}>
              <div style={{ height: "300px" }}>
                <ContactMedic />
              </div>
            </Grid.Col>

          </Grid>
        </Grid.Col>
      </Grid>
    </>
  );
}
