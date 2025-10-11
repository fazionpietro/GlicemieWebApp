
import { Card, ScrollArea, Text, Stack, Box, Group } from '@mantine/core';
import { useState } from 'react';
import type { Comunicazione } from '../type/DataType';

const getPriorityColor = (priorita: number) => {
  switch (priorita) {
    case 3:
      return { color: 'red' };
    case 2:
      return { color: 'orange' };
    case 1:
      return { color: 'blue' };
    default:
      return { color: 'gray' };
  }
};

const formatTimestamp = (timestamp: string) => {
  const date = new Date(timestamp);
  return date.toLocaleString('it-IT', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

type Props = {
  messages: Comunicazione[]
}


export function LogMessaggi({ messages }: Props) {





  return (
    <Card padding="lg" radius="md">
      <Text size="lg" fw={700} mb="md">
        Messaggi
      </Text>

      <ScrollArea type="auto">
        <Stack gap="sm">
          {messages.map((msg) => {
            const priorityStyle = getPriorityColor(msg.priorita);

            return (
              <Box
                key={msg.id}
                p="md"
                style={{

                  backgroundColor: '#424242',
                  borderRadius: '8px',
                  borderLeft: `4px solid var(--mantine-color-${priorityStyle.color}-6)`,
                  transition: 'transform 0.2s ease',
                }}
              >
                <Group justify="space-between" mb="xs">

                  <Text size="xs" >
                    {formatTimestamp(msg.timestamp)}
                  </Text>
                </Group>

                <Text fw={600} size="sm" mb="xs" style={{ width: '100%', wordBreak: 'break-word' }}>
                  {msg.descrizione}
                </Text>

                <Group gap="xs">
                  <Text size="sm" >
                    Paziente:
                  </Text>
                  <Text size="sm" fw={500}>
                    {msg.nome} {msg.cognome}
                  </Text>
                </Group>

                <Text size="xs" mt={4}>
                  {msg.email}
                </Text>
              </Box>
            );
          })}
        </Stack>
      </ScrollArea>
    </Card>
  );
}
