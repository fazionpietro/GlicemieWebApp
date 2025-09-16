import { Button, Group, Textarea, TextInput, Title, Card, Text, Checkbox, Stack } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useState } from 'react';

function ContactMedic() {
  const form = useForm({
    initialValues: {
      subject: '',
      message: '',
    },
    validate: {
      subject: (value) => value.trim().length === 0,
    },
  });

  return (
    <form onSubmit={form.onSubmit(() => { })}>
      <Title
        order={2}
        size="h1"
        style={{ fontFamily: 'Outfit, var(--mantine-font-family)' }}
        fw={900}
        ta="center"
      >
        Contact your Medic
      </Title>
      <TextInput
        label="Subject"
        placeholder="Subject"
        mt="md"
        name="subject"
        variant="filled"
        {...form.getInputProps('subject')}
      />
      <Textarea
        mt="md"
        label="Messaggio"
        placeholder="Inserisci il messaggio"
        maxRows={10}
        minRows={5}
        autosize
        name="message"
        variant="filled"
        {...form.getInputProps('message')}
      />

      <Group justify="center" mt="xl">
        <Button type="submit" size="md">
          Send message
        </Button>
      </Group>
    </form>
  );
}

function SegnalaSintomi(){
  const form = useForm({
    initialValues: {
      subject: '',
      message: '',
    },
    validate: {
      subject: (value) => value.trim().length === 0,
    },
  });

  return(
    <form onSubmit={form.onSubmit(()=>{})} >
      <Title order={2} size="h1" style={{fontFamily: 'Outfit, var(--mantine-font-family)'}} fw={900} ta="center">
        Segnala Sintomi
      </Title>

      <TextInput label="Subject" placeholder="Subject" mt="md" name="subject"
      variant="filled" {...form.getInputProps('subject')}/>

      <Textarea mt="md" label="messaggio" placeholder="Inserisci il messaggio"
      maxRows={10} minRows={5} autosize name="message" variant="filled" {...form.getInputProps('message')}/>

      <Group justify="center" mt="xl">
        <Button type="submit" size="md">
          Send message
        </Button>
      </Group>

    </form>
  )
}

export { ContactMedic, SegnalaSintomi };
