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
import classes from "./StatsCard.module.css";
import { FiUsers, FiActivity, FiAlignRight } from "react-icons/fi";
import { FaUserMd } from "react-icons/fa";
import floatingcss from "./AdminFloatingIndicator.module.css";
import { useState } from "react";

const PRIMARY_COL_HEIGHT = "50vh";

const data = ["Gestione pazienti", "Gestione medici", "Logs report"];
const elements = [
    { position: 6, mass: 12.011, symbol: "C", name: "Carbon" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
    { position: 7, mass: 14.007, symbol: "N", name: "Nitrogen" },
    { position: 39, mass: 88.906, symbol: "Y", name: "Yttrium" },
    { position: 56, mass: 137.33, symbol: "Ba", name: "Barium" },
    { position: 58, mass: 140.12, symbol: "Ce", name: "Cerium" },
];

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

    const rows = elements.map((element) => (
        <Table.Tr key={element.name}>
            <Table.Td>{element.position}</Table.Td>
            <Table.Td>{element.name}</Table.Td>
            <Table.Td>{element.symbol}</Table.Td>
            <Table.Td>{element.mass}</Table.Td>
        </Table.Tr>
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
                                <FiAlignRight size={48} color="#704ae2ff" />
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
                        <Paper
                            withBorder
                            radius="md"
                            style={{ overflow: "hidden" }}
                        >
                            <ScrollArea
                                h={"40vh"}
                                type="always"
                                offsetScrollbars
                            >
                                <Table
                                    highlightOnHover
                                    withTableBorder
                                    withColumnBorders
                                    verticalSpacing="sm"
                                    horizontalSpacing="md"
                                    style={{
                                        tableLayout: "fixed",
                                        minWidth: "100%",
                                    }}
                                >
                                    <Table.Thead>
                                        <Table.Tr>
                                            <Table.Th style={{ width: "25%" }}>
                                                Element position
                                            </Table.Th>
                                            <Table.Th style={{ width: "25%" }}>
                                                Element name
                                            </Table.Th>
                                            <Table.Th style={{ width: "25%" }}>
                                                Symbol
                                            </Table.Th>
                                            <Table.Th style={{ width: "25%" }}>
                                                Atomic mass
                                            </Table.Th>
                                        </Table.Tr>
                                    </Table.Thead>
                                    <Table.Tbody>{rows}</Table.Tbody>
                                </Table>
                            </ScrollArea>
                        </Paper>
                    </Grid.Col>
                </Grid>
            </Container>
        </div>
    );
}

export default DashboardAdmin;
