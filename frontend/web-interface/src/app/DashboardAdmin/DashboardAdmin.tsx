import {
    Container,
    FloatingIndicator,
    Grid,
    Paper,
    ScrollArea,
    Skeleton,
    Table,
    Text,
    UnstyledButton,
} from "@mantine/core";
import { HeaderMegaMenu } from "../CommonFile/Header";
import classes from "../CommonFile/StatsCard.module.css";
import { FiUsers, FiActivity, FiAlignRight } from "react-icons/fi";
import { FaUserMd } from "react-icons/fa";
import floatingcss from "./AdminFloatingIndicator.module.css";
import { useState } from "react";
import TablePazienti from "./TablePazienti";

const PRIMARY_COL_HEIGHT = "50vh";

const data = ["Gestione pazienti", "Gestione medici", "Logs report"];
function DashboardAdmin() {
    const [rootRef, setRootRef] = useState<HTMLDivElement | null>(null);
    const [active, setActive] = useState(0);
    const [controlsRefs, setControlsRefs] = useState<
        Record<string, HTMLButtonElement | null>
    >({});
    const setControlRef = (index: number) => (node: HTMLButtonElement) => {
        controlsRefs[index] = node;
        setControlsRefs(controlsRefs);
    };

    const controls = data.map((item, index) => (
        <UnstyledButton
            key={item}
            className={floatingcss.control}
            ref={setControlRef(index)}
            onClick={() => setActive(index)}
            mod={{ active: active === index }}
        >
            <span className={floatingcss.controlLabel}>{item}</span>
        </UnstyledButton>
    ));



    return (
        <div>
            <HeaderMegaMenu />
            <Container fluid my={40}>
                <Grid gutter="md" mb={100}>
                    {/* Le tue card statistiche */}
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
                                    <span className={classes.value}>12</span>
                                </Text>
                            </div>
                        </Paper>
                    </Grid.Col>
                    <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
                        <Paper className={classes.stat} radius="md" shadow="md">
                            <div className={classes.icon}>
                                <FaUserMd size={48} color="#4ae293ff" />
                            </div>
                            <div>
                                <Text className={classes.label}>
                                    Medici Totali
                                </Text>
                                <Text fz="lg" className={classes.count}>
                                    <span className={classes.value}>12</span>
                                </Text>
                            </div>
                        </Paper>
                    </Grid.Col>
                    <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
                        <Paper className={classes.stat} radius="md" shadow="md">
                            <div className={classes.icon}>
                                <FiActivity size={48} color="#e2b74aff" />
                            </div>
                            <div>
                                <Text className={classes.label}>
                                    Rilevazioni giornaliere
                                </Text>
                                <Text fz="lg" className={classes.count}>
                                    <span className={classes.value}>12</span>
                                </Text>
                            </div>
                        </Paper>
                    </Grid.Col>
                    <Grid.Col span={{ base: 12, md: 6, lg: 3 }}>
                        <Paper className={classes.stat} radius="md" shadow="md">
                            <div className={classes.icon}>
                                <FiAlignRight size={48} color="#704ae2ff" />
                            </div>
                            <div>
                                <Text className={classes.label}>
                                    Log giornalieri
                                </Text>
                                <Text fz="lg" className={classes.count}>
                                    <span className={classes.value}>12</span>
                                </Text>
                            </div>
                        </Paper>
                    </Grid.Col>
                </Grid>

                <Grid gutter="md" pt={60}>
                    <Grid.Col span={12}>
                        <div
                            className={floatingcss.root}
                            ref={setRootRef}
                            style={{ marginBottom: "20px" }}
                        >
                            {controls}
                            <FloatingIndicator
                                target={controlsRefs[active]}
                                parent={rootRef}
                                className={floatingcss.indicator}
                            />
                        </div>
                    </Grid.Col>

                    <Grid.Col span={12}>
                        <TablePazienti>
                            
                        </TablePazienti>
                    </Grid.Col>
                </Grid>
            </Container>
        </div>
    );
}

export default DashboardAdmin;
