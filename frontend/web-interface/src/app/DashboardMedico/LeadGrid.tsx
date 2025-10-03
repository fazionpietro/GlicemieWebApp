import { Grid, Card } from '@mantine/core';
import classes from "../Components/StatsCard.module.css";
import { Paper, Text } from "@mantine/core";
import { IoIosAlert } from "react-icons/io";
import { TbActivityHeartbeat } from "react-icons/tb";
import { HiMiniArrowTrendingUp } from "react-icons/hi2";
import { FiUsers } from "react-icons/fi";



export function LeadGrid() {
  return (
    <>
      <Grid gutter="md" mb={100}>
        <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
          <Paper className={classes.stat} radius="md" shadow="md">
            <div className={classes.icon}>
              <FiUsers size={48} color="#4A90E2" />
            </div>
            <div>
              <Text className={classes.label}>
                Pazienti Totali
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
              <IoIosAlert size={48} color="#4ae293ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Alert Attivi
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
              <TbActivityHeartbeat size={48} color="#e2b74aff" />
            </div>
            <div>
              <Text className={classes.label}>
                Rilevazioni Oggi
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
              <HiMiniArrowTrendingUp size={48} color="#704ae2ff" />
            </div>
            <div>
              <Text className={classes.label}>
                Media Controlli
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
          <h1>Alert Pazienti</h1>
        </Grid.Col>
        <Grid.Col span={12}>
          <h1>Inserimenti Recenti</h1>
        </Grid.Col>

      </Grid>
    </>
  );
}
