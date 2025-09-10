import { LineChart } from '@mantine/charts';
import { ReferenceLine } from 'recharts';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';

const data=[
  {date: '01-01', Inserimenti_Glicemia:60},
  {date: '01-02', Inserimenti_Glicemia:150},
  {date: '01-03', Inserimenti_Glicemia:170},
  {date: '01-04', Inserimenti_Glicemia:90},
  {date: '01-05', Inserimenti_Glicemia:70}
];

function LineC() {
  return (
    <LineChart
      h="100%"
      w="100%"
      data={data}
      dataKey="date"
      withLegend
      legendProps={{ verticalAlign: 'bottom', height: 50 }}
      withPointLabels
      series={[
        { name: 'Inserimenti_Glicemia', color: 'indigo.6' },
      ]}
      curveType="monotone"
      tickLine="x"
      gridAxis="xy"
      gridProps={{ yAxisId:"left" }}
      referenceLines={[{ y: 180, color: "red", label: 'soglia massima' }, { y: 70, color: "red", label: 'soglia minima' }]}
      yAxisProps={{
        domain: [50, 200],
      }}
    />
  );
}

export default LineC;