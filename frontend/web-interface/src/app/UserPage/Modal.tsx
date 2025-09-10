import { useDisclosure } from '@mantine/hooks';
import { Modal, Button } from '@mantine/core';
import { JsonInput } from '@mantine/core';

function ModalSintomi() {
  const [opened, { open, close }] = useDisclosure(false);

  return (
    <>
      <Modal opened={opened} onClose={close} title="Inserisci una descrizione dei sintomi" centered>
            <JsonInput
                size="xs"
                radius="lg"
                placeholder="segnala i sintomi qui"
                w="100%"
            />
            <button style={{marginTop:"20px"}}>invia</button>
      </Modal>

      <Button fullWidth variant="default" onClick={open} mt="30">
        Inserisci malattia o sintomo
      </Button>
    </>
  );
}

function ModalMedicinali() {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <>
      <Modal opened={opened} onClose={close} title="Inserisci i medicinali assunti" centered>
            <JsonInput
                size="xs"
                radius="lg"
                placeholder="Inserisci nome e quantità delle assunzioni qui"
                w="100%"
            />
            <button style={{marginTop:"20px"}}>invia</button>
      </Modal>

      <Button fullWidth variant="default" onClick={open} mt="30">
        Inserisci Medicinali Assunti
      </Button>
    </>
  );
}

function ModalGlicemia() {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <>
      <Modal opened={opened} onClose={close} title="Inserisci Glicemia" centered>
            <JsonInput
                size="xs"
                radius="lg"
                placeholder="Inserisci valore Glicemico qui"
                w="100%"
            />
            <button style={{marginTop:"20px"}}>invia</button>
      </Modal>

      <Button fullWidth variant="default" onClick={open} mt="30">
        Inserisci Valore Glicemia
      </Button>
    </>
  );
}


export { ModalMedicinali, ModalSintomi, ModalGlicemia};