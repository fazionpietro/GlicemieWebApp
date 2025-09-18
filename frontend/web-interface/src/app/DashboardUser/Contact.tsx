import { Button, Group, Textarea, TextInput, Title, Card, Text, Checkbox, CheckboxGroup, Stack, Select, SimpleGrid } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';


interface ComunicazioneData{
  priorita: number;
  descrizione: string;
  idPaziente: string;
}

interface FormValues{
    priorita: string; 
    message: string;
}

const  salvaComunicazione = async (datiComunicazione: ComunicazioneData) =>{
    try{
    const response = await axios.post(`${import.meta.env.VITE_API_KEY}api/comunicazioni`, JSON.stringify(datiComunicazione),{
      headers:{
        'Content-Type': 'application/json',
      },
    });
    return response.data;
    }catch(error){
      console.error('errore', error);
      throw error;
    }
  };
  

function ContactMedic() {

  const {user}  = useAuth();
 
  const comunicazioneForm = useForm({
    initialValues:{
      priorita:'1',
      message:''
    },
    validate: {
      message: (value) => (value.trim().length === 0 ? 'obbligatorio': null)
    },
  });

  const handleSubmit= async (values: FormValues) =>{

    if(!user || !user.id){
      return;
    }
    try {
      const datiComunicazione = {
        priorita: parseInt(values.priorita),
        descrizione: values.message,
        idPaziente: user.id
      };
      const risultato = await salvaComunicazione(datiComunicazione);

      comunicazioneForm.reset();
    }catch(error){
      console.error("errore");
    }
  };

  return (
    <form onSubmit={comunicazioneForm.onSubmit(handleSubmit)}>
      <Title
        order={2}
        size="h1"
        style={{ fontFamily: 'Outfit, var(--mantine-font-family)' }}
        fw={900}
        ta="center"
      >
        Contact your Medic
      </Title>
      
      <Select label="priorità messaggio" placeholder="seleziona la priorità" data={[
        {value: '1', label:'bassa'},{value: '2', label:'media'},{value: '3', label:'alta'},
      ]}
      {...comunicazioneForm.getInputProps('priorita')}
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
        {...comunicazioneForm.getInputProps('message')}
      />

      <Group justify="center" mt="xl">
        <Button type="submit" size="md">
          Send message
        </Button>
      </Group>
    </form>
  );
}


const sintomiComuni: string[]=[
  'nausea', 'vertigini', 'mal di testa', 'stanchezza', 'sudorazione', 'tremori', 'visione offuscata', 'confusione', 'palpitazioni', 'altri sintomi'
]

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

    <SimpleGrid cols={2} spacing="xs">
      {sintomiComuni.map((sintomo)=>(
        <Checkbox
          key={sintomo}
          label={sintomo}
          size="sm"
          color="blue"
        />
      ))}
    </SimpleGrid>

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
