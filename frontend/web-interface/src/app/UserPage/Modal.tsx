import { useDisclosure } from '@mantine/hooks';
import { Modal, Button } from '@mantine/core';
import { JsonInput } from '@mantine/core';

function ModalSintomi() {
  const [opened, { open, close }] = useDisclosure(false);

  return (
    <>
      <Modal opened={opened} onClose={close} title="Authentication" centered>
            <JsonInput
                size="xs"
                radius="lg"
                label="Inserisci una descrizione dei sintomi"
                placeholder="segnala i sintomi qui"
                w="100%"
            />
            <button>invia</button>
      </Modal>

      <Button variant="default" onClick={open}>
        Inserisci malattia o sintomo
      </Button>
    </>
  );
}

function ModalMedicinali() {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <>
      <Modal opened={opened} onClose={close} title="Authentication" centered>
            <JsonInput
                size="xs"
                radius="lg"
                label="Inserisci i medicinali assunti"
                placeholder="Inserisci nome e quantità delle assunzioni qui"
                w="100%"
            />
            <button>invia</button>
      </Modal>

      <Button variant="default" onClick={open}>
        Inserisci Medicinali Assunti
      </Button>
    </>
  );
}

function ModalGlicemia() {
  const [opened, { open, close }] = useDisclosure(false);
  return (
    <>
      <Modal opened={opened} onClose={close} title="Authentication" centered>
            <JsonInput
                size="xs"
                radius="lg"
                label="Inserisci Glicemia"
                placeholder="Inserisci valore Glicemico qui"
                w="100%"
            />
            <button>invia</button>
      </Modal>

      <Button variant="default" onClick={open}>
        Inserisci Valore Glicemia
      </Button>
    </>
  );
}


export { ModalMedicinali, ModalSintomi, ModalGlicemia};