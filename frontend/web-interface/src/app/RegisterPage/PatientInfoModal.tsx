import { Modal, Textarea, Group, Button } from "@mantine/core";

interface PatientInfoModalProps {
    opened: boolean;
    onClose: () => void;
    onSave: () => void;
    tempFattoriRischio: string;
    setTempFattoriRischio: (value: string) => void;
    tempComorbita: string;
    setTempComorbita: (value: string) => void;
    tempPatologiePregresse: string;
    setTempPatologiePregresse: (value: string) => void;
}

/**
 * Modal for collecting additional patient information during registration.
 * Includes fields for risk factors, comorbidities, and past pathologies.
 */
export function PatientInfoModal({
    opened,
    onClose,
    onSave,
    tempFattoriRischio,
    setTempFattoriRischio,
    tempComorbita,
    setTempComorbita,
    tempPatologiePregresse,
    setTempPatologiePregresse,
}: PatientInfoModalProps) {
    return (
        <Modal
            opened={opened}
            onClose={onClose}
            withCloseButton={false}
            centered
            radius={"md"}
            size="auto"
            title="Informazioni aggiuntive sul paziente"
        >
            <Textarea
                size="md"
                radius="md"
                mt={40}
                mb={20}
                w={600}
                label="Fattori di Rischio"
                placeholder="Fattori di Rischio"
                value={tempFattoriRischio}
                onChange={(e) => setTempFattoriRischio(e.currentTarget.value)}
            />
            <Textarea
                size="md"
                radius="md"
                mb={20}
                label="Comorbità"
                placeholder="Comorbità"
                value={tempComorbita}
                onChange={(e) => setTempComorbita(e.currentTarget.value)}
            />
            <Textarea
                size="md"
                radius="md"
                mb={40}
                label="Patologie pregresse"
                placeholder="Patologie pregresse"
                value={tempPatologiePregresse}
                onChange={(e) => setTempPatologiePregresse(e.currentTarget.value)}
            />
            <Group mt="lg" justify="flex-end">
                <Button
                    onClick={onClose}
                    variant="default"
                >
                    Chiudi
                </Button>
                <Button
                    onClick={onSave}
                >
                    Salva
                </Button>
            </Group>
        </Modal>
    );
}