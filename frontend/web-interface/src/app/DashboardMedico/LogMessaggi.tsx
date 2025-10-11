
import { Card, ScrollArea, Text, Stack, Box, Group } from '@mantine/core';
import { useState } from 'react';

interface LogMessage {
  id: number;
  descrizione: string;
  priorita: number;
  timestamp: string;
  nome_paziente: string;
  cognome_paziente: string;
  email_paziente: string;
}

const messagesExample: LogMessage[] = [
  {
    id: 1,
    descrizione: 'Richiesta urgente visita cardiologica',
    priorita: 3,
    timestamp: '2025-10-11T14:30:00',
    nome_paziente: 'Mario',
    cognome_paziente: 'Rossi',
    email_paziente: 'mario.rossi@example.com'
  },
];

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

export function LogMessaggi() {
  const [messages] = useState<LogMessage[]>(messagesExample);

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
                    {msg.nome_paziente} {msg.cognome_paziente}
                  </Text>
                </Group>

                <Text size="xs" mt={4}>
                  {msg.email_paziente}
                </Text>
              </Box>
            );
          })}
        </Stack>
      </ScrollArea>
    </Card>
  );
}
