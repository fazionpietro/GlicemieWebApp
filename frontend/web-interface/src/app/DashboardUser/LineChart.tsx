import { LineChart } from '@mantine/charts';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';

const data = [
  { date: '01-01', Inserimenti_Glicemia: 60 },
  { date: '01-01', Inserimenti_Glicemia: 150 },
  { date: '01-01', Inserimenti_Glicemia: 170 },
  { date: '01-01', Inserimenti_Glicemia: 90 },
  { date: '01-01', Inserimenti_Glicemia: 70 }
];

function LineC() {
  return (
    <LineChart
      h="100%"
      w="100%"
      data={data}
      dataKey="date"
      withPointLabels
      series={[
        { name: 'Inserimenti_Glicemia', color: 'blue' },
      ]}
      curveType="monotone"
      tickLine="x"
      gridAxis="x"
      gridProps={{ yAxisId: "left" }}
      referenceLines={[{ y: 180, color: "yellow", label: 'soglia massima', strokeDasharray: '5 5' }, { y: 70, color: "yellow", label: 'soglia minima', strokeDasharray: '5 5' }]}

      yAxisProps={{
        domain: [50, 200],
      }}
    />
  );
}

export default LineC;
