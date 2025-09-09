import { LineChart } from '@mantine/charts';

const data=[
  {
    date: '2024-01-01',
    glicemia:180
  },
  {
    date: '2024-01-02',
    glicemia:150
  },
  {
    date: '2024-01-03',
    glicemia:170
  }
];

function LineC() {
  return (
    <LineChart
      h={300}
      data={data}
      dataKey="date"
      series={[
        { name: 'glicemia', color: 'indigo.6' },
      ]}
      curveType="monotone"
      tickLine="x"
      gridAxis="xy"
    />
  );
}

export default LineC;