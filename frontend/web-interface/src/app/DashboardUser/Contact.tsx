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
    <form onSubmit={comunicazioneForm.onSubmit(handleSubmit)} style={{textAlign: 'left'}}>
      <Title
        order={2}
        size="h1"
        style={{ fontFamily: 'Outfit, var(--mantine-font-family)' }}
        fw={900}
        ta="center"
        mb="10px"
      >
        Contatta il tuo Medico
      </Title>
      
      <Select label="priorità messaggio" placeholder="seleziona la priorità" data={[
        {value: '1', label:'Bassa'},{value: '2', label:'Media'},{value: '3', label:'Alta'},
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

      <Group justify="center" mt={22}>
        <Button type="submit" size="md" w="100%">
          Invia
        </Button>
      </Group>
    </form>
  );
}


const sintomiComuni: string[]=[
  'nausea', 'vertigini', 'mal di testa', 'stanchezza', 'sudorazione', 'tremori', 'visione offuscata', 'confusione', 'palpitazioni', 'altri sintomi'
]

function SegnalaSintomi(){
  const {user} = useAuth();

  const form = useForm({
    initialValues: {
      sintomi: [] as string[],
      message: '',
    },

    validate: {
      message: (value) => value.trim().length === 0,
    },
  });

  const handleSubmit = async(valori: {sintomi: string[]; message: string}) => {
    if (!user || !user.id) return;

      try{

        const payload = {
          priorita: 1,
          descrizione: `${valori.sintomi.join(', ')}. ${valori.message}`,
          idPaziente: user.id
        };

        const response = await axios.post(`${import.meta.env.VITE_API_KEY}api/comunicazioni`, JSON.stringify(payload),{
        headers:{
          'Content-Type': 'application/json',
        },
        });
        form.reset()
        return response.data;
      }catch(error){
        console.error('errore', error);
        throw error;
      }
    };
  

  return(
    <form onSubmit={form.onSubmit(handleSubmit)} style={{textAlign: 'left'}}>
      <Title mb="10px" order={2} size="h1" style={{fontFamily: 'Outfit, var(--mantine-font-family)'}} fw={900} ta="center">
        Segnala Sintomi
      </Title>

      <Checkbox.Group {...form.getInputProps('sintomi')}>
        <SimpleGrid cols={2} spacing="xs">
          {sintomiComuni.map((sintomo) =>(
            <Checkbox
              key={sintomo}
              value={sintomo}
              label={sintomo}
              size="sm"
              color="blue"

            />
          ))}
        </SimpleGrid>
      </Checkbox.Group>

        <Textarea mt="md" label="Note Aggiuntive" placeholder="Inserisci note aggiuntive"
        maxRows={3} minRows={2} autosize name="message" variant="filled" {...form.getInputProps('message')}/>

        <Group justify="center" mt="xl">
          <Button type="submit" size="md" w="100%">
            Invia
          </Button>
        </Group>

    </form>
  );
}

export { ContactMedic, SegnalaSintomi };
