import {
    Container,
    FloatingIndicator,
    Grid,
    Paper,
    Notification,
    Text,
    UnstyledButton,
    ScrollArea,
    Title,
} from "@mantine/core";
import { HeaderMegaMenu } from "../CommonFile/Header";
import classes from "./StatsCard.module.css";
import { FiUsers, FiActivity, FiAlignRight } from "react-icons/fi";
import { FaUserMd } from "react-icons/fa";
import floatingcss from "./AdminFloatingIndicator.module.css";
import { useEffect, useState } from "react";
import TablePazienti from "./TablePazienti";
import axios from "axios";
import type { Medico, Paziente } from "../type/DataType";
import { TableMedici } from "./TableMedici";


const PRIMARY_COL_HEIGHT = "50vh";

const data = ["Gestione pazienti", "Gestione medici"];
function DashboardAdmin() {
    const [rootRef, setRootRef] = useState<HTMLDivElement | null>(null);
    const [active, setActive] = useState(0);
    const [controlsRefs, setControlsRefs] = useState<
        Record<string, HTMLButtonElement | null>
    >({});
    const [didFetch, setDidFetch] = useState(false);

    const [pazienti, setPazienti] = useState<Paziente[] | null>(null);
    const [medici, setMedici] = useState<Medico[] | null>(null);

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

    async function fetchPazienti() {
        await axios({
            method: "GET",
            url: `${import.meta.env.VITE_API_KEY}api/pazienti/all`,
            headers: {
                "Content-Type": "application/json",
                withCredentials: true,
            },
        })
            .then((res) => {
                setPazienti(res.data);
                console.log(pazienti);
            })
            .catch((err) => {
                console.error(err);
            });
    }

    async function fetchMedici() {
        await axios({
            method: "GET",
            url: `${import.meta.env.VITE_API_KEY}api/utenti/medici/all`,
            headers: {
                "Content-Type": "application/json",
                withCredentials: true,
            },
        })
            .then((res) => {
                setMedici(res.data);
                console.log(medici);
            })
            .catch((err) => {
                console.error(err);
            });
    }

    useEffect(() => {
        if (!didFetch) {
            fetchPazienti();
            fetchMedici();
            setDidFetch(true);
        }
    }, []);

    return (
        <div>
            <HeaderMegaMenu />
            <Container fluid my={40}>
                <Grid gutter="md" mb={70}>
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
                    <Grid.Col span={{ base: 12, md: 12, lg: 6 }}>
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
                        {active == 0 ? (
                            <Grid.Col span={12}>
                                <TablePazienti
                                    pazienti={pazienti}
                                    fetchPazienti={fetchPazienti}
                                    medici={medici}
                                    fetchMedici={fetchMedici}
                                ></TablePazienti>
                            </Grid.Col>
                        ) : (
                            <Grid.Col span={12}>
                                <TableMedici
                                    medici={medici}
                                    fetchMedici={fetchMedici}
                                />
                            </Grid.Col>
                        )}
                    </Grid.Col>
                    <Grid.Col span={{ base: 12, md: 12, lg: 6 }}>
                        <Container  h={"45vh"} ta={"left"} pl="2vw" >
                                <Title fz="3rem">Logs</Title>
                                <ScrollArea style={{ height: "100%" }} mah="45vh">
                                    
                                    
                                </ScrollArea>
                                
                                
                            
                        </Container>
                    </Grid.Col>
                </Grid>
                
            </Container>
        </div>
    );
}

export default DashboardAdmin;
